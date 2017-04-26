var loadUsers = function() {};

angular.
    module('heystk').
    controller(
        'UserAdminController',
        [
            '$scope',
            '$uibModal',
            function($scope, $uibModal) {
                $scope.users = [];
                $scope.newUser = function() {
                    $uibModal.open({
                        animation: true,
                        //template: '<section><h1>Welcome</h1></section>',
                        templateUrl: '/static/html/create-user.html',
                        controller: function($scope) {

                        }
                    });
                };
                loadUsers = function(users) { $scope.$apply(function() { $scope.users = users; }); };
                getUserList();
            }
        ]
    );