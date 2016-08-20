(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostDialogController', PostDialogController);

    PostDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Post', 'Tag'];

    function PostDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Post, Tag) {
        var vm = this;

        vm.post = entity;
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
            if (vm.post.id !== null) {
                Post.update({id: vm.post.id}, vm.post, onSaveSuccess, onSaveError);
            } else {
                Post.save(vm.post, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('gatewayApp:postUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
