(function () {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Post', 'AlertService', 'pagingParams', 'paginationConstants'];

    function HomeController($scope, $state, Post, AlertService, pagingParams, paginationConstants) {
        var vm = this;

        vm.transition = transition;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        /* Posts */
        vm.postPage = null;
        vm.latest5 = null;
        vm.popular5 = null;

        latestPosts();

        function latestPosts() {
            Post.query({
                id: 'latest',
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function latest5Posts() {
            Post.query({
                id: 'latest',
                page: 0,
                size: 5,
                sort: sort()
            }, function (data, headers) {
                vm.latest5 = data;
            }, onError);
        }

        function top5PopularPosts() {
            Post.query({
                id: 'popular',
                page: 0,
                size: 5,
                sort: sort()
            }, function (data, headers) {
                vm.popular5 = data;
            }, onError);
        }

        function sort() {
            // var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            // if (vm.predicate !== 'id') {
            //     result.push('id');
            // }
            // return result;
            return [];
        }

        function onSuccess(data, headers) {
            vm.totalItems = headers('X-Total-Count');
            vm.postPage = data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
