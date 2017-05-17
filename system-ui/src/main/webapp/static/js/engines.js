var loadEngines = function() {};
var engineCreated = function() {};
var showEngineConfig = function() {};

angular.
    module('heystk').
    controller(
        'EnginesController',
        [
            '$scope',
            '$uibModal',
            function($scope, $uibModal) {
                $scope.engines = [];
                loadEngines = function(apis) {
                    $scope.engines = apis;
                    $scope.$apply();
                };
                $scope.engineSelected = function() {
                    for(var i in $scope.engines) {
                        if($scope.engines.hasOwnProperty(i)) {
                            if($scope.engines[i].selected) {
                                return true;
                            }
                        }
                    }
                    return false;
                };
                $scope.canCreateEngine = function() { return createEngine != null; };
                $scope.canDeleteEngine = function() { return deleteEngines != null; };
                $scope.deleteEngines = function() {
                    deleteEngines($scope.engines.filter(function(u) { return u.selected; }).map(function(u) { return u.id; }));
                };
                $scope.generateConfig = function(id) { generateConfig(id) ; };
                showEngineConfig = function(config) {
                    $uibModal.open({
                        animation: true,
                        templateUrl : getContext() + '/static/html/display-engine-config.html',
                        controller : function($scope) {
                            $scope.config = config;
                        }
                    });
                    $scope.$apply();
                };
                $scope.createEngine = function() {
                    var modal = $uibModal.open({
                        animation: true,
                        templateUrl : getContext() + '/static/html/create-engine.html',
                        controller: function($scope) {
                            $scope.name = 'New Engine';
                            $scope.queue = {
                                amqp: {
                                    host: 'localhost',
                                    port: 5672,
                                    vhost: 'heystk',
                                    api: {
                                        exchangeName: 'from-api',
                                        queueName: 'from-api-to-process',
                                        routingKey: 'to-process'
                                    }
                                }
                            };
                            engineCreated = function() {
                                getEngineList();
                                modal.close();
                                $scope.$apply();
                            };
                            $scope.create = function() {
                                createEngine({
                                    name: $scope.name,
                                    queue: {
                                        amqp: {
                                            host: $scope.queue.amqp.host,
                                            port: $scope.queue.amqp.port,
                                            vhost: $scope.queue.amqp.vhost,
                                            api: {
                                                exchangeName: $scope.queue.amqp.api.exchangeName,
                                                queueName: $scope.queue.amqp.api.queueName,
                                                routingKey: $scope.queue.amqp.api.routingKey
                                            }
                                        }
                                    }
                                });
                            };
                            $scope.cancel = function() { modal.close(); };
                        }
                    });
                };
                getEngineList();
            }
        ]
    );