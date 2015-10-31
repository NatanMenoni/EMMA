'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profileEvent', {
                parent: 'entity',
                url: '/profileEvents',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.profileEvent.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/profileEvent/profileEvents.html',
                        controller: 'ProfileEventController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('profileEvent');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('profileEvent.detail', {
                parent: 'entity',
                url: '/profileEvent/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.profileEvent.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/profileEvent/profileEvent-detail.html',
                        controller: 'ProfileEventDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('profileEvent');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProfileEvent', function($stateParams, ProfileEvent) {
                        return ProfileEvent.get({id : $stateParams.id});
                    }]
                }
            })
            .state('profileEvent.new', {
                parent: 'profileEvent',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/profileEvent/profileEvent-dialog.html',
                        controller: 'ProfileEventDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    name: null,
                                    date: null,
                                    description: null,
                                    location: null,
                                    venue: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('profileEvent', null, { reload: true });
                    }, function() {
                        $state.go('profileEvent');
                    })
                }]
            })
            .state('profileEvent.edit', {
                parent: 'profileEvent',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/profileEvent/profileEvent-dialog.html',
                        controller: 'ProfileEventDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProfileEvent', function(ProfileEvent) {
                                return ProfileEvent.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('profileEvent', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
