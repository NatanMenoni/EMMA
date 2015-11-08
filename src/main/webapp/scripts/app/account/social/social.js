'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register-social', {
                parent: 'account',
                url: '/register-social',
                data: {
                    roles: [],
                    pageTitle: 'social.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/social/register-social.html',
                        controller: 'RegisterSocialController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('social');
                        return $translate.refresh();
                    }]
                }
            });
    });
