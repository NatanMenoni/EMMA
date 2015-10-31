'use strict';

angular.module('creativehubApp')
    .controller('ProfileController', function ($scope, Profile, ParseLinks) {
        $scope.profiles = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Profile.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.profiles = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Profile.get({id: id}, function(result) {
                $scope.profile = result;
                $('#deleteProfileConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Profile.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProfileConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.profile = {
                aboutMe: null,
                facebookLink: null,
                linkedinLink: null,
                profilePicturePath: null,
                coverPicturePath: null,
                promotionPicturePath: null,
                id: null
            };
        };
    });
