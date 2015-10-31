'use strict';

angular.module('creativehubApp')
    .controller('TagController', function ($scope, Tag, ParseLinks) {
        $scope.tags = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Tag.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.tags = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
                $('#deleteTagConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Tag.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tag = {
                name: null,
                id: null
            };
        };
    });
