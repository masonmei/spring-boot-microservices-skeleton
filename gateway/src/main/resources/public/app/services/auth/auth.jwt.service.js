(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$q'];

    function AuthServerProvider ($http, $localStorage, $sessionStorage, $q) {
        var service = {
            getToken: getToken,
            hasValidToken: hasValidToken,
            login: login,
            loginWithToken: loginWithToken,
            storeAuthenticationToken: storeAuthenticationToken,
            logout: logout
        };

        return service;

        function getToken () {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function hasValidToken () {
            var token = this.getToken();
            return token && token.expires && token.expires > new Date().getTime();
        }

        function login (credentials) {
            var data = {
                username: credentials.username,
                password: credentials.password,
                grant_type: "password"
            };
            var headers = {
                'Content-Type': 'application/x-www-form-urlencoded',
                // "Authorization" : "Basic d2ViX2FwcDo="
            };

            return $http({
                url: 'uaa/oauth/token',
                method: 'post',
                data: data,
                headers: headers,
                transformRequest: function(obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]));
                    }
                    return str.join('&');
                }
            }).then(function (data) {
                var accessToken = data.data["access_token"];
                var refreshToken = data.data["refresh_token"];
                if (angular.isDefined(accessToken) && angular.isDefined(refreshToken)) {
                    service.storeAuthenticationToken(accessToken, refreshToken, credentials.rememberMe);
                } else if (!angular.isDefined(refreshToken)){
                    service.storeAuthenticationToken(accessToken, '', credentials.rememberMe);
                }
            });
        }

        function loginWithRefreshToken() {
            
        }
        
        function loginWithToken(jwt, rememberMe) {
            var deferred = $q.defer();

            if (angular.isDefined(jwt)) {
                this.storeAuthenticationToken(jwt, rememberMe);
                deferred.resolve(jwt);
            } else {
                deferred.reject();
            }

            return deferred.promise;
        }

        function storeAuthenticationToken(jwt, refreshToken, rememberMe) {
            if(rememberMe){
                $localStorage.authenticationToken = jwt;
                $localStorage.authenticationRefreshToken = refreshToken;
            } else {
                $sessionStorage.authenticationToken = jwt;
                $sessionStorage.authenticationRefreshToken = refreshToken;
            }
        }

        function logout () {
            delete $localStorage.authenticationToken;
            delete $sessionStorage.authenticationToken;

            delete $localStorage.authenticationRefreshToken;
            delete $sessionStorage.authenticationRefreshToken;
        }
    }
})();
