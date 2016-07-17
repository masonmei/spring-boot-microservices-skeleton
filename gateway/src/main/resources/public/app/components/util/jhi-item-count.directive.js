(function() {
    'use strict';

    var jhiItemCount = {
        template: '<div class="info">' +
                    'Showing {{(($ctrl.page-1) * 10)==0 ? 1:(($ctrl.page-1) * 10 + 1)}} - ' +
                    '{{($ctrl.page * 10) < $ctrl.queryCount ? ($ctrl.page * 10) : $ctrl.queryCount}} ' +
                    'of {{$ctrl.queryCount}} items.' +
                '</div>',
        bindings: {
            page: '<',
            queryCount: '<total'
        }
    };

    angular
        .module('gatewayApp')
        .component('jhiItemCount', jhiItemCount);
})();
