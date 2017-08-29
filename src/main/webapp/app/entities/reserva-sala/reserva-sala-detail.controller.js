(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaDetailController', ReservaSalaDetailController);

    ReservaSalaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReservaSala', 'Sala', 'User'];

    function ReservaSalaDetailController($scope, $rootScope, $stateParams, previousState, entity, ReservaSala, Sala, User) {
        var vm = this;

        vm.reservaSala = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reservaSalasApp:reservaSalaUpdate', function(event, result) {
            vm.reservaSala = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
