'use strict';

angular.module('creativehubApp')
    .controller('TelephoneController', function ($scope, Telephone, ParseLinks) {
        $scope.telephones = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Telephone.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.telephones = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Telephone.get({id: id}, function(result) {
                $scope.telephone = result;
                $('#deleteTelephoneConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Telephone.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTelephoneConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.telephone = {
                areaCode: null,
                number: null,
                id: null
            };
        };
    });
