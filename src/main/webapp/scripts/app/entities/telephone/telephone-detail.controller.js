'use strict';

angular.module('creativehubApp')
    .controller('TelephoneDetailController', function ($scope, $rootScope, $stateParams, entity, Telephone) {
        $scope.telephone = entity;
        $scope.load = function (id) {
            Telephone.get({id: id}, function(result) {
                $scope.telephone = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:telephoneUpdate', function(event, result) {
            $scope.telephone = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
