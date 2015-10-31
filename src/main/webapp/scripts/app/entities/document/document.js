'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('document', {
                parent: 'entity',
                url: '/documents',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.document.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/document/documents.html',
                        controller: 'DocumentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('document');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('document.detail', {
                parent: 'entity',
                url: '/document/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.document.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/document/document-detail.html',
                        controller: 'DocumentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('document');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Document', function($stateParams, Document) {
                        return Document.get({id : $stateParams.id});
                    }]
                }
            })
            .state('document.new', {
                parent: 'document',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/document/document-dialog.html',
                        controller: 'DocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    documentType: null,
                                    documentNumber: null,
                                    documentImagePath: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('document', null, { reload: true });
                    }, function() {
                        $state.go('document');
                    })
                }]
            })
            .state('document.edit', {
                parent: 'document',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/document/document-dialog.html',
                        controller: 'DocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Document', function(Document) {
                                return Document.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('document', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
