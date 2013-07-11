angular.module('minetrocity').controller('serversCtrl',
  function ($scope, $location, $http, serversData) {
    serversData.getServers().then(
      function (servers) {
        $scope.servers = servers;
        $scope.server = servers[0];
      },
      function (err) {
        console.error(err);
      }
    );

    $scope.newServer = function () {
      $location.path('/newServer');
    };

    $scope.bleh = function () {
      alert('not set up yet');
    };

    $scope.deleteServer = function (server) {
      var json = {
        id: server.id
      };

      $http.post('/delete_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            return console.error(d.err);
          }

          for (var i = 0; i < $scope.servers.length; ++i) {
            if ($scope.servers[i].id === server.id) {
              $scope.servers.splice(i, 1);
            }
          }
          $scope.server = $scope.servers[0];
        },
        function (err) {
          console.error(err);
        }
      );
    };

    $scope.startServer = function (server) {
      var json = { id: server.id };
      $http.post('/start_server', json).then(angular.noop, angular.noop);
    };

    $scope.stopServer = function (server) {
      var json = { id: server.id };
      $http.post('/stop_server', json).then(angular.noop, angular.noop);
    };

    $scope.restartServer = function (server) {
      var json = { id: server.id };
      $http.post('/restart_server', json).then(angular.noop, angular.noop);
    };
  }
);
