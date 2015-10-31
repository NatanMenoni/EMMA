'use strict';

describe('Profile Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProfile, MockWorkCollection, MockUser, MockProfileEvent, MockWorkExperience, MockEducation, MockAward;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProfile = jasmine.createSpy('MockProfile');
        MockWorkCollection = jasmine.createSpy('MockWorkCollection');
        MockUser = jasmine.createSpy('MockUser');
        MockProfileEvent = jasmine.createSpy('MockProfileEvent');
        MockWorkExperience = jasmine.createSpy('MockWorkExperience');
        MockEducation = jasmine.createSpy('MockEducation');
        MockAward = jasmine.createSpy('MockAward');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Profile': MockProfile,
            'WorkCollection': MockWorkCollection,
            'User': MockUser,
            'ProfileEvent': MockProfileEvent,
            'WorkExperience': MockWorkExperience,
            'Education': MockEducation,
            'Award': MockAward
        };
        createController = function() {
            $injector.get('$controller')("ProfileDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:profileUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
