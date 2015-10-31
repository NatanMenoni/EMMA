'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workExperience', {
                parent: 'entity',
                url: '/workExperiences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workExperience.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workExperience/workExperiences.html',
                        controller: 'WorkExperienceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workExperience');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workExperience.detail', {
                parent: 'entity',
                url: '/workExperience/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workExperience.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workExperience/workExperience-detail.html',
                        controller: 'WorkExperienceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workExperience');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WorkExperience', function($stateParams, WorkExperience) {
                        return WorkExperience.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workExperience.new', {
                parent: 'workExperience',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workExperience/workExperience-dialog.html',
                        controller: 'WorkExperienceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    startingDate: null,
                                    finishingDate: null,
                                    organization: null,
                                    title: null,
                                    location: null,
                                    current: null,
                                    jobDescription: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('workExperience', null, { reload: true });
                    }, function() {
                        $state.go('workExperience');
                    })
                }]
            })
            .state('workExperience.edit', {
                parent: 'workExperience',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workExperience/workExperience-dialog.html',
                        controller: 'WorkExperienceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['WorkExperience', function(WorkExperience) {
                                return WorkExperience.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workExperience', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
