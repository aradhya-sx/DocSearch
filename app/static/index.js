var app = angular.module("PractoProject2", ["ngRoute"]);

app.config(function($routeProvider){
	$routeProvider
	.when('/allSpecializations', {
		templateUrl: 'templates/allSpecialities.html',
		controller: 'specializationsController'
	})
  .when('/allDoctor', {
    templateUrl: 'templates/allDoctor.html',
    controller: 'DoctorController'
  }) 
  .when('/allClinics', {
    templateUrl: "templates/allClinics.html",
    controller: "clinicsController"
  })
  .when('/addClinic', {
    templateUrl: "templates/addClinic.html",
    controller: "addClinicController"
  })
  .when('/addDoctor', {
    templateUrl: "templates/addDoctor.html",
    controller: "addDoctorController"
  })
  .when('/editDoctor', {
    templateUrl: "templates/editDoctor.html",
    controller: "editDoctorController"
  })
  .when('/editClinic', {
    templateUrl: "templates/editClinic.html",
    controller: "editClinicController"
  })
  .when('/addSpecialization', {
    templateUrl: "templates/addSpecialization.html",
    controller: "addSpecializationController"
  })

});
 
app.controller("DoctorController", function($scope, $rootScope, $http, $location) {
	$rootScope.Tab = 1;
	var req = {
      method: 'GET',
      url: 'http://localhost:5000/allDoctors',
      headers: {'Content-Type': 'application/json'}
  };
  $http(req)
  .success(function(data){
      if(data.returnCode == "SUCCESS"){
      	   $scope.doctors = data.data;
    	}
    	else{
    		  console.log("error")
    	}
  })
  .error(function(){
  	console.log("Error connecting to the server");
  });

  $scope.addDoctor = function() {
      $rootScope.spec = {}
      var req = {
          method: 'GET',
          url: 'http://localhost:5000/allSpecializations',
          headers: {'Content-Type': 'application/json'}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS"){
              $rootScope.spec = data.data;
          }
      })
      $rootScope.dClinic = {}
      var req = {
          method: 'GET',
          url: 'http://localhost:5000/getClinic',
          headers: {'Content-Type': 'application/json'}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS"){
             $rootScope.dClinic = data.data;
          }
      })
      $location.path('/addDoctor');
  }
  $scope.editDoctor = function(doctor){
      $rootScope.DocAll = {}
      var req = {
          method: 'GET',
          url: 'http://localhost:5000/getDoctorDetails/' + doctor.id,
          headers: {'Content-Type': 'application/json'}
      };
      $http(req).
      success(function(data){
          if(data.returnCode == "SUCCESS"){
             $rootScope.DocAll = data.data;
          }
      })
      $rootScope.specEdit = {}
      var req = {
          method: 'GET',
          url: 'http://localhost:5000/allSpecializationsForDoctor',
          headers: {'Content-Type': 'application/json'}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS"){
              $rootScope.specEdit = data.data;
          }
      })
      $rootScope.dClinicEdit = {}
      var req = {
          method: 'GET',
          url: 'http://localhost:5000/getClinicForDoctor',
          headers: {'Content-Type': 'application/json'}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS"){
             $rootScope.dClinicEdit = data.data;
          }
      })
      $rootScope.doctorEdit = doctor;
      $location.path('/editDoctor');
  }
  $scope.deleteDoctor = function(doctor){
      var req = {
          method: 'POST',
          url: 'http://localhost:5000/deleteDoctor',
          headers: {'Content-Type': 'application/json'},
          data : { id: doctor.id }
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS"){
              alert("Deleted");
              $location.path('/');
          }
          else{
              console.log("error")
          }
      })
      .error(function(){
          console.log("Error connecting to the server");
      });
    }
});

app.controller("tabsController", function($scope, $rootScope, $location){
  $rootScope.Tab = 1;
  $scope.tabClicked = function(tab){
      if(tab == 1){
        $rootScope.Tab = 1;
        $location.path('/allDoctor');
      }
      else if(tab == 2){
        $rootScope.Tab = 2;
        $location.path('/allClinics');
      }
      else{
        $rootScope.Tab = 3;
        $location.path('/allSpecializations');
      }
  }
});

app.controller("clinicsController", function($scope, $http, $rootScope, $location){
    $rootScope.Tab = 2;
  	$scope.clinics = [];
    var req = {
        method: 'GET',
        url: 'http://localhost:5000/allClinics',
        headers: {'Content-Type': 'application/json'}
    };
    $http(req)
    .success(function(data){
        if(data.returnCode == "SUCCESS"){
            $scope.clinics = data.data;
        }
        else{
        	 console.log("error")
        }
    })
    .error(function(){
    	console.log("Error connecting to the server");
    });

    $scope.addClinic = function() {
        $location.path('/addClinic');
    }
    $scope.editClinic = function(clinic){
        console.log(clinic);
        $rootScope.clinicEdit = clinic;
        $location.path('/editClinic');
    }
    $scope.deleteClinic = function(clinic, index){
        var req = {
            method: 'POST',
            url: 'http://localhost:5000/deleteClinic',
            headers: {'Content-Type': 'application/json'},
            data : {id: clinic.id}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS") {
              //alert("Deleted");
              $scope.clinics.splice(index, 1)
              $location.path('/allClinics');
          }
        else{
              console.log("error")
        }
      })
      .error(function(){
          console.log("Error connecting to the server");
      });
    }
});

