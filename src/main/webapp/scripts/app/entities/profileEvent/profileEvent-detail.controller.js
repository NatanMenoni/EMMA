'use strict';

angular.module('creativehubApp')
    .controller('ProfileEventDetailController', function ($scope, $rootScope, $stateParams, entity, ProfileEvent) {
        $scope.profileEvent = entity;
        $scope.load = function (id) {
            ProfileEvent.get({id: id}, function(result) {
                $scope.profileEvent = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:profileEventUpdate', function(event, result) {
            $scope.profileEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
