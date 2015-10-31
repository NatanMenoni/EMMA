'use strict';

angular.module('creativehubApp').controller('AwardDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Award',
        function($scope, $stateParams, $modalInstance, entity, Award) {

        $scope.award = entity;
        $scope.load = function(id) {
            Award.get({id : id}, function(result) {
                $scope.award = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:awardUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.award.id != null) {
                Award.update($scope.award, onSaveFinished);
            } else {
                Award.save($scope.award, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
