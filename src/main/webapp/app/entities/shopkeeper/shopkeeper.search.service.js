(function() {
    'use strict';

    angular
        .module('chharkoiApp')
        .factory('ShopkeeperSearch', ShopkeeperSearch);

    ShopkeeperSearch.$inject = ['$resource'];

    function ShopkeeperSearch($resource) {
        var resourceUrl =  'api/_search/shopkeepers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
