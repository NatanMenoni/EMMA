'use strict';

angular.module('creativehubApp')
    .controller('ProfileEventController', function ($scope, ProfileEvent, ParseLinks) {
        $scope.profileEvents = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ProfileEvent.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.profileEvents = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ProfileEvent.get({id: id}, function(result) {
                $scope.profileEvent = result;
                $('#deleteProfileEventConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ProfileEvent.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProfileEventConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.profileEvent = {
                type: null,
                name: null,
                date: null,
                description: null,
                location: null,
                venue: null,
                id: null
            };
        };
    });
