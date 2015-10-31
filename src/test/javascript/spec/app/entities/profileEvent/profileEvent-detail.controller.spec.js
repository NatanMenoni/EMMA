'use strict';

describe('ProfileEvent Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProfileEvent;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProfileEvent = jasmine.createSpy('MockProfileEvent');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ProfileEvent': MockProfileEvent
        };
        createController = function() {
            $injector.get('$controller')("ProfileEventDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:profileEventUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
