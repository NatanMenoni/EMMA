'use strict';

angular.module('creativehubApp')
    .controller('EducationDetailController', function ($scope, $rootScope, $stateParams, entity, Education) {
        $scope.education = entity;
        $scope.load = function (id) {
            Education.get({id: id}, function(result) {
                $scope.education = result;
            });
        };
        var unsubscribe = $rootScope.$on('creativehubApp:educationUpdate', function(event, result) {
            $scope.education = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
