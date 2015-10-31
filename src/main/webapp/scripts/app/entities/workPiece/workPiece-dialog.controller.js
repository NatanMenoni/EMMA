'use strict';

angular.module('creativehubApp').controller('WorkPieceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'WorkPiece', 'WorkCollection', 'Tag', 'User', 'Category',
        function($scope, $stateParams, $modalInstance, entity, WorkPiece, WorkCollection, Tag, User, Category) {

        $scope.workPiece = entity;
        $scope.workcollections = WorkCollection.query();
        $scope.tags = Tag.query();
        $scope.users = User.query();
        $scope.categorys = Category.query();
        $scope.load = function(id) {
            WorkPiece.get({id : id}, function(result) {
                $scope.workPiece = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:workPieceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.workPiece.id != null) {
                WorkPiece.update($scope.workPiece, onSaveFinished);
            } else {
                WorkPiece.save($scope.workPiece, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
