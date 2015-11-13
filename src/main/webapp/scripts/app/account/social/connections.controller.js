'use strict';

angular.module('creativehubApp')
    .controller('ConnectionsController', function ($scope, SocialConnection, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.success = null;
        $scope.error = null;
        $scope.connections = SocialConnection.get();

        $scope.delete = function (connection) {
        	SocialConnection.delete({providerId: encodeURIComponent(connection.externalProvider)},
                function () {
                    $scope.error = null;
                    $scope.success = 'OK';
                    $scope.connections = SocialConnection.get();
                },
                function () {
                    $scope.success = null;
                    $scope.error = 'ERROR';
                });
        };
    });
