(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Comment', Comment);

    Comment.$inject = ['$resource', 'DateUtils'];

    function Comment ($resource, DateUtils) {
        var resourceUrl =  'blog/api/posts/:postId/comments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
