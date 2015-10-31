'use strict';

angular.module('creativehubApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


