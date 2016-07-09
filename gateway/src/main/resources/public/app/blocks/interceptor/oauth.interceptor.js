(function () {
    'use strict';

    angular.module('gatewayApp').factory('oauthService', oauthService);
    oauthService.$inject = ['$rootScope', 'httpBuffer'];
    function oauthService($rootScope, httpBuffer) {
        var service = {
            loginConfirmed: loginConfirmed,
            loginCancelled: loginCancelled
        };

        return service;

        /**
         * Call this function to indicate that authentication was successfull and trigger a
         * retry of all deferred requests.
         * @param data an optional argument to pass on to $broadcast which may be useful for
         * example if you need to pass through details of the user that was logged in
         * @param configUpdater an optional transformation function that can modify the
         * requests that are retried after having logged in.  This can be used for example
         * to add an authentication token.  It must return the request.
         */
        function loginConfirmed (data, configUpdater) {
            var updater = configUpdater || function (config) {
                    return config;
                };
            $rootScope.$broadcast('event:oauth-loginConfirmed', data);
            httpBuffer.retryAll(updater);
        }

        /**
         * Call this function to indicate that authentication should not proceed.
         * All deferred requests will be abandoned or rejected (if reason is provided).
         * @param data an optional argument to pass on to $broadcast.
         * @param reason if provided, the requests are rejected; abandoned otherwise.
         */
        function loginCancelled(data, reason) {
            httpBuffer.rejectAll(reason);
            $rootScope.$broadcast('event:oauth-loginCancelled', data);
        }
    }


    /**
     * $http interceptor.
     * On 401 response (without 'ignoreAuthModule' option) stores the request
     * and broadcasts 'event:auth-loginRequired'.
     * On 403 response (without 'ignoreAuthModule' option) discards the request
     * and broadcasts 'event:auth-forbidden'.
     */
    angular.module('gatewayApp').factory('oauthInterceptor', oauthInterceptor);
    oauthInterceptor.$inject = ['$rootScope', '$q', 'httpBuffer',  '$localStorage', '$sessionStorage'];
    function oauthInterceptor($rootScope, $q, httpBuffer,  $localStorage, $sessionStorage) {
        var service = {
            request: request,
            responseError: responseError
        };

        return service;

        function request (config) {
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            var token = $localStorage.authenticationToken || $sessionStorage.authenticationToken;

            if (token) {
                config.headers.Authorization = 'Bearer ' + token;
            }

            return config;
        }

        function responseError(rejection) {
            var config = rejection.config || {};
            if (!config.ignoreAuthModule) {
                switch (rejection.status) {
                    case 401:
                        if (rejection.data && rejection.data.error && rejection.data.error === 'invalid_token') {
                            var deferred = $q.defer();
                            var bufferLength = httpBuffer.append(config, deferred);
                            if (bufferLength === 1)
                                $rootScope.$broadcast('event:oauth-loginRequired', rejection);
                            return deferred.promise;
                        } else {
                            break;
                        }
                    case 403:
                        $rootScope.$broadcast('event:oauth-forbidden', rejection);
                        break;
                }
            }
            // otherwise, default behaviour
            return $q.reject(rejection);
        }
    }

    /**
     * Private module, a utility, required internally by 'http-auth-interceptor'.
     */

    angular.module('gatewayApp').factory('httpBuffer', httpBuffer);
    httpBuffer.$inject = ['$injector'];
    function httpBuffer($injector) {
        /** Holds all the requests, so they can be re-requested in future. */
        var buffer = [];

        /** Service initialized later because of circular dependency problem. */
        var $http;

        function retryHttpRequest(config, deferred) {
            function successCallback(response) {
                deferred.resolve(response);
            }

            function errorCallback(response) {
                deferred.reject(response);
            }

            $http = $http || $injector.get('$http');
            $http(config).then(successCallback, errorCallback);
        }

        return {
            /**
             * Appends HTTP request configuration object with deferred response attached to buffer.
             * @return {Number} The new length of the buffer.
             */
            append: function (config, deferred) {
                return buffer.push({
                    config: config,
                    deferred: deferred
                });
            },

            /**
             * Abandon or reject (if reason provided) all the buffered requests.
             */
            rejectAll: function (reason) {
                if (reason) {
                    for (var i = 0; i < buffer.length; ++i) {
                        buffer[i].deferred.reject(reason);
                    }
                }
                buffer = [];
            },

            /**
             * Retries all the buffered requests clears the buffer.
             */
            retryAll: function (updater) {
                for (var i = 0; i < buffer.length; ++i) {
                    var _cfg = updater(buffer[i].config);
                    if (_cfg !== false)
                        retryHttpRequest(_cfg, buffer[i].deferred);
                }
                buffer = [];
            }
        };
    }
})();