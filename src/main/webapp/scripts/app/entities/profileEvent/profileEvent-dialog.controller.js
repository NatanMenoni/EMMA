'use strict';

angular.module('creativehubApp').controller('ProfileEventDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ProfileEvent',
        function($scope, $stateParams, $modalInstance, entity, ProfileEvent) {

        $scope.profileEvent = entity;
        $scope.load = function(id) {
            ProfileEvent.get({id : id}, function(result) {
                $scope.profileEvent = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:profileEventUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.profileEvent.id != null) {
                ProfileEvent.update($scope.profileEvent, onSaveFinished);
            } else {
                ProfileEvent.save($scope.profileEvent, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
