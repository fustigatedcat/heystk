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
                            userCreated = function() {
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
                $scope.deleteUsers = function() {
                    deleteUsers($scope.users.filter(function(u) { return u.selected; }).map(function(u) { return u.id; }));
                };
                $scope.userSelected = function() {
                    for(var i in $scope.users) {
                        if($scope.users.hasOwnProperty(i)) {
                            if($scope.users[i].selected) {
                                return true;
                            }
                        }
                    }
                    return false;
                };
                $scope.canCreateUser = function() { return createUser != null; };
                $scope.canDeleteUsers = function() { return deleteUsers != null; };
                loadUsers = function(users) { $scope.$apply(function() { $scope.users = users; }); };
                getUserList();
            }
        ]
    );