(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaDeleteController',ReservaSalaDeleteController);

    ReservaSalaDeleteController.$inject = ['$uibModalInstance', 'entity', 'ReservaSala'];

    function ReservaSalaDeleteController($uibModalInstance, entity, ReservaSala) {
        var vm = this;

        vm.reservaSala = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ReservaSala.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
