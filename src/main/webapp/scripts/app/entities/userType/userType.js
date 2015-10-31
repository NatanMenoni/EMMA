'use strict';

angular.module('creativehubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userType', {
                parent: 'entity',
                url: '/userTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.userType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userType/userTypes.html',
                        controller: 'UserTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userType.detail', {
                parent: 'entity',
                url: '/userType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'creativehubApp.userType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userType/userType-detail.html',
                        controller: 'UserTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserType', function($stateParams, UserType) {
                        return UserType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userType.new', {
                parent: 'userType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userType/userType-dialog.html',
                        controller: 'UserTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userType', null, { reload: true });
                    }, function() {
                        $state.go('userType');
                    })
                }]
            })
            .state('userType.edit', {
                parent: 'userType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userType/userType-dialog.html',
                        controller: 'UserTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserType', function(UserType) {
                                return UserType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
