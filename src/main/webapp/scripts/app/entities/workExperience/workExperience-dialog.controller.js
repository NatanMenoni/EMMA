'use strict';

angular.module('creativehubApp').controller('WorkExperienceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'WorkExperience',
        function($scope, $stateParams, $modalInstance, entity, WorkExperience) {

        $scope.workExperience = entity;
        $scope.load = function(id) {
            WorkExperience.get({id : id}, function(result) {
                $scope.workExperience = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:workExperienceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.workExperience.id != null) {
                WorkExperience.update($scope.workExperience, onSaveFinished);
            } else {
                WorkExperience.save($scope.workExperience, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
