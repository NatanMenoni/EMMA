'use strict';

angular.module('creativehubApp').controller('AddressDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Address', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Address, Country) {

        $scope.address = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Address.get({id : id}, function(result) {
                $scope.address = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:addressUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.address.id != null) {
                Address.update($scope.address, onSaveFinished);
            } else {
                Address.save($scope.address, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
