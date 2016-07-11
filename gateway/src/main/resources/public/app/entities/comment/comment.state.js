(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comment', {
            parent: 'entity',
            url: '/comment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comment/comments.html',
                    controller: 'CommentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comment-detail', {
            parent: 'entity',
            url: '/comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comment/comment-detail.html',
                    controller: 'CommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Comment', function($stateParams, Comment) {
                    return Comment.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('comment.new', {
            parent: 'comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-dialog.html',
                    controller: 'CommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                postId: null,
                                author: null,
                                authorEmail: null,
                                authorUrl: null,
                                authorIp: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comment', null, { reload: true });
                }, function() {
                    $state.go('comment');
                });
            }]
        })
        .state('comment.edit', {
            parent: 'comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-dialog.html',
                    controller: 'CommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Comment', function(Comment) {
                            return Comment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comment.delete', {
            parent: 'comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comment/comment-delete-dialog.html',
                    controller: 'CommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Comment', function(Comment) {
                            return Comment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
