'use strict';

angular.module('creativehubApp')
    .controller('WorkCollectionController', function ($scope, WorkCollection, ParseLinks) {
        $scope.workCollections = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            WorkCollection.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.workCollections = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            WorkCollection.get({id: id}, function(result) {
                $scope.workCollection = result;
                $('#deleteWorkCollectionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            WorkCollection.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWorkCollectionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.workCollection = {
                description: null,
                date: null,
                coverImagePath: null,
                id: null
            };
        };
    });
