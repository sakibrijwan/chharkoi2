(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('ShopkeeperDetailController', ShopkeeperDetailController);

    ShopkeeperDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Shopkeeper', 'User'];

    function ShopkeeperDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Shopkeeper, User) {
        var vm = this;

        vm.shopkeeper = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('chharkoiApp:shopkeeperUpdate', function(event, result) {
            vm.shopkeeper = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
