(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('ReservaSalaController', ReservaSalaController);

    ReservaSalaController.$inject = ['ReservaSala'];

    function ReservaSalaController(ReservaSala) {

        var vm = this;

        vm.reservaSalas = [];

        loadAll();

        function loadAll() {
            ReservaSala.query(function(result) {
                vm.reservaSalas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
