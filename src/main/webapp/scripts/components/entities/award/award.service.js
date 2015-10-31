'use strict';

angular.module('creativehubApp')
    .factory('Award', function ($resource, DateUtils) {
        return $resource('api/awards/:id', {}, {
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
