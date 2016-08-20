// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g,'..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/resources/public/dep/angular/angular.js',
            'src/main/resources/public/dep/angular-aria/angular-aria.js',
            'src/main/resources/public/dep/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/resources/public/dep/angular-cache-buster/angular-cache-buster.js',
            'src/main/resources/public/dep/angular-cookies/angular-cookies.js',
            'src/main/resources/public/dep/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'src/main/resources/public/dep/ngstorage/ngStorage.js',
            'src/main/resources/public/dep/angular-loading-bar/build/loading-bar.js',
            'src/main/resources/public/dep/angular-resource/angular-resource.js',
            'src/main/resources/public/dep/angular-sanitize/angular-sanitize.js',
            'src/main/resources/public/dep/angular-translate/angular-translate.js',
            'src/main/resources/public/dep/messageformat/messageformat.js',
            'src/main/resources/public/dep/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'src/main/resources/public/dep/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'src/main/resources/public/dep/angular-ui-router/release/angular-ui-router.js',
            'src/main/resources/public/dep/jquery/dist/jquery.js',
            'src/main/resources/public/dep/json3/lib/json3.js',
            'src/main/resources/public/dep/marked/lib/marked.js',
            'src/main/resources/public/dep/ng-file-upload/ng-file-upload.js',
            'src/main/resources/public/dep/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/resources/public/dep/angular-marked/dist/angular-marked.js',
            'src/main/resources/public/dep/highlightjs/highlight.pack.js',
            'src/main/resources/public/dep/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/resources/public/dep/bootstrap-sass/assets/javascripts/bootstrap.js',
            'src/main/resources/public/dep/angular-mocks/angular-mocks.js',
            'src/main/resources/public/dep/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'src/main/resources/public/dep/bootstrap-material-design/dist/js/material.js',
            'src/main/resources/public/dep/bootstrap-material-design/dist/js/ripples.js',
            // endbower
            'src/main/resources/public/app/app.module.js',
            'src/main/resources/public/app/app.state.js',
            'src/main/resources/public/app/app.constants.js',
            'src/main/resources/public/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
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
