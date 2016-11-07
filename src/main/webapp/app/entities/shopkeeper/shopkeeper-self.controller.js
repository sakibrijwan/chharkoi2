/**
 * Created by Sakib on 11/3/2016.
 */
(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('ShopkeeperSelfController', ShopkeeperSelfController);

    ShopkeeperSelfController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'Shopkeeper', 'User', 'ShopkeeperSelf'];

    function ShopkeeperSelfController ($timeout, $scope, $stateParams,  DataUtils, entity, Shopkeeper, User, ShopkeeperSelf) {
        var vm = this;

        vm.shopkeeper = ShopkeeperSelf.get();
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.result1 = '';
        vm.options1 = null;
        vm.details1 = '';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {

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
