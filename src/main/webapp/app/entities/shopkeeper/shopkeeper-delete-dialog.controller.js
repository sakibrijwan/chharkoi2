(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('ShopkeeperDeleteController',ShopkeeperDeleteController);

    ShopkeeperDeleteController.$inject = ['$uibModalInstance', 'entity', 'Shopkeeper'];

    function ShopkeeperDeleteController($uibModalInstance, entity, Shopkeeper) {
        var vm = this;

        vm.shopkeeper = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Shopkeeper.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
