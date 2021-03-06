(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$q'];

    function AuthServerProvider($http, $localStorage, $sessionStorage, $q) {
        var service = {
            getToken: getToken,
            hasValidToken: hasValidToken,
            login: login,
            refreshAccessToken: refreshAccessToken,
            storeAuthenticationToken: storeAuthenticationToken,
            logout: logout
        };

        return service;

        function getToken() {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function hasValidToken() {
            var token = this.getToken();
            return token && token.expires && token.expires > new Date().getTime();
        }

        function login(credentials) {
            var data = {
                username: credentials.username,
                password: credentials.password,
                grant_type: "password"
            };
            var headers = {
                'Content-Type': 'application/x-www-form-urlencoded'
            };

            return $http({
                url: 'uaa/oauth/token',
                method: 'post',
                data: data,
                headers: headers,
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]));
                    }
                    return str.join('&');
                }
            }).then(function (data) {
                var accessToken = data.data["access_token"];
                if (angular.isDefined(accessToken)) {
                    service.storeAuthenticationToken(accessToken, credentials.rememberMe);
                }
            });
        }

        function refreshAccessToken() {
            var data = {
                grant_type: "refresh_token"
            };
            var headers = {
                'Content-Type': 'application/x-www-form-urlencoded'
            };

            return $http({
                url: 'uaa/oauth/token',
                method: 'post',
                data: data,
                headers: headers,
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]));
                    }
                    return str.join('&');
                }
            });
        }


        function storeAuthenticationToken(jwt, rememberMe) {
            if (rememberMe) {
                $localStorage.authenticationToken = jwt;

            } else {
                $sessionStorage.authenticationToken = jwt;
            }
        }

        function logout() {
            delete $localStorage.authenticationToken;
            delete $sessionStorage.authenticationToken;
        }
    }
})();
