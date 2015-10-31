'use strict';

describe('WorkCollection Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockWorkCollection, MockWorkPiece;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockWorkCollection = jasmine.createSpy('MockWorkCollection');
        MockWorkPiece = jasmine.createSpy('MockWorkPiece');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'WorkCollection': MockWorkCollection,
            'WorkPiece': MockWorkPiece
        };
        createController = function() {
            $injector.get('$controller')("WorkCollectionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:workCollectionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
