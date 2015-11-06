'use strict';

angular.module('creativehubApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV, CategoryExtended) {
        //JHipster Generated---------------------------------------------------
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
        //---------------------------------------------------------------------
        $scope.artworkCategories = {};
        CategoryExtended.getArtworkCategories().success(function(data){
           $scope.artworkCategories = data;
        });
        $scope.professionalCategories = {};
        CategoryExtended.getProfessionalCategories().success(function(data){
            $scope.professionalCategories = data;
        });
    });
