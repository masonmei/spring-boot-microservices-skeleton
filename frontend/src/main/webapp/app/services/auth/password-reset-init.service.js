(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('PasswordResetInit', PasswordResetInit);

    PasswordResetInit.$inject = ['$resource'];

    function PasswordResetInit($resource) {
        var service = $resource('uaa/api/account/reset_password/init', {}, {});

        return service;
    }
})();
