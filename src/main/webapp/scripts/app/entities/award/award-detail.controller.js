'use strict';

angular.module('creativehubApp')
    .controller('AwardDetailController', function ($scope, $rootScope, $stateParams, entity, Award) {
        $scope.award = entity;
        $scope.load = function (id) {
            Award.get({id: id}, function(result) {
                $scope.award = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:awardUpdate', function(event, result) {
            $scope.award = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
