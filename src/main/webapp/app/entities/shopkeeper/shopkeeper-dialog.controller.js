(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('ShopkeeperDialogController', ShopkeeperDialogController);

    ShopkeeperDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Shopkeeper', 'User'];

    function ShopkeeperDialogController ($timeout, $scope, $stateParams, $uibModalInstance,  DataUtils, entity, Shopkeeper, User) {
        var vm = this;

        vm.shopkeeper = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.shopkeeper.id !== null) {
                Shopkeeper.update(vm.shopkeeper, onSaveSuccess, onSaveError);
            } else {
                Shopkeeper.save(vm.shopkeeper, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('chharkoiApp:shopkeeperUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setShopLogo = function ($file, shopkeeper) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        shopkeeper.shopLogo = base64Data;
                        shopkeeper.shopLogoContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
