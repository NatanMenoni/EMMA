'use strict';

describe('Category Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCategory, MockWorkPiece;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCategory = jasmine.createSpy('MockCategory');
        MockWorkPiece = jasmine.createSpy('MockWorkPiece');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Category': MockCategory,
            'WorkPiece': MockWorkPiece
        };
        createController = function() {
            $injector.get('$controller')("CategoryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:categoryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
