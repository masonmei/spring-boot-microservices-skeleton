(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('post', {
            parent: 'entity',
            url: '/post?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'post.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/post/posts.html',
                    controller: 'PostController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('post');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('post-detail', {
            parent: 'entity',
            url: '/post/{id}',
            data: {
                authorities: [],
                pageTitle: 'post.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/post/post-detail.html',
                    controller: 'PostDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('post');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Post', function($stateParams, Post) {
                    return Post.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('post.new', {
            parent: 'post',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/post/post-dialog.html',
                    controller: 'PostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                author: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('post', null, { reload: true });
                }, function() {
                    $state.go('post');
                });
            }]
        })
        .state('post.edit', {
            parent: 'post',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/post/post-dialog.html',
                    controller: 'PostDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Post', function(Post) {
                            return Post.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('post', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('post.delete', {
            parent: 'post',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/post/post-delete-dialog.html',
                    controller: 'PostDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Post', function(Post) {
                            return Post.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('post', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
