(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaController', ReservaSalaController);

    ReservaSalaController.$inject = ['ReservaSala', 'moment', 'calendarConfig'];

    function ReservaSalaController(ReservaSala, moment, calendarConfig) {

        var vm = this;

        vm.reservaSalas = [];
        vm.reservas = [];
        vm.calendarView = 'month';
        vm.viewDate = new Date();
        calendarConfig.dateFormatter = 'moment';
        calendarConfig.allDateFormats.moment.title.week = 'Semana {week} de {year}';
        calendarConfig.i18nStrings.weekNumber = 'Semana {week}';


        loadAll();

        function loadAll() {
            ReservaSala.query(function(result) {
                vm.reservaSalas = result;
                vm.reservaSalas.forEach(function (value) {
                    vm.reservas.push(
                        {
                            title: value.sala.nombre + ', ' + 'Reservó: ' + value.user.firstName + '</br>' + '\"' + value.titulo + '\", ' + value.descripcion,
                            color: calendarConfig.colorTypes.info,
                            startsAt: moment(value.fechaHoraInicial).toDate(),
                            endsAt: moment(value.fechaHoraFinal).toDate(),
                        }
                    );
                });
            });
        }

        vm.eventClicked = function(event) {
            vm.calendarView = 'day';
            vm.viewDate = new Date(event.startsAt);
        };
    }
})();
