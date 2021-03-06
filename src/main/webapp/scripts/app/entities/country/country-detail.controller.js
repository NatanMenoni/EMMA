'use strict';

angular.module('creativehubApp')
    .controller('CountryDetailController', function ($scope, $rootScope, $stateParams, entity, Country) {
        $scope.country = entity;
        $scope.load = function (id) {
            Country.get({id: id}, function(result) {
                $scope.country = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:countryUpdate', function(event, result) {
            $scope.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
