'use strict';

describe('Address Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAddress, MockCountry;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAddress = jasmine.createSpy('MockAddress');
        MockCountry = jasmine.createSpy('MockCountry');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Address': MockAddress,
            'Country': MockCountry
        };
        createController = function() {
            $injector.get('$controller')("AddressDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:addressUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
