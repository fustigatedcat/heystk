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
                getLoggedInUser();
            }
        ]
    );