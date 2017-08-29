(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaController', ReservaSalaController);

    ReservaSalaController.$inject = ['ReservaSala', 'moment', 'calendarConfig'];

    function ReservaSalaController(ReservaSala, moment, calendarConfig) {

        var vm = this;

        vm.reservaSalas = [];
        vm.calendarView = 'month';
        vm.viewDate = new Date();
        calendarConfig.dateFormatter = 'moment';
        calendarConfig.allDateFormats.moment.title.week = 'Semana {week} de {year}';
        calendarConfig.i18nStrings.weekNumber = 'Semana {week}';

        vm.reservas = [
            {
              title: 'Junta Importante',
              color: calendarConfig.colorTypes.warning,
              startsAt: moment().startOf('week').subtract(2, 'days').add(8, 'hours').toDate(),
              endsAt: moment().startOf('week').add(1, 'week').add(9, 'hours').toDate(),
              draggable: true,
              resizable: true
            }, {
              title: '<i class="glyphicon glyphicon-asterisk"></i> <span>Otra Reserva</span>',
              color: calendarConfig.colorTypes.info,
              startsAt: moment().subtract(1, 'day').toDate(),
              endsAt: moment().add(5, 'days').toDate(),
              draggable: true,
              resizable: true
            }, {
              title: 'Proyección de pelicula...',
              color: calendarConfig.colorTypes.success,
              startsAt: moment().startOf('day').add(7, 'hours').toDate(),
              endsAt: moment().startOf('day').add(19, 'hours').toDate(),
              draggable: true,
              resizable: true
            }
        ];

        loadAll();

        function loadAll() {
            ReservaSala.query(function(result) {
                vm.reservaSalas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
