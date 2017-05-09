var loadAgents = function() {};
var agentCreated = function() {};
var showAgentConfig = function(input) {
    alert(input);
};

angular.
    module('heystk').
    controller(
        'AgentsController',
        [
            '$scope',
            '$uibModal',
            function($scope, $uibModal) {
                $scope.agents = [];
                loadAgents = function(agents) { $scope.$apply(function() { $scope.agents = agents; }) };
                showAgentConfig = function(config) {
                    $uibModal.open({
                        animation: true,
                        templateUrl : getContext() + '/static/html/display-agent-config.html',
                        controller : function($scope) {
                            $scope.config = config;
                        }
                    });
                    $scope.$apply();
                };
                $scope.canCreateAgent = function() { return createAgent != null; };
                $scope.canDeleteAgent = function() { return deleteAgents != null; };
                $scope.createAgent = function() {
                    var modal = $uibModal.open({
                        animation: true,
                        templateUrl : getContext() + '/static/html/create-agent.html',
                        controller: function($scope) {
                            $scope.name = '';
                            $scope.type = 'NONE';
                            $scope.engineUrl = 'http://localhost:8080/engine';
                            $scope.maxCache = 10;
                            $scope.maxDelay = 5;
                            agentCreated = function() {
                                getAgentList();
                                modal.close();
                                $scope.$apply();
                            };
                            $scope.isRecordValid = function() {
                                if($scope.type == 'FILE_READER') {
                                    return $scope.filePath != null && $scope.filePath.trim() != '';
                                }
                                return $scope.name.trim() != '' &&
                                        $scope.engineUrl.trim() != '' &&
                                        $scope.maxCache != 0 &&
                                        $scope.maxDelay != 0;
                            };
                            $scope.create = function() {
                                createAgent({
                                    name : $scope.name.trim(),
                                    type : $scope.type,
                                    filePath : $scope.filePath.trim(),
                                    engineUrl : $scope.engineUrl.trim(),
                                    maxCache : $scope.maxCache,
                                    maxDelay : $scope.maxDelay
                                });
                            };
                            $scope.cancel = function() { modal.close(); };
                        }
                    })
                };
                $scope.deleteAgents = function() {
                    deleteAgents($scope.agents.filter(function(u) { return u.selected; }).map(function(u) { return u.id; }));
                };
                $scope.agentSelected = function() {
                    for(var i in $scope.agents) {
                        if($scope.agents.hasOwnProperty(i)) {
                            if($scope.agents[i].selected) {
                                return true;
                            }
                        }
                    }
                    return false;
                };
                $scope.generateConfig = function(id) { generateConfig(id); };
                getAgentList();
            }
        ]
    );