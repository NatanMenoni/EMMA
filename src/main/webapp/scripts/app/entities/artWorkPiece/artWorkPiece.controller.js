'use strict';

angular.module('creativehubApp')
    .controller('ArtWorkPieceController', function ($scope, ArtWorkPiece, ParseLinks) {
        $scope.artWorkPieces = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ArtWorkPiece.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.artWorkPieces = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ArtWorkPiece.get({id: id}, function(result) {
                $scope.artWorkPiece = result;
                $('#deleteArtWorkPieceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ArtWorkPiece.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteArtWorkPieceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.artWorkPiece = {
                width: null,
                height: null,
                depth: null,
                commercialState: null,
                price: null,
                id: null
            };
        };
    });
