(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('setting', {
            parent: 'entity',
            url: '/setting',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chharkoiApp.setting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/setting/settings.html',
                    controller: 'SettingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('setting');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('setting-detail', {
            parent: 'entity',
            url: '/setting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'chharkoiApp.setting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/setting/setting-detail.html',
                    controller: 'SettingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('setting');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Setting', function($stateParams, Setting) {
                    return Setting.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'setting',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('setting-detail.edit', {
            parent: 'setting-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting/setting-dialog.html',
                    controller: 'SettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Setting', function(Setting) {
                            return Setting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('setting.new', {
            parent: 'setting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting/setting-dialog.html',
                    controller: 'SettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('setting', null, { reload: 'setting' });
                }, function() {
                    $state.go('setting');
                });
            }]
        })
        .state('setting.edit', {
            parent: 'setting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting/setting-dialog.html',
                    controller: 'SettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Setting', function(Setting) {
                            return Setting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('setting', null, { reload: 'setting' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('setting.delete', {
            parent: 'setting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting/setting-delete-dialog.html',
                    controller: 'SettingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Setting', function(Setting) {
                            return Setting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('setting', null, { reload: 'setting' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
