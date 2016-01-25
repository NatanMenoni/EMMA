/**
 * Created by Natan on 05/11/2015.
 */
'use strict';

angular.module('creativehubApp')
    .factory('CategoryExtended', ['$http', function CategoryExtendedFactory($http){
       return {
            getArtworkCategories: function(){
                return $http({method: 'GET', url: 'api/categories/artwork'});
            },
            getProfessionalCategories: function(){
                return $http({method: 'GET', url: 'api/categories/professional'});
            },
           getEventCategories: function(){
               return $http({method: 'GET', url: 'api/categories/events'});
           }
       };
    }]);
