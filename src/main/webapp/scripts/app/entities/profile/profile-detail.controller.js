'use strict';

angular.module('creativehubApp')
    .controller('ProfileDetailController', function ($scope, $rootScope, $stateParams, entity, Profile, WorkCollection, User, ProfileEvent, WorkExperience, Education, Award) {
        $scope.profile = entity;
        $scope.load = function (id) {
            Profile.get({id: id}, function(result) {
                $scope.profile = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:profileUpdate', function(event, result) {
            $scope.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
