(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaDialogController', ReservaSalaDialogController);

    ReservaSalaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReservaSala', 'Sala', 'User', 'AlertService', 'moment'];

    function ReservaSalaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReservaSala, Sala, User, AlertService, moment) {
        var vm = this;

        vm.reservaSala = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.salas = Sala.query();
        vm.users = User.query();
        vm.min = new Date();
        vm.min.setHours(7);
        vm.min.setMinutes(59);
        vm.max = new Date();
        vm.max.setHours(20);
        vm.max.setMinutes(1);
        

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reservaSala.id !== null) {
                validaReserva();
                if (vm.isSaving) {
                    //ReservaSala.update(vm.reservaSala, onSaveSuccess, onSaveError);
                }
            } else {
                validaReserva();
                if (vm.isSaving) {
                    //ReservaSala.save(vm.reservaSala, onSaveSuccess, onSaveError);
                }
            }
        }

        function validaReserva() {
            vm.reservaSalas = [];
            ReservaSala.query(function(result) {
                vm.reservaSalas = result;
                vm.reservaSalas.forEach(function (value) {
                    if (moment(vm.reservaSala.fechaHoraInicial).isBetween(value.fechaHoraInicial, value.fechaHoraFinal) || moment(vm.reservaSala.fechaHoraFinal).isBetween(value.fechaHoraInicial, value.fechaHoraFinal)) {
                        AlertService.error('Ya existe una reservación en el horario proporcionado.');
                        vm.isSaving = false;
                    }
                });
            });
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
