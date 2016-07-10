(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Post', 'Tag'];

    function PostDetailController($scope, $rootScope, $stateParams, entity, Post, Tag) {
        var vm = this;

        vm.post = entity;

        var unsubscribe = $rootScope.$on('gatewayApp:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
