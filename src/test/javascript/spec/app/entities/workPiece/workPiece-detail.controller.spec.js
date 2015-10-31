'use strict';

describe('WorkPiece Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockWorkPiece, MockWorkCollection, MockTag, MockUser, MockCategory;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockWorkPiece = jasmine.createSpy('MockWorkPiece');
        MockWorkCollection = jasmine.createSpy('MockWorkCollection');
        MockTag = jasmine.createSpy('MockTag');
        MockUser = jasmine.createSpy('MockUser');
        MockCategory = jasmine.createSpy('MockCategory');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'WorkPiece': MockWorkPiece,
            'WorkCollection': MockWorkCollection,
            'Tag': MockTag,
            'User': MockUser,
            'Category': MockCategory
        };
        createController = function() {
            $injector.get('$controller')("WorkPieceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'creativehubApp:workPieceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
