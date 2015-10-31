'use strict';

angular.module('creativehubApp')
    .controller('WorkExperienceDetailController', function ($scope, $rootScope, $stateParams, entity, WorkExperience) {
        $scope.workExperience = entity;
        $scope.load = function (id) {
            WorkExperience.get({id: id}, function(result) {
                $scope.workExperience = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:workExperienceUpdate', function(event, result) {
            $scope.workExperience = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
