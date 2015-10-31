'use strict';

angular.module('creativehubApp')
    .controller('AddressDetailController', function ($scope, $rootScope, $stateParams, entity, Address, Country) {
        $scope.address = entity;
        $scope.load = function (id) {
            Address.get({id: id}, function(result) {
                $scope.address = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:addressUpdate', function(event, result) {
            $scope.address = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
