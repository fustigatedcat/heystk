var loadEngineAPIs = function() {};
var engineAPICreated = function() {};

angular.
    module('heystk').
    controller(
        'EngineAPIsController',
        [
            '$scope',
            '$uibModal',
            function($scope, $uibModal) {
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
                $scope.canDeleteEngineAPI = function() { return deleteEngineAPIs != null; };
                $scope.deleteEngineAPIs = function() {
                    deleteEngineAPIs($scope.engineAPIs.filter(function(u) { return u.selected; }).map(function(u) { return u.id; }));
                };
                $scope.createEngineAPI = function() {
                    var modal = $uibModal.open({
                        animation: true,
                        templateUrl : getContext() + '/static/html/create-engine-api.html',
                        controller: function($scope) {
                            $scope.listenHost = 'localhost';
                            $scope.listenPort = 6060;
                            $scope.context = 'engine';
                            $scope.authorization = {expiry: 5};
                            $scope.database = {};
                            $scope.database.host = 'localhost';
                            $scope.database.port = 3306;
                            $scope.database.db = 'heystk';
                            $scope.queue = {amqp: {api: {}}};
                            $scope.queue.amqp.host = 'localhost';
                            $scope.queue.amqp.port = 5672;
                            $scope.queue.amqp.vhost = 'heystk';
                            $scope.queue.amqp.api.exchangeName = 'from-api';
                            $scope.queue.amqp.api.queueName = 'from-api-to-process';
                            $scope.queue.amqp.api.routingKey = 'to-process';
                            engineAPICreated = function() {
                                getEngineAPIList();
                                modal.close();
                                $scope.$apply();
                            };
                            $scope.create = function() {
                                createEngineAPI({
                                    host: $scope.listenHost,
                                    port: $scope.listenPort,
                                    context: $scope.context,
                                    authorization: {
                                        expiry: $scope.authorization.expiry
                                    },
                                    database: {
                                        host: $scope.database.host,
                                        port: $scope.database.port,
                                        db: $scope.database.db
                                    },
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
                getEngineAPIList();
            }
        ]
    );