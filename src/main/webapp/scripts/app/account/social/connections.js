'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('connections', {
                parent: 'account',
                url: '/social/connections',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.account.connections'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/social/connections.html',
                        controller: 'ConnectionsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('connections');
                        return $translate.refresh();
                    }]
                }
            });
    });
