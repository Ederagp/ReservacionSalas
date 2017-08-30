(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaDialogController', ReservaSalaDialogController);

    ReservaSalaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReservaSala', 'Sala', 'User'];

    function ReservaSalaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReservaSala, Sala, User) {
        var vm = this;

        vm.reservaSala = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.salas = Sala.query();
        vm.users = User.query();
        

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reservaSala.id !== null) {
                ReservaSala.update(vm.reservaSala, onSaveSuccess, onSaveError);
            } else {
                ReservaSala.save(vm.reservaSala, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reservaSalasApp:reservaSalaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaHoraInicial = false;
        vm.datePickerOpenStatus.fechaHoraFinal = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
