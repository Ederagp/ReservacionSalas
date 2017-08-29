(function() {
    'use strict';

    angular
        .module('reservaSalasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reserva-sala', {
            parent: 'entity',
            url: '/reserva-sala',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reservaSalasApp.reservaSala.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reserva-sala/reserva-salas.html',
                    controller: 'ReservaSalaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservaSala');
                    $translatePartialLoader.addPart('estado');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reserva-sala-detail', {
            parent: 'reserva-sala',
            url: '/reserva-sala/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reservaSalasApp.reservaSala.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reserva-sala/reserva-sala-detail.html',
                    controller: 'ReservaSalaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservaSala');
                    $translatePartialLoader.addPart('estado');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReservaSala', function($stateParams, ReservaSala) {
                    return ReservaSala.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reserva-sala',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reserva-sala-detail.edit', {
            parent: 'reserva-sala-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserva-sala/reserva-sala-dialog.html',
                    controller: 'ReservaSalaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReservaSala', function(ReservaSala) {
                            return ReservaSala.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reserva-sala.new', {
            parent: 'reserva-sala',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserva-sala/reserva-sala-dialog.html',
                    controller: 'ReservaSalaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titulo: null,
                                fechaHoraInicial: null,
                                fechaHoraFinal: null,
                                descripcion: null,
                                estado: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reserva-sala', null, { reload: 'reserva-sala' });
                }, function() {
                    $state.go('reserva-sala');
                });
            }]
        })
        .state('reserva-sala.edit', {
            parent: 'reserva-sala',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserva-sala/reserva-sala-dialog.html',
                    controller: 'ReservaSalaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReservaSala', function(ReservaSala) {
                            return ReservaSala.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reserva-sala', null, { reload: 'reserva-sala' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reserva-sala.delete', {
            parent: 'reserva-sala',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserva-sala/reserva-sala-delete-dialog.html',
                    controller: 'ReservaSalaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReservaSala', function(ReservaSala) {
                            return ReservaSala.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reserva-sala', null, { reload: 'reserva-sala' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
