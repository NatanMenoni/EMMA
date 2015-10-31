'use strict';

angular.module('creativehubApp')
    .controller('DocumentController', function ($scope, Document, ParseLinks) {
        $scope.documents = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Document.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.documents = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Document.get({id: id}, function(result) {
                $scope.document = result;
                $('#deleteDocumentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Document.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDocumentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.document = {
                documentType: null,
                documentNumber: null,
                documentImagePath: null,
                id: null
            };
        };
    });
