(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shopkeeper', {
            parent: 'entity',
            url: '/shopkeeper',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'chharkoiApp.shopkeeper.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shopkeeper/shopkeepers.html',
                    controller: 'ShopkeeperController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shopkeeper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shopkeeper-self', {
            parent: 'entity',
            url: '/shopkeeper-self',
            data: {
                authorities: ['ROLE_SHOPKEEPER'],
                pageTitle: 'chharkoiApp.shopkeeper.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-self.html',
                    controller: 'ShopkeeperSelfController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: null,
                        address: null,
                        location: null,
                        shopType: null,
                        phone: null,
                        email: null,
                        webAddress: null,
                        shopTin: null,
                        shopLogo: null,
                        shopLogoContentType: null,
                        date: null,
                        id: null
                    };
                },
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shopkeeper');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shopkeeper-detail', {
            parent: 'entity',
            url: '/shopkeeper/{id}',
            data: {
                authorities: ['ROLE_SHOPKEEPER'],
                pageTitle: 'chharkoiApp.shopkeeper.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-detail.html',
                    controller: 'ShopkeeperDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shopkeeper');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Shopkeeper', function($stateParams, Shopkeeper) {
                    return Shopkeeper.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'shopkeeper',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('shopkeeper-detail.edit', {
            parent: 'shopkeeper-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_SHOPKEEPER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-dialog.html',
                    controller: 'ShopkeeperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shopkeeper', function(Shopkeeper) {
                            return Shopkeeper.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shopkeeper.new', {
            parent: 'shopkeeper',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-dialog.html',
                    controller: 'ShopkeeperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                location: null,
                                shopType: null,
                                phone: null,
                                email: null,
                                webAddress: null,
                                shopTin: null,
                                shopLogo: null,
                                shopLogoContentType: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shopkeeper', null, { reload: 'shopkeeper' });
                }, function() {
                    $state.go('shopkeeper');
                });
            }]
        })
        .state('shopkeeper.edit', {
            parent: 'shopkeeper',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-dialog.html',
                    controller: 'ShopkeeperDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shopkeeper', function(Shopkeeper) {
                            return Shopkeeper.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shopkeeper', null, { reload: 'shopkeeper' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shopkeeper.delete', {
            parent: 'shopkeeper',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shopkeeper/shopkeeper-delete-dialog.html',
                    controller: 'ShopkeeperDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Shopkeeper', function(Shopkeeper) {
                            return Shopkeeper.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shopkeeper', null, { reload: 'shopkeeper' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
