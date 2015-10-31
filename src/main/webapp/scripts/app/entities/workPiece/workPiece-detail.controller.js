'use strict';

angular.module('creativehubApp')
    .controller('WorkPieceDetailController', function ($scope, $rootScope, $stateParams, entity, WorkPiece, WorkCollection, Tag, User, Category) {
        $scope.workPiece = entity;
        $scope.load = function (id) {
            WorkPiece.get({id: id}, function(result) {
                $scope.workPiece = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:workPieceUpdate', function(event, result) {
            $scope.workPiece = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
