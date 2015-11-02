'use strict';

angular.module('creativehubApp').controller('ArtWorkPieceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ArtWorkPiece',
        function($scope, $stateParams, $modalInstance, entity, ArtWorkPiece) {

        $scope.artWorkPiece = entity;
        $scope.load = function(id) {
            ArtWorkPiece.get({id : id}, function(result) {
                $scope.artWorkPiece = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:artWorkPieceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.artWorkPiece.id != null) {
                ArtWorkPiece.update($scope.artWorkPiece, onSaveFinished);
            } else {
                ArtWorkPiece.save($scope.artWorkPiece, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
