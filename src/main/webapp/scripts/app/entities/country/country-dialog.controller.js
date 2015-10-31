'use strict';

angular.module('creativehubApp').controller('CountryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Country) {

        $scope.country = entity;
        $scope.load = function(id) {
            Country.get({id : id}, function(result) {
                $scope.country = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:countryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.country.id != null) {
                Country.update($scope.country, onSaveFinished);
            } else {
                Country.save($scope.country, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
