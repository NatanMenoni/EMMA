'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('award', {
                parent: 'entity',
                url: '/awards',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.award.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/award/awards.html',
                        controller: 'AwardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('award');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('award.detail', {
                parent: 'entity',
                url: '/award/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.award.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/award/award-detail.html',
                        controller: 'AwardDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('award');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Award', function($stateParams, Award) {
                        return Award.get({id : $stateParams.id});
                    }]
                }
            })
            .state('award.new', {
                parent: 'award',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/award/award-dialog.html',
                        controller: 'AwardDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    year: null,
                                    name: null,
                                    issuer: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('award', null, { reload: true });
                    }, function() {
                        $state.go('award');
                    })
                }]
            })
            .state('award.edit', {
                parent: 'award',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/award/award-dialog.html',
                        controller: 'AwardDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Award', function(Award) {
                                return Award.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('award', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
