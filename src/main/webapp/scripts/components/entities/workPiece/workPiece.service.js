'use strict';

angular.module('creativehubApp')
    .factory('WorkPiece', function ($resource, DateUtils) {
        return $resource('api/workPieces/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.inputDate = DateUtils.convertLocaleDateFromServer(data.inputDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.inputDate = DateUtils.convertLocaleDateToServer(data.inputDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.inputDate = DateUtils.convertLocaleDateToServer(data.inputDate);
                    return angular.toJson(data);
                }
            }
        });
    });
