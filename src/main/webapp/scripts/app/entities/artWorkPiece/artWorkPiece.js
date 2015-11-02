'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('artWorkPiece', {
                parent: 'entity',
                url: '/artWorkPieces',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.artWorkPiece.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artWorkPiece/artWorkPieces.html',
                        controller: 'ArtWorkPieceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('artWorkPiece');
                        $translatePartialLoader.addPart('commercialState');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('artWorkPiece.detail', {
                parent: 'entity',
                url: '/artWorkPiece/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.artWorkPiece.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artWorkPiece/artWorkPiece-detail.html',
                        controller: 'ArtWorkPieceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('artWorkPiece');
                        $translatePartialLoader.addPart('commercialState');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ArtWorkPiece', function($stateParams, ArtWorkPiece) {
                        return ArtWorkPiece.get({id : $stateParams.id});
                    }]
                }
            })
            .state('artWorkPiece.new', {
                parent: 'artWorkPiece',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/artWorkPiece/artWorkPiece-dialog.html',
                        controller: 'ArtWorkPieceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    width: null,
                                    height: null,
                                    depth: null,
                                    commercialState: null,
                                    price: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('artWorkPiece', null, { reload: true });
                    }, function() {
                        $state.go('artWorkPiece');
                    })
                }]
            })
            .state('artWorkPiece.edit', {
                parent: 'artWorkPiece',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/artWorkPiece/artWorkPiece-dialog.html',
                        controller: 'ArtWorkPieceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ArtWorkPiece', function(ArtWorkPiece) {
                                return ArtWorkPiece.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artWorkPiece', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
