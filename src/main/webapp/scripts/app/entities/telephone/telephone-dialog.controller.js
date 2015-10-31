'use strict';

angular.module('creativehubApp').controller('TelephoneDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Telephone',
        function($scope, $stateParams, $modalInstance, entity, Telephone) {

        $scope.telephone = entity;
        $scope.load = function(id) {
            Telephone.get({id : id}, function(result) {
                $scope.telephone = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:telephoneUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.telephone.id != null) {
                Telephone.update($scope.telephone, onSaveFinished);
            } else {
                Telephone.save($scope.telephone, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
