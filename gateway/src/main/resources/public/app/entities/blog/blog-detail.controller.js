(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlogDetailController', BlogDetailController);

    BlogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Blog', 'Tag'];

    function BlogDetailController($scope, $rootScope, $stateParams, entity, Blog, Tag) {
        var vm = this;

        vm.blog = entity;

        var unsubscribe = $rootScope.$on('gatewayApp:blogUpdate', function(event, result) {
            vm.blog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
