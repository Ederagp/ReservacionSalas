(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaDialogController', ReservaSalaDialogController);

    ReservaSalaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReservaSala', 'Sala', 'User', 'AlertService', 'moment', 'Principal'];

    function ReservaSalaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReservaSala, Sala, User, AlertService, moment, Principal) {
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


        if (vm.reservaSala.id === null) {
            Principal.identity().then(function (account) {
                vm.reservaSala.user = account;
            });
        }

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
            //Se consultan todas las reservas de la sala seleccionada
            ReservaSala.queryBySala({ id: vm.reservaSala.sala.id }, function(result) {
            //ReservaSala.query(function(result) {
                vm.reservaSalas = result;
                console.log(result);
                vm.reservaSalas.forEach(function (value) {
                    console.log(vm.reservaSala.fechaHoraInicial);
                    if (moment(value.fechaHoraInicial).isBetween(vm.reservaSala.fechaHoraInicial, vm.reservaSala.fechaHoraFinal, null, '[)')) {
                        console.log(vm.reservaSala.fechaHoraInicial);
                        if (moment(vm.reservaSala.fechaHoraInicial).isBetween(value.fechaHoraInicial, value.fechaHoraFinal) || moment(vm.reservaSala.fechaHoraFinal).isBetween(value.fechaHoraInicial, value.fechaHoraFinal)) {
                            AlertService.error('Ya existe una reservación dentro del horario en la ' + vm.reservaSala.sala.nombre);
                            vm.isSaving = false;
                        } else {
                            if (moment(value.fechaHoraInicial).isBetween(vm.reservaSala.fechaHoraInicial, vm.reservaSala.fechaHoraFinal) || moment(value.fechaHoraFinal).isBetween(vm.reservaSala.fechaHoraInicial, vm.reservaSala.fechaHoraFinal)) {
                                AlertService.error('No puede reservar sobre un horario existente en la ' + vm.reservaSala.sala.nombre);
                                vm.isSaving = false;
                            }
                        }
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
