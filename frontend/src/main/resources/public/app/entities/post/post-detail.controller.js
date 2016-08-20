(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Post', 'PostComment', 'Tag', 'Principal'];

    function PostDetailController($scope, $rootScope, $stateParams, entity, Post, PostComment, Tag, Principal) {
        var vm = this;

        vm.post = entity;
        vm.isAuthenticated = Principal.isAuthenticated;

        var unsubscribe = $rootScope.$on('gatewayApp:postUpdate', function(event, result) {
            vm.post = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
