'use strict';

angular.module('creativehubApp').controller('WorkCollectionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'WorkCollection', 'WorkPiece',
        function($scope, $stateParams, $modalInstance, entity, WorkCollection, WorkPiece) {

        $scope.workCollection = entity;
        $scope.workpieces = WorkPiece.query();
        $scope.load = function(id) {
            WorkCollection.get({id : id}, function(result) {
                $scope.workCollection = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:workCollectionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.workCollection.id != null) {
                WorkCollection.update($scope.workCollection, onSaveFinished);
            } else {
                WorkCollection.save($scope.workCollection, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
