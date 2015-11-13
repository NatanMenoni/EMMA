'use strict';

angular.module('creativehubApp')
    .controller('RegisterSocialController', function ($scope, $translate, $timeout, $location, $state, Auth, Register, Principal) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {};
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        if ($location.search().error) {
        	$scope.errorAccessDenied = $location.search().error == 'access_denied';
        }

        Register.get().$promise.then(
                function(data) {
                    $scope.registerAccount = data;
                    $scope.registerAccount.langKey = $scope.registerAccount.langKey || $translate.use();

                    // for registration, we're only ever linked to a single provider
                    $scope.externalAccountProvider = data.externalAccounts[0].externalProvider;

                    if (Principal.isAuthenticated()) {
                    	$scope.register();
                    }
                },
                function(httpResponse) {
                    // a 404 means that there isn't an ongoing social registration.  this isn't really an error
                    if (httpResponse.status != 404) {
                        $scope.status = 'failed';
                    }
                }
            );

        $scope.register = function () {
    		$scope.registerAccount.password = $scope.confirmPassword = Math.random().toString(36).substr(2);
            $scope.registerAccount.langKey = $translate.use();
            $scope.doNotMatch = null;
            $scope.error = null;
            $scope.errorUserExists = null;
            $scope.errorEmailExists = null;

            Auth.createAccount($scope.registerAccount).then(function () {
                $scope.success = 'OK';

                if (Principal.isAuthenticated()) {
                	$timeout(function (){ $state.go('connections'); }, 1000);
                }
            }).catch(function (response) {
                $scope.success = null;
                if (response.status === 400 && response.data === 'login already in use') {
                    $scope.errorUserExists = 'ERROR';
                } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                    $scope.errorEmailExists = 'ERROR';
                } else {
                    $scope.error = 'ERROR';
                }
            });
        };

    });
