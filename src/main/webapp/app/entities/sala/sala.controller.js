(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .controller('SalaController', SalaController);

    SalaController.$inject = ['Sala'];

    function SalaController(Sala) {

        var vm = this;

        vm.salas = [];

        loadAll();

        function loadAll() {
            Sala.query(function(result) {
                vm.salas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
