'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('telephone', {
                parent: 'entity',
                url: '/telephones',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.telephone.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telephone/telephones.html',
                        controller: 'TelephoneController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telephone');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('telephone.detail', {
                parent: 'entity',
                url: '/telephone/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.telephone.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telephone/telephone-detail.html',
                        controller: 'TelephoneDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telephone');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Telephone', function($stateParams, Telephone) {
                        return Telephone.get({id : $stateParams.id});
                    }]
                }
            })
            .state('telephone.new', {
                parent: 'telephone',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telephone/telephone-dialog.html',
                        controller: 'TelephoneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    areaCode: null,
                                    number: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('telephone', null, { reload: true });
                    }, function() {
                        $state.go('telephone');
                    })
                }]
            })
            .state('telephone.edit', {
                parent: 'telephone',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telephone/telephone-dialog.html',
                        controller: 'TelephoneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Telephone', function(Telephone) {
                                return Telephone.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('telephone', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
