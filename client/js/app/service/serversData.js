angular.module('minetrocity').factory('serversData',
  function ($http, $q) {
    function getServers() {
      var deferred = $q.defer();
      $http.get('/servers').then(
        function (resp) {
          var d = resp.data;
          // if (!d.success) {
          //   return deferred.reject(d.err);
          // }
          var servers = [];
          for (var key in d.servers) {
            servers.push(d.servers[key]);
          }
          deferred.resolve(servers);
        },
        deferred.reject
      );
      return deferred.promise;
    }

    function getVersions() {
      var deferred = $q.defer();
      $http.get('/versions').then(
        function (resp) {
          deferred.resolve(resp.data.versions.versions);
        },
        deferred.reject
      );
      return deferred.promise;
    }

    return {
      getServers: getServers,
      getVersions: getVersions
    };
  }
);
