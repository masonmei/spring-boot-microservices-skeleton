(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Comment'];

    function CommentDetailController($scope, $rootScope, $stateParams, entity, Comment) {
        var vm = this;

        vm.comment = entity;

        var unsubscribe = $rootScope.$on('gatewayApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
