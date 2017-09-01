(function() {
    'use strict';
    angular
        .module('reservaSalasApp')
        .factory('ReservaSala', ReservaSala);

    ReservaSala.$inject = ['$resource', 'DateUtils'];

    function ReservaSala ($resource, DateUtils) {
        var resourceUrl =  'api/reserva-salas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryBySala': { method: 'GET', isArray: true, url: 'api/reserva-salas/sala/:id'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaHoraInicial = DateUtils.convertDateTimeFromServer(data.fechaHoraInicial);
                        data.fechaHoraFinal = DateUtils.convertDateTimeFromServer(data.fechaHoraFinal);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaHoraInicial = DateUtils.convertDateTimeFromServer(copy.fechaHoraInicial);
                    copy.fechaHoraFinal = DateUtils.convertDateTimeFromServer(copy.fechaHoraFinal);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaHoraInicial = DateUtils.convertDateTimeFromServer(copy.fechaHoraInicial);
                    copy.fechaHoraFinal = DateUtils.convertDateTimeFromServer(copy.fechaHoraFinal);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
