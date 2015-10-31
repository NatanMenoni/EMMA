'use strict';

angular.module('creativehubApp')
    .controller('WorkExperienceController', function ($scope, WorkExperience, ParseLinks) {
        $scope.workExperiences = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            WorkExperience.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.workExperiences = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            WorkExperience.get({id: id}, function(result) {
                $scope.workExperience = result;
                $('#deleteWorkExperienceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            WorkExperience.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWorkExperienceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.workExperience = {
                startingDate: null,
                finishingDate: null,
                organization: null,
                title: null,
                location: null,
                current: null,
                jobDescription: null,
                id: null
            };
        };
    });
