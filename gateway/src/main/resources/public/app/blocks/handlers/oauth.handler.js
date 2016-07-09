(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('oauthHandler', oauthHandler);

    oauthHandler.$inject = ['$rootScope', 'oauthService', 'AuthServerProvider', 'LoginService', 'VERSION'];

    function oauthHandler($rootScope, oauthService, AuthServerProvider, LoginService, VERSION) {
        return {
            initialize: initialize
        };

        function initialize() {
            $rootScope.VERSION = VERSION;

            $rootScope.$on('event:oauth-loginConfirmed', function (event, data) {
                console.log('login confirmed: refresh token success.');
            });

            $rootScope.$on('event:oauth-loginCancelled',  function(event, data) {
                console.log("login cancelled.");
                LoginService.open();
            });

            $rootScope.$on('event:oauth-loginRequired', function (event, rejection) {
                AuthServerProvider.refreshAccessToken().then(
                    function (response) {
                        console.log(response);
                        var accessToken = response.data["access_token"];
                        if (angular.isDefined(accessToken)) {
                            console.log("Old Token: ", accessToken);
                            AuthServerProvider.logout();
                            AuthServerProvider.storeAuthenticationToken(accessToken, false);
                            oauthService.loginConfirmed('refresh token success', function (config) {
                                /*jshint camelcase: false */
                                config.headers = config.headers || {};
                                var token = AuthServerProvider.getToken();
                                console.log("New Token: " + token);

                                if (token) {
                                    config.headers.Authorization = 'Bearer ' + token;
                                }
                                return config;
                            })
                        } else {
                            console.log("failed to refresh access token");
                            oauthService.loginCancelled(response.data, "refresh token failed");
                        }
                    }).catch(function (response) {
                        console.log(response);
                        oauthService.loginCancelled(response.data, "refresh token failed");
                    }
                );
            });
        }
    }
})();
