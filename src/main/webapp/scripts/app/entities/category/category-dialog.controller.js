'use strict';

angular.module('creativehubApp').controller('CategoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Category', 'WorkPiece',
        function($scope, $stateParams, $modalInstance, entity, Category, WorkPiece) {

        $scope.category = entity;
        $scope.categorys = Category.query();
        $scope.workpieces = WorkPiece.query();
        $scope.load = function(id) {
            Category.get({id : id}, function(result) {
                $scope.category = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:categoryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.category.id != null) {
                Category.update($scope.category, onSaveFinished);
            } else {
                Category.save($scope.category, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
