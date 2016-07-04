(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('authExpiredInterceptor', authExpiredInterceptor);


    authExpiredInterceptor.$inject = ['$rootScope', '$q', '$injector', '$localStorage', '$sessionStorage'];

    function authExpiredInterceptor($rootScope, $q, $injector, $localStorage, $sessionStorage) {
        var service = {
            responseError: responseError
        };

        return service;

        function responseError(response) {
            // if (response.status === 401) {
            //     console.log(JSON.stringify(response));
            //     var refreshToken = getRefreshToken();
            //     delete $localStorage.authenticationToken;
            //     delete $sessionStorage.authenticationToken;
            //
            //     if (response.data.error === 'invalid_token') {
            //         console.log("Need refresh token")
            //         var AuthServerProvider = $injector.get('AuthServerProvider');
            //         AuthServerProvider.refreshAccessToken(refreshToken).then(function (data) {
            //             var accessToken = data.data["access_token"];
            //             if (angular.isDefined(accessToken)) {
            //                 AuthServerProvider.storeAuthenticationToken(data.data, credentials.rememberMe);
            //             }
            //
            //             return $q.deferred.promise;
            //         });
            //
            //         return response || $q.when(response);
            //     }
            //
            // }
            //
            // var Principal = $injector.get('Principal');
            // if (Principal.isAuthenticated()) {
            //     var Auth = $injector.get('Auth');
            //     Auth.authorize(true);
            // }
            if (response.status === 401) {

                var refreshToken = getRefreshToken();
                delete $localStorage.authenticationToken;
                delete $sessionStorage.authenticationToken;

                var AuthServerProvider = $injector.get('AuthServerProvider');
                AuthServerProvider.refreshAccessToken(refreshToken);

                var Principal = $injector.get('Principal');
                if (Principal.isAuthenticated()) {
                    var Auth = $injector.get('Auth');
                    Auth.authorize(true);
                }
            }

            return $q.reject(response);
        }

        function getToken() {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function getRefreshToken() {
            var token = getToken();

            if (token) {
                return token['refresh_token'];
            }
            return undefined;
        }
    }
})();
