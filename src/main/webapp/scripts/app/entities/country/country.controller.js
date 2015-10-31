'use strict';

angular.module('creativehubApp')
    .controller('CountryController', function ($scope, Country, ParseLinks) {
        $scope.countrys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Country.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.countrys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Country.get({id: id}, function(result) {
                $scope.country = result;
                $('#deleteCountryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Country.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCountryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.country = {
                name: null,
                phoneCode: null,
                language: null,
                id: null
            };
        };
    });
