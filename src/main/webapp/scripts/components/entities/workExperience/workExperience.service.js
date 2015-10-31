'use strict';

angular.module('creativehubApp')
    .factory('WorkExperience', function ($resource, DateUtils) {
        return $resource('api/workExperiences/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startingDate = DateUtils.convertLocaleDateFromServer(data.startingDate);
                    data.finishingDate = DateUtils.convertLocaleDateFromServer(data.finishingDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startingDate = DateUtils.convertLocaleDateToServer(data.startingDate);
                    data.finishingDate = DateUtils.convertLocaleDateToServer(data.finishingDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startingDate = DateUtils.convertLocaleDateToServer(data.startingDate);
                    data.finishingDate = DateUtils.convertLocaleDateToServer(data.finishingDate);
                    return angular.toJson(data);
                }
            }
        });
    });
