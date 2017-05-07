var loadAgents = function() {};
var agentCreated = function() {};

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
                $scope.canCreateAgent = function() { return createAgent != null; };
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
                getAgentList();
            }
        ]
    );