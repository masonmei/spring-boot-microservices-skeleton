(function() {
    'use strict';

    angular
        .module('gatewayApp', [
            'ngStorage', 
            'tmh.dynamicLocale',
            'pascalprecht.translate', 
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'hc.marked',
            // add here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', 'oauthHandler'];

    function run(stateHandler, translationHandler, oauthHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
        oauthHandler.initialize();
    }
})();
