(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .factory('SettingSearch', SettingSearch);

    SettingSearch.$inject = ['$resource'];

    function SettingSearch($resource) {
        var resourceUrl =  'api/_search/settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
