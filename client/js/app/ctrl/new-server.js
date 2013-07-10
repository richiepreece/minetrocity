angular.module('minetrocity').controller('newServerCtrl',
  function ($scope, $http, $location, serversData) {
    serversData.getVersions().then(
      function (versions) {
        $scope.versions = versions;
        $scope.version = versions[0];
      },
      function (err) {
        console.error(err);
      }
    );

    $scope.saveServer = function (name, port, version) {
      var json = {
        server_name: name,
        port: port,
        version: version.id,
        version_type: version.type
      };

      $http.post('/add_server', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return $scope.msg = d.err;
          }
          $location.path('/servers');
        },
        function (err) {
          console.error(err);
          $scope.msg = err.data;
        }
      );
    };

    $scope.back = function () {
      $location.path('/servers');
    };
  }
);
