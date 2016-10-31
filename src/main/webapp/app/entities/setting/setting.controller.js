(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .controller('SettingController', SettingController);

    SettingController.$inject = ['$scope', '$state', 'Setting', 'SettingSearch'];

    function SettingController ($scope, $state, Setting, SettingSearch) {
        var vm = this;
        
        vm.settings = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Setting.query(function(result) {
                vm.settings = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SettingSearch.query({query: vm.searchQuery}, function(result) {
                vm.settings = result;
            });
        }    }
})();
