'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workCollection', {
                parent: 'entity',
                url: '/workCollections',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workCollection.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workCollection/workCollections.html',
                        controller: 'WorkCollectionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workCollection');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workCollection.detail', {
                parent: 'entity',
                url: '/workCollection/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workCollection.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workCollection/workCollection-detail.html',
                        controller: 'WorkCollectionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workCollection');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WorkCollection', function($stateParams, WorkCollection) {
                        return WorkCollection.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workCollection.new', {
                parent: 'workCollection',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workCollection/workCollection-dialog.html',
                        controller: 'WorkCollectionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    date: null,
                                    coverImagePath: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('workCollection', null, { reload: true });
                    }, function() {
                        $state.go('workCollection');
                    })
                }]
            })
            .state('workCollection.edit', {
                parent: 'workCollection',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workCollection/workCollection-dialog.html',
                        controller: 'WorkCollectionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['WorkCollection', function(WorkCollection) {
                                return WorkCollection.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workCollection', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
