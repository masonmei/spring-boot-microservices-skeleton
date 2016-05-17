// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '../../',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'main/dep/jquery/dist/jquery.js',
            'main/dep/angular/angular.js',
            'main/dep/angular-animate/angular-animate.js',
            'main/dep/angular-aria/angular-aria.js',
            'main/dep/angular-messages/angular-messages.js',
            'main/dep/angular-material/angular-material.js',
            'main/dep/marked/lib/marked.js',
            'main/dep/angular-marked/dist/angular-marked.js',
            'main/dep/highlightjs/highlight.pack.js',
            'main/dep/angular-bootstrap/ui-bootstrap-tpls.js',
            'main/dep/angular-cache-buster/angular-cache-buster.js',
            'main/dep/angular-cookies/angular-cookies.js',
            'main/dep/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'main/dep/angular-local-storage/dist/angular-local-storage.js',
            'main/dep/angular-loading-bar/build/loading-bar.js',
            'main/dep/angular-resource/angular-resource.js',
            'main/dep/angular-sanitize/angular-sanitize.js',
            'main/dep/angular-translate/angular-translate.js',
            'main/dep/messageformat/messageformat.js',
            'main/dep/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'main/dep/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'main/dep/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'main/dep/angular-ui-router/release/angular-ui-router.js',
            'main/dep/bootstrap/dist/js/bootstrap.js',
            'main/dep/json3/lib/json3.js',
            'main/dep/ng-file-upload/ng-file-upload.js',
            'main/dep/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'main/dep/angular-mocks/angular-mocks.js',
            // endbower
            'main/javascript/scripts/app/app.js',
            'main/javascript/scripts/app/**/*.js',
            'main/javascript/scripts/components/**/*.+(js|html)',
            'test/javascript/spec/helpers/module.js',
            'test/javascript/spec/helpers/httpBackend.js',
            'test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        preprocessors: {
            './**/*.js': ['coverage']
        },

        reporters: ['dots', 'jenkins', 'coverage', 'progress'],

        jenkinsReporter: {

            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {

            dir: '../target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout : 10000, // default 2000
        browserDisconnectTolerance : 1, // default 0
        browserNoActivityTimeout : 4*60*1000 //default 10000
    });
};
