'use strict';

angular.module('creativehubApp')
    .factory('SocialConnection', function Account($resource) {
        return $resource('api/account/connections/:providerId', {}, {
        	'get': { method: 'GET', isArray: true}
        });
    });
