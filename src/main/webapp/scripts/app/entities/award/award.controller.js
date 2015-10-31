'use strict';

angular.module('creativehubApp')
    .controller('AwardController', function ($scope, Award, ParseLinks) {
        $scope.awards = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Award.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.awards = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Award.get({id: id}, function(result) {
                $scope.award = result;
                $('#deleteAwardConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Award.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAwardConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.award = {
                year: null,
                name: null,
                issuer: null,
                description: null,
                id: null
            };
        };
    });
