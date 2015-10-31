'use strict';

angular.module('creativehubApp').controller('EducationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Education',
        function($scope, $stateParams, $modalInstance, entity, Education) {

        $scope.education = entity;
        $scope.load = function(id) {
            Education.get({id : id}, function(result) {
                $scope.education = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:educationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.education.id != null) {
                Education.update($scope.education, onSaveFinished);
            } else {
                Education.save($scope.education, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
