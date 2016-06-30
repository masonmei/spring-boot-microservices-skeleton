$(function() {

    var options = {  lng : 'zh_cn', resGetPath: 'i18n/__lng__/__ns__.json',fallbackLng : 'en'}

    i18n.init( options, function(t) {
        $(document).i18n();
    });

    $('#cnLangBut').click( function() {
        i18n.setLng('zh_cn', function(t) {
            $(document).i18n();
        });
    });

    $('#enLangBut').click( function() {
        i18n.setLng('en', function(t) {
            $(document).i18n();
        });
    });

});