(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('authInterceptor', authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$location', '$localStorage', '$sessionStorage'];

    function authInterceptor ($rootScope, $q, $location, $localStorage, $sessionStorage) {
        var service = {
            request: request
        };

        return service;

        function request (config) {
            /*jshint camelcase: false */
            config.headers = config.headers || {};
            var token = getAccessToken();
            
            if (token) {
                config.headers.Authorization = 'Bearer ' + token['access_token'];
            }
            
            return config;
        }

        function getAccessToken() {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }
    }
})();
