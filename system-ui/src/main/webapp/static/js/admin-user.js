var loadUsers = function() {};
var checkUserNameExistsCb = function() {};
var userCreated = function() {};

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
                    var modal = $uibModal.open({
                        animation: true,
                        templateUrl: getContext() + '/static/html/create-user.html',
                        controller: function($scope) {
                            $scope.usernameExists = false;
                            checkUserNameExistsCb = function(rtn) {
                                $scope.usernameExists = rtn;
                                $scope.$apply();
                            };
                            userCreated = function(rtn) {
                                getUserList();
                                modal.close();
                                $scope.$spply();
                            };
                            $scope.username = '';
                            $scope.firstName = '';
                            $scope.lastName = '';
                            $scope.password = '';
                            $scope.reenter = '';
                            $scope.passwordsNotMatch = function() {
                                return $scope.password != $scope.reenter && $scope.password != '' && $scope.reenter != '';
                            };
                            $scope.isRecordValid = function() {
                                return $scope.username.length > 3 &&
                                    $scope.firstName.length >= 3 &&
                                    $scope.lastName.length >= 3 &&
                                    !$scope.usernameExists &&
                                    $scope.password == $scope.reenter && $scope.password.length > 6;
                            };
                            $scope.usernameChanged = function() {
                                if($scope.username.length > 3) {
                                    checkUserNameExists($scope.username);
                                }
                            };
                            $scope.cancel = function() {
                                modal.close();
                            };
                            $scope.create = function() {
                                createUser({
                                    username: $scope.username,
                                    firstName : $scope.firstName,
                                    lastName : $scope.lastName,
                                    password : $scope.password
                                })
                            };
                        }
                    });
                };
                loadUsers = function(users) { $scope.$apply(function() { $scope.users = users; }); };
                getUserList();
            }
        ]
    );