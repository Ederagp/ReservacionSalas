'use strict';

describe('Controller Tests', function() {

    describe('Sala Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSala, MockReservaSala;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSala = jasmine.createSpy('MockSala');
            MockReservaSala = jasmine.createSpy('MockReservaSala');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Sala': MockSala,
                'ReservaSala': MockReservaSala
            };
            createController = function() {
                $injector.get('$controller')("SalaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reservaSalasApp:salaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
