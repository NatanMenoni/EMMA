'use strict';

angular.module('creativehubApp')
    .controller('DocumentDetailController', function ($scope, $rootScope, $stateParams, entity, Document, Country) {
        $scope.document = entity;
        $scope.load = function (id) {
            Document.get({id: id}, function(result) {
                $scope.document = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:documentUpdate', function(event, result) {
            $scope.document = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
