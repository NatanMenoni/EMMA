'use strict';

angular.module('creativehubApp')
    .controller('WorkCollectionDetailController', function ($scope, $rootScope, $stateParams, entity, WorkCollection, WorkPiece) {
        $scope.workCollection = entity;
        $scope.load = function (id) {
            WorkCollection.get({id: id}, function(result) {
                $scope.workCollection = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:workCollectionUpdate', function(event, result) {
            $scope.workCollection = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
