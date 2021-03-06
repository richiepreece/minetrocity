/* global angular */
angular.module('minetrocity').factory('serversData',
  function ($http, $q) {
    'use strict';

    function getServers() {
      var deferred = $q.defer();
      $http.get('/servers').then(
        function (resp) {
          var d = resp.data;
          var servers = [];
          for (var key in d.servers) {
            var currServer = d.servers[key];
            servers.push(currServer);
            currServer.history = currServer.history || [];
            // for (var i = 0; i < currServer.history.length; ++i) {
            //   currServer.history[i] = currServer.history[i].replace(/\n/g, '<br>');
            // }
          }
          deferred.resolve({
            arr: servers,
            obj: d.servers
          });
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

    function getInfo() {
      var deferred = $q.defer();
      $q.all([
        getServers(),
        getVersions()
      ]).then(
        function (resp) {
          deferred.resolve({
            serversObj: resp[0].obj,
            servers: resp[0].arr,
            versions: resp[1]
          });
        },
        deferred.reject
      );
      return deferred.promise;
    }

    return {
      getServers: getServers,
      getVersions: getVersions,
      getInfo: getInfo
    };
  }
);
