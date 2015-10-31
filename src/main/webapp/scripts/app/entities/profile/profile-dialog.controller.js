'use strict';

angular.module('creativehubApp').controller('ProfileDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Profile', 'WorkCollection', 'User', 'ProfileEvent', 'WorkExperience', 'Education', 'Award',
        function($scope, $stateParams, $modalInstance, entity, Profile, WorkCollection, User, ProfileEvent, WorkExperience, Education, Award) {

        $scope.profile = entity;
        $scope.workcollections = WorkCollection.query();
        $scope.users = User.query();
        $scope.profileevents = ProfileEvent.query();
        $scope.workexperiences = WorkExperience.query();
        $scope.educations = Education.query();
        $scope.awards = Award.query();
        $scope.load = function(id) {
            Profile.get({id : id}, function(result) {
                $scope.profile = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('creativehubApp:profileUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.profile.id != null) {
                Profile.update($scope.profile, onSaveFinished);
            } else {
                Profile.save($scope.profile, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
