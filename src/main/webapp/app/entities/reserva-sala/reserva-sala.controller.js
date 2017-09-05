(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaController', ReservaSalaController);

    ReservaSalaController.$inject = ['ReservaSala', 'moment', 'calendarConfig', 'Principal', '$uibModal', '$state'];

    function ReservaSalaController(ReservaSala, moment, calendarConfig, Principal, $uibModal, $state) {

        var vm = this;

        vm.reservaSalas = [];
        vm.reservas = [];
        vm.calendarView = 'month';
        vm.viewDate = new Date();
        calendarConfig.dateFormatter = 'moment';
        calendarConfig.allDateFormats.moment.title.week = 'Semana {week} de {year}';
        calendarConfig.i18nStrings.weekNumber = 'Semana {week}';
        vm.actions = [];
        Principal.identity().then(function (account) {
            account.authorities.forEach(function (valor){
                if (valor === 'ROLE_ADMIN') {
                    vm.actions = [{
                        label: '<i class="glyphicon glyphicon-remove"></i>',
                        onClick: function (args) {
                            $uibModal.open({
                                templateUrl: 'app/entities/reserva-sala/reserva-sala-delete-dialog.html',
                                controller: 'ReservaSalaDeleteController',
                                controllerAs: 'vm',
                                size: 'md',
                                resolve: {
                                    entity: ['ReservaSala', function(ReservaSala) {
                                        return ReservaSala.get({id : args.calendarEvent.idReserva}).$promise;
                                    }]
                                }
                            }).result.then(function() {
                                $state.go('reserva-sala', null, { reload: 'reserva-sala' });
                            });
                        }
                    }];
                }
            })
        });


        loadAll();

        function loadAll() {
            ReservaSala.query(function (result) {
                vm.reservaSalas = result;
                vm.reservaSalas.forEach(function (value) {
                    if (value.estado === 'Reservada') {
                        vm.reservas.push(
                            {
                                title: value.sala.nombre + ', ' + 'Reservó: ' + value.user.firstName + ', \"' + value.titulo + '\", ' + value.descripcion,
                                color: calendarConfig.colorTypes.info,
                                startsAt: moment(value.fechaHoraInicial).toDate(),
                                endsAt: moment(value.fechaHoraFinal).toDate(),
                                actions: vm.actions,
                                idReserva: value.id
                            }
                        );
                    }
                });
            });
        }

        vm.eventClicked = function(event) {
            vm.calendarView = 'day';
            vm.viewDate = new Date(event.startsAt);
        };
    }
})();
