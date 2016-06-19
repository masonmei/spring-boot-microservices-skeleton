(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlogDialogController', BlogDialogController);

    BlogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Blog', 'Tag'];

    function BlogDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Blog, Tag) {
        var vm = this;

        vm.blog = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tags = Tag.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.blog.id !== null) {
                Blog.update({id: vm.blog.id}, vm.blog, onSaveSuccess, onSaveError);
            } else {
                Blog.save(vm.blog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('gatewayApp:blogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
