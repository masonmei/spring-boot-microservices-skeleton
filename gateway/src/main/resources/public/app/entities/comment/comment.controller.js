(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CommentController', CommentController);

    CommentController.$inject = ['$scope', '$state', 'Comment'];

    function CommentController ($scope, $state, Comment) {
        var vm = this;
        
        vm.comments = [];

        loadAll();

        function loadAll() {
            Comment.query(function(result) {
                vm.comments = result;
            });
        }
    }
})();
