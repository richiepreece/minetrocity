angular.module('minetrocity').controller('serversCtrl',
  function ($scope, $http, $rootScope, serversData, alerts) {
    var serversObj = {};

    function getInfo() {
      serversData.getInfo().then(
        function (data) {
          $scope.servers = data.servers;
          $scope.server = data.servers[0];
          $scope.versions = data.versions;
          $scope.version = data.versions[0];
          serversObj = data.serversObj;
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    }
    getInfo();

    ////////////////////////////////////////////////////////////////////////////////
    //-- Socket IO Stuff ---------------------------------------------------------//
    ////////////////////////////////////////////////////////////////////////////////
    $rootScope.$on('msg', function (e, data) {
      var currServer = serversObj[data.id];
      if (!currServer) return;
      // data.msg = data.msg.replace(/\n/g, '<br>');
      currServer.history.push(data.msg);
    });

    $scope.sendCommand = function (cmd, server) {
      var json = {
        id: server.id,
        cmd: cmd
      };

      alerts.create('info', 'Telling Server: ' + cmd);
      $http.post('/command_server', json).then(
        function (resp) {
          if (!resp.data.success) {
            console.error(resp.data.err);
            return alerts.create('error', resp.data.err);
          }
          alerts.create('success', 'Command Sent!');
          $scope.msg = '';
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    ////////////////////////////////////////////////////////////////////////////////
    //-- Modal Stuff -------------------------------------------------------------//
    ////////////////////////////////////////////////////////////////////////////////
    $scope.modalOpts = { backdropFade: true, dialogFade: true };

    $scope.openModal = function () {
      $scope.showModal = true;
    };

    $scope.closeModal = function () {
      $scope.showModal = false;
    };

    $scope.createServer = function (name, port, version) {
      if (isNaN(port)) {
        return alerts.create('error', 'Invalid Port. Needs to be a number.');
      }

      var json = {
        server_name: name,
        port: port,
        version: version.id,
        version_type: version.type
      };

      alerts.create('info', 'Creating Server...');
      $http.post('/add_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }
          alerts.create('success', 'Created the Server!');
          json.id = d.id;
          json.running = false;
          $scope.servers.push(json);

          $scope.name = '';
          $scope.port = '';
          $scope.showModal = false;
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    ////////////////////////////////////////////////////////////////////////////////
    //-- Server Actions ----------------------------------------------------------//
    ////////////////////////////////////////////////////////////////////////////////
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
          server.running = true;
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
          server.running = false;
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