app.controller("specializationsController", function($scope, $http, $rootScope, $location){
  	$rootScope.Tab = 3;
  	var req = {
          	 method: 'GET',
             url: 'http://localhost:5000/allSpecializations',
             headers: {'Content-Type': 'application/json'}
          };
    $http(req).
    success(function(data){
        if(data.returnCode == "SUCCESS"){
        	   $scope.specializations = data.data;
        }
        else{
        	   console.log("Error in retrieving specializations");
        }
    })
    .error(function(){
  		  console.log("Error Connecting to the backend");
    });

    $scope.addSpecialization = function() {
      $location.path('/addSpecialization');
    }

    $scope.deleteSpecialization = function(specialization){
      var req = {
          method: 'POST',
          url: 'http://localhost:5000/deleteSpecialization',
          headers: {'Content-Type': 'application/json'},
          data : {name: specialization.name}
      };
      $http(req)
      .success(function(data){
          if(data.returnCode == "SUCCESS") {
              alert("deleted");
              $location.path('/');
          }
        else{
            console.log("error");
        }
      })
      .error(function(){
          console.log("Error connecting to the server");
      });
    }
});

app.controller("addSpecializationController", function($scope, $http, $location){
    $scope.addSpecialization = function(){
        var req = {
            method: 'POST',
            url: 'http://localhost:5000/addSpecialization',
            headers: {'Content-Type': 'application/json'},
            data: {name: $scope.name}
        };
        $http(req)
        .success(function(data){
            $location.path('/allSpecializations');
        })
        .error(function(){
            console.log("Error Connecting to the server");
      })
    }
});

app.controller("addDoctorController", function($scope, $http, $location){
    $scope.speclist = [];
    $scope.cliniclist = [];
    $scope.addDoctor = function(){
        console.log($scope.speclist);
        var req = {
            method: 'POST',
            url: 'http://localhost:5000/addDoctor',
            headers: {'Content-Type': 'application/json'},
            data:{  name : $scope.name, 
                    experience : $scope.experience, 
                    email : $scope.email,
                    recommendations : $scope.recommendations, 
                    education : $scope.education, 
                    speclist : $scope.speclist,
                    cliniclist : $scope.cliniclist
                }
        };
        $http(req)
        .success(function(data){
            $location.path('/allDoctor');
        })
        .error(function(){
            console.log("Error Connecting to the server");
        })
    } 
});

app.controller("editDoctorController", function($scope, $http, $rootScope, $location){

    var doctorObject = $rootScope.doctorEdit;
    $scope.name = doctorObject.name;
    $scope.education = doctorObject.education
    $scope.email = doctorObject.email;
    $scope.experience = doctorObject.experience;
    $scope.recommendations = doctorObject.recommendations;
    $scope.speclist = doctorObject.specList;
    $scope.cliniclist = doctorObject.clinicList;
    console.log($scope.speclist);
    console.log($scope.cliniclist);
    $scope.editDoctor = function(){
        var req = {
            method: 'POST',
            url: 'http://localhost:5000/editDoctor',
            headers: {'Content-Type': 'application/json'},
            data: { id : doctorObject.id, 
                name : $scope.name, 
                experience : $scope.experience, 
                email : $scope.email,
                recommendations : $scope.recommendations, 
                education : $scope.education,
                speclist : $scope.speclist,
                cliniclist : $scope.cliniclist
              }
        };
        $http(req)
        .success(function(data){
            $location.path('/allDoctor');
        })
        .error(function(){
            console.log("Error Connecting to the Server");
        })
    }
});


app.controller("addClinicController", function($scope, $http, $location){
    $scope.addClinic = function(){
        var req = {
            method: 'POST',
            url: 'http://localhost:5000/addClinic',
            headers: {'Content-Type': 'application/json'},
            data: { name : $scope.name, 
                    city_name : $scope.city_name, 
                    contact_no : $scope.contact_no, 
                    area : $scope.area, 
                    address : $scope.address
                  }
        };
        $http(req)
        .success(function(data){
              $location.path('/allClinics');
        })
        .error(function(){
            console.log("Error Connecting to the server");
        })
    }
});

app.controller("editClinicController", function($scope, $http, $rootScope, $location){
    
    var clinic = $rootScope.clinicEdit;
    $scope.name = clinic.name;
    $scope.city_name = clinic.city_name;
    $scope.area = clinic.area;
    $scope.address = clinic.address;
    $scope.contact_no = clinic.contact_no;

    $scope.editClinic = function(){
      var req = {
          method: 'POST',
          url: 'http://localhost:5000/editClinic',
          headers: {'Content-Type': 'application/json'},
          data: { id:clinic.id, 
                  name : $scope.name, 
                  city_name : $scope.city_name, 
                  contact_no : $scope.contact_no, 
                  area : $scope.area, 
                  address : $scope.address
          }
      };
      $http(req)
      .success(function(data){
          $location.path('/allClinics');
      })
      .error(function(){
          console.log("Error Connecting to the Server");
      })
    }
  });


