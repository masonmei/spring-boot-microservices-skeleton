(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tag', 'Blog'];

    function TagDetailController($scope, $rootScope, $stateParams, entity, Tag, Blog) {
        var vm = this;

        vm.tag = entity;

        var unsubscribe = $rootScope.$on('gatewayApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
