function MembersCtrl($scope, $http) {

    
	var defaultNewMember = {
	    name: "",
	    email: "",
	    phoneNumber: ""
	};
	
	updateList = function() {
		$http.get('rest/members').success(function(data) { 
			$scope.members = data;
		});
	};
	
	updateList();
	
	$scope.orderProp = 'name';
	
	$scope.newMember = defaultNewMember;
	
	$scope.reset = function() {
		//clear input fields
        $scope.newMember = {};
	};
	
	$scope.register = function() {
		$scope.successMessages = '';
		$scope.errorMessages = '';
		$http.post('rest/members', $scope.newMember).success(function(data) {
            
            //mark success on the registration form
            $scope.successMessages = [ 'Member Registered' ];
            
            // Update the list of members
            updateList();
            
            //Clear the form
            $scope.reset();
		}).error(function(data, status) {
			if ((status == 409) || (status == 400)) {
                // TODO bind errors messages
            } else {
                //console.log("error - unknown server issue");
            	$scope.errorMessages = ['Unknown  server error'];
            }
		});
	};
}