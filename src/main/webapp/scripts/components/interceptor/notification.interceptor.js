 'use strict';

angular.module('creativehubApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-creativehubApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-creativehubApp-params')});
                }
                return response;
            }
        };
    });
