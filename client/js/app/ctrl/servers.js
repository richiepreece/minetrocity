angular.module('minetrocity').controller('serversCtrl',
  function ($scope, $location, $http, serversData, alerts) {
    serversData.getServers().then(
      function (servers) {
        $scope.servers = servers;
        $scope.server = servers[0];
      },
      function (err) {
        console.error(err);
        alerts.create('error', err);
      }
    );

    $scope.newServer = function () {
      $location.path('/newServer');
    };

    $scope.deleteServer = function (server) {
      var json = {
        id: server.id
      };

      alerts.create('info', 'Deleting Server...');
      $http.post('/delete_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }

          alerts.create('success', 'Deleted Server!');
          for (var i = 0; i < $scope.servers.length; ++i) {
            if ($scope.servers[i].id === server.id) {
              $scope.servers.splice(i, 1);
            }
          }
          $scope.server = $scope.servers[0];
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    $scope.startServer = function (server) {
      var json = {
        id: server.id
      };
      alerts.create('info', 'Starting Server...');
      $http.post('/start_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }
          alerts.create('success', 'Server Started!');
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    $scope.stopServer = function (server) {
      var json = {
        id: server.id
      };
      alerts.create('info', 'Stopping Server...');
      $http.post('/stop_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }
          alerts.create('success', 'Server Stopped!');
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    $scope.restartServer = function (server) {
      var json = {
        id: server.id
      };
      alerts.create('info', 'Restarting Server...');
      $http.post('/restart_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }
          alerts.create('success', 'Server Restarted!');
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };
  }
);
