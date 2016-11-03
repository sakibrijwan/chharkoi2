(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('OfferDetailController', OfferDetailController);

    OfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Offer', 'Shopkeeper'];

    function OfferDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Offer, Shopkeeper) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('chharkoiApp:offerUpdate', function(event, result) {
            vm.offer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
