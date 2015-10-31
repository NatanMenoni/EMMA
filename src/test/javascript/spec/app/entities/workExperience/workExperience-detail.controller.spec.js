'use strict';

describe('WorkExperience Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockWorkExperience;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockWorkExperience = jasmine.createSpy('MockWorkExperience');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'WorkExperience': MockWorkExperience
        };
        createController = function() {
            $injector.get('$controller')("WorkExperienceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:workExperienceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
