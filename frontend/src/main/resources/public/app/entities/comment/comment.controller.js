(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CommentController', CommentController);

    CommentController.$inject = ['$scope', '$state', '$stateParams', 'Comment'];

    function CommentController ($scope, $state, $stateParams, Comment) {
        var vm = this;
        
        vm.comments = [];
        vm.postId = $stateParams.postId;

        loadAll();

        function loadAll() {
            Comment.query({postId: vm.postId}, function(result) {
                vm.comments = result;
            });
        }
    }
})();
