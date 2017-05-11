var loadEngineAPIs = function() {};

angular.
    module('heystk').
    controller(
        'EngineAPIsController',
        [
            '$scope',
            function($scope) {
                $scope.engineAPIs = [];
                loadEngineAPIs = function(apis) {
                    $scope.engineAPIs = apis;
                    $scope.$apply();
                };
                $scope.engineAPISelected = function() {
                    for(var i in $scope.engineAPIs) {
                        if($scope.engineAPIs.hasOwnProperty(i)) {
                            if($scope.engineAPIs[i].selected) {
                                return true;
                            }
                        }
                    }
                    return false;
                };
                $scope.canCreateEngineAPI = function() { return getEngineAPIList != null; };
                $scope.canDeleteEngineAPI = function() { return getDeleteAPIList != null; };
                $scope.deleteEngineAPIs = function() {
                    deleteEngineAPIs($scope.engineAPIs.filter(function(u) { return u.selected; }).map(function(u) { return u.id; }));
                };
                getEngineAPIList();
            }
        ]
    );