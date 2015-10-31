'use strict';

angular.module('creativehubApp')
    .controller('UserTypeDetailController', function ($scope, $rootScope, $stateParams, entity, UserType) {
        $scope.userType = entity;
        $scope.load = function (id) {
            UserType.get({id: id}, function(result) {
                $scope.userType = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:userTypeUpdate', function(event, result) {
            $scope.userType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
