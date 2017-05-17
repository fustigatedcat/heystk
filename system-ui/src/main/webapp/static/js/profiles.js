var populateUser = function() {};

angular.
    module('heystk').
    controller(
        'ProfileController',
        [
            '$scope',
            function($scope) {
                $scope.username = '';
                $scope.firstName = '';
                $scope.lastName = '';
                populateUser = function(data) {
                    $scope.username = data.username;
                    $scope.firstName = data.firstName;
                    $scope.lastName = data.lastName;
                    $scope.$apply();
                };
                $scope.passwordsNotMatch = function() {
                    return $scope.password != $scope.reenter && $scope.password != null;
                };
                $scope.canUpdate = function() {
                    return $scope.password == $scope.reenter && $scope.password != null && $scope.password.trim() != '';
                };
                $scope.updateUser = function() {
                    var pw = null;
                    if($scope.password != null && $scope.password.trim() != '') {
                        pw = $scope.password;
                    }
                    updateUser({
                        firstName : $scope.firstName,
                        lastName : $scope.lastName,
                        password : pw
                    });
                };
                getLoggedInUser();
            }
        ]
    );