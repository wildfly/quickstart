angular.module('membercatServices', ['ngResource']).
    factory('Member', function($resource){
  return $resource('rest//:phoneId.json', {}, {
    query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
  });
});