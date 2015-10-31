'use strict';

angular.module('creativehubApp')
    .controller('EducationController', function ($scope, Education, ParseLinks) {
        $scope.educations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Education.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.educations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Education.get({id: id}, function(result) {
                $scope.education = result;
                $('#deleteEducationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Education.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEducationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.education = {
                institutionName: null,
                startingYear: null,
                finishingYear: null,
                degree: null,
                degreeOtherDescription: null,
                fieldOfStudy: null,
                careerName: null,
                description: null,
                id: null
            };
        };
    });
