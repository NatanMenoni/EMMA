'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workPiece', {
                parent: 'entity',
                url: '/workPieces',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workPiece.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workPiece/workPieces.html',
                        controller: 'WorkPieceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workPiece');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workPiece.detail', {
                parent: 'entity',
                url: '/workPiece/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.workPiece.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workPiece/workPiece-detail.html',
                        controller: 'WorkPieceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workPiece');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WorkPiece', function($stateParams, WorkPiece) {
                        return WorkPiece.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workPiece.new', {
                parent: 'workPiece',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workPiece/workPiece-dialog.html',
                        controller: 'WorkPieceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    imagePath: null,
                                    yearOfCreation: null,
                                    inputDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('workPiece', null, { reload: true });
                    }, function() {
                        $state.go('workPiece');
                    })
                }]
            })
            .state('workPiece.edit', {
                parent: 'workPiece',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workPiece/workPiece-dialog.html',
                        controller: 'WorkPieceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['WorkPiece', function(WorkPiece) {
                                return WorkPiece.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workPiece', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
