'use strict';

angular.module('creativehubApp')
    .factory('Education', function ($resource, DateUtils) {
        return $resource('api/educations/:id', {}, {
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
