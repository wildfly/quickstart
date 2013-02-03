// Define the REST resource service, allowing us to interact with it as a high level service
angular.module('membersService', ['ngResource']).
    factory('Members', function($resource){
  return $resource('rest/members:memberId', {});
});