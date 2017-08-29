(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('SalaDetailController', SalaDetailController);

    SalaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sala', 'ReservaSala'];

    function SalaDetailController($scope, $rootScope, $stateParams, previousState, entity, Sala, ReservaSala) {
        var vm = this;

        vm.sala = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reservaSalasApp:salaUpdate', function(event, result) {
            vm.sala = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
