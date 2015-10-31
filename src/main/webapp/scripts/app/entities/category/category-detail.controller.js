'use strict';

angular.module('creativehubApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, WorkPiece) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
