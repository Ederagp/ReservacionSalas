(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('SalaDialogController', SalaDialogController);

    SalaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sala', 'ReservaSala'];

    function SalaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sala, ReservaSala) {
        var vm = this;

        vm.sala = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reservasalas = ReservaSala.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sala.id !== null) {
                Sala.update(vm.sala, onSaveSuccess, onSaveError);
            } else {
                Sala.save(vm.sala, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reservaSalasApp:salaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
