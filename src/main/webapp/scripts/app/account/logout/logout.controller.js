'use strict';

angular.module('creativehubApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
