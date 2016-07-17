(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Post', Post);

    Post.$inject = ['$resource'];

    function Post ($resource) {
        var resourceUrl =  'blog/api/posts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    angular.module('gatewayApp')
        .factory('PostComment', PostComment);
    PostComment.$inject = ['$resource'];
    function PostComment($resource) {
        var resourceUrl = 'blog/api/posts/:postId/comments/:commentId';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET'}
        });
    }
})();
