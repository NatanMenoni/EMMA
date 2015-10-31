'use strict';

angular.module('creativehubApp').controller('UserTypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserType',
        function($scope, $stateParams, $modalInstance, entity, UserType) {

        $scope.userType = entity;
        $scope.load = function(id) {
            UserType.get({id : id}, function(result) {
                $scope.userType = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:userTypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.userType.id != null) {
                UserType.update($scope.userType, onSaveFinished);
            } else {
                UserType.save($scope.userType, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
