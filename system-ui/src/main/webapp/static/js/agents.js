var loadAgents = function() {};

angular.
    module("heystk").
    controller(
        "AgentsController",
        [
            "$scope",
            function($scope) {
                $scope.agents = [];
                loadAgents = function(agents) { $scope.$apply(function() { $scope.agents = agents; }) };
                getAgentList();
            }
        ]
    );