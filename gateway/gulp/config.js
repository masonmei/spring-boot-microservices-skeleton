module.exports = {
    app: 'src/main/resources/public/',
    dist: 'target/www/',
    test: 'src/test/javascript/',
    bower: 'src/main/resources/public/dep/',
    tmp: 'target/tmp',
    revManifest: 'target/tmp/rev-manifest.json',
    port: 9000,
    apiPort: 8080,
    liveReloadPort: 35729,
    uri: 'http://localhost:',
    constantTemplate:
        '(function () {\n' +
        '    \'use strict\';\n' +
        '    // DO NOT EDIT THIS FILE, EDIT THE GULP TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n' +
        '    angular\n' +
        '        .module(\'<%- moduleName %>\')\n' +
        '<% constants.forEach(function(constant) { %>        .constant(\'<%- constant.name %>\', <%= constant.value %>)\n<% }) %>;\n' +
        '})();\n'
};
