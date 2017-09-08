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
        vm.reservaSalas = [];


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

        function save() {
            vm.isSaving = true;
            //Se consultan todas las reservas de la sala seleccionada
            ReservaSala.queryBySala({ id: vm.reservaSala.sala.id }, function (result) {
                vm.reservaSalas = result;
                if (vm.reservaSala.id !== null) {
                    validaReserva();
                    if (vm.isSaving) {
                        ReservaSala.update(vm.reservaSala, onSaveSuccess, onSaveError);
                    }
                } else {
                    validaReserva();
                    if (vm.isSaving) {
                        ReservaSala.save(vm.reservaSala, onSaveSuccess, onSaveError);
                    }
                }
            });
        }

        function validaReserva() {
            console.log(vm.reservaSalas);
            var fromDate = moment(vm.reservaSala.fechaHoraInicial).format();
            var toDate = moment(vm.reservaSala.fechaHoraFinal).format();
            vm.reservaSalas.forEach(function (value) {
                if (vm.isSaving) {
                    if (moment(value.fechaHoraInicial).isBetween(fromDate, toDate)) {
                        if (moment(fromDate).isBetween(value.fechaHoraInicial, value.fechaHoraFinal) || moment(toDate).isBetween(value.fechaHoraInicial, value.fechaHoraFinal)) {
                            AlertService.error('Ya existe una reservación dentro del horario en la ' + vm.reservaSala.sala.nombre);
                            vm.isSaving = false;
                        } else {
                            if (moment(value.fechaHoraInicial).isBetween(fromDate, toDate) || moment(value.fechaHoraFinal).isBetween(fromDate, toDate)) {
                                AlertService.error('No puede reservar sobre un horario existente en la ' + vm.reservaSala.sala.nombre);
                                vm.isSaving = false;
                            }
                        }
                    }
                }
            });
        }

        function onSaveSuccess (result) {
            $scope.$emit('reservaSalasApp:reservaSalaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
            AlertService.info("Se ha enviado la reservación a su Correo Electrónico.");
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
