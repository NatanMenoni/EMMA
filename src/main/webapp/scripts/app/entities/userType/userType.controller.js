'use strict';

angular.module('creativehubApp')
    .controller('UserTypeController', function ($scope, UserType, ParseLinks) {
        $scope.userTypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            UserType.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userTypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            UserType.get({id: id}, function(result) {
                $scope.userType = result;
                $('#deleteUserTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userType = {
                name: null,
                id: null
            };
        };
    });
