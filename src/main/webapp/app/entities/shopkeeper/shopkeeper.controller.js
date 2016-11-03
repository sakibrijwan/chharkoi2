(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('ShopkeeperController', ShopkeeperController);

    ShopkeeperController.$inject = ['$scope', '$state', 'DataUtils', 'Shopkeeper', 'ShopkeeperSearch'];

    function ShopkeeperController ($scope, $state, DataUtils, Shopkeeper, ShopkeeperSearch) {
        var vm = this;
        
        vm.shopkeepers = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Shopkeeper.query(function(result) {
                vm.shopkeepers = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ShopkeeperSearch.query({query: vm.searchQuery}, function(result) {
                vm.shopkeepers = result;
            });
        }    }
})();
