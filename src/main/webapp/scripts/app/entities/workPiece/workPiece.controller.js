'use strict';

angular.module('creativehubApp')
    .controller('WorkPieceController', function ($scope, WorkPiece, ParseLinks) {
        $scope.workPieces = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            WorkPiece.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.workPieces = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            WorkPiece.get({id: id}, function(result) {
                $scope.workPiece = result;
                $('#deleteWorkPieceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            WorkPiece.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWorkPieceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.workPiece = {
                description: null,
                imagePath: null,
                yearOfCreation: null,
                inputDate: null,
                id: null
            };
        };
    });
