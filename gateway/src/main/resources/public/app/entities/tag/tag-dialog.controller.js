(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TagDialogController', TagDialogController);

    TagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tag', 'Blog'];

    function TagDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Tag, Blog) {
        var vm = this;

        vm.tag = entity;
        vm.clear = clear;
        vm.save = save;
        vm.blogs = Blog.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.tag.id !== null) {
                Tag.update({id: vm.tag.id}, vm.tag, onSaveSuccess, onSaveError);
            } else {
                Tag.save(vm.tag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('gatewayApp:tagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
