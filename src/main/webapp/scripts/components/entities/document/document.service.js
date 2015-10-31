'use strict';

angular.module('creativehubApp')
    .factory('Document', function ($resource, DateUtils) {
        return $resource('api/documents/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
