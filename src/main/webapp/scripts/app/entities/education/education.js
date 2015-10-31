'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('education', {
                parent: 'entity',
                url: '/educations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.education.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/education/educations.html',
                        controller: 'EducationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('education');
                        $translatePartialLoader.addPart('degreeType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('education.detail', {
                parent: 'entity',
                url: '/education/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.education.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/education/education-detail.html',
                        controller: 'EducationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('education');
                        $translatePartialLoader.addPart('degreeType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Education', function($stateParams, Education) {
                        return Education.get({id : $stateParams.id});
                    }]
                }
            })
            .state('education.new', {
                parent: 'education',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/education/education-dialog.html',
                        controller: 'EducationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    institutionName: null,
                                    startingYear: null,
                                    finishingYear: null,
                                    degree: null,
                                    degreeOtherDescription: null,
                                    fieldOfStudy: null,
                                    careerName: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('education', null, { reload: true });
                    }, function() {
                        $state.go('education');
                    })
                }]
            })
            .state('education.edit', {
                parent: 'education',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/education/education-dialog.html',
                        controller: 'EducationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Education', function(Education) {
                                return Education.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('education', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
