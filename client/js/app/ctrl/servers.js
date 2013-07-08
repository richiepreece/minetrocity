angular.module('minetrocity').controller('serversCtrl',
  function ($scope, $http) {
    $http.get('/servers').then(
      function (resp) {
        $scope.servers = resp.data;
      },
      function (err) {
        $scope.servers = "Couldn't get server list: " + err.data;
      }
    )
  }
);
