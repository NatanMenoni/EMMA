'use strict';

angular.module('creativehubApp')
    .controller('ArtWorkPieceDetailController', function ($scope, $rootScope, $stateParams, entity, ArtWorkPiece) {
        $scope.artWorkPiece = entity;
        $scope.load = function (id) {
            ArtWorkPiece.get({id: id}, function(result) {
                $scope.artWorkPiece = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:artWorkPieceUpdate', function(event, result) {
            $scope.artWorkPiece = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
