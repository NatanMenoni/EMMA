'use strict';

angular.module('creativehubApp').controller('DocumentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Document', 'Country',
        function($scope, $stateParams, $modalInstance, entity, Document, Country) {

        $scope.document = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            Document.get({id : id}, function(result) {
                $scope.document = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:documentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.document.id != null) {
                Document.update($scope.document, onSaveFinished);
            } else {
                Document.save($scope.document, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
