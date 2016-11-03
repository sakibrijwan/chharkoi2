/**
 * Created by Sakib on 11/3/2016.
 */
(function() {
    'use strict';
    angular
        .module('chharkoiApp')
        .factory('ShopkeeperSelf', ShopkeeperSelf);

    ShopkeeperSelf.$inject = ['$resource', 'DateUtils'];

    function ShopkeeperSelf ($resource, DateUtils) {
        var resourceUrl =  'api/shopkeeper-self';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            }
        });
    }
})();
