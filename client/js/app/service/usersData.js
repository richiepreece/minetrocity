angular.module('minetrocity').factory('usersData',
  function ($http, $q) {
    function getPermissions() {
      var deferred = $q.defer();
      $http.get('/permissions').then(
        function (resp) {
          deferred.resolve(resp.data.permissions.permissions);
        },
        deferred.reject
      );
      return deferred.promise;
    }

    function getUsers() {
      var deferred = $q.defer();
      $http.get('/users').then(
        function (resp) {
          var d = resp.data;
          // if (!d.success) {
          //   return deferred.reject(d.err);
          // }
          var users = [];
          for (var key in d.users) {
            users.push(d.users[key]);
          }
          deferred.resolve(users);
        },
        deferred.reject
      );
      return deferred.promise;
    }

    function getData() {
      var deferred = $q.defer();
      $q.all([
        getUsers(),
        getPermissions()
      ]).then(
        function (resp) {
          var users = resp[0];
          var perms = resp[1];
          for (var i = 0; i < users.length; ++i) {
            var acl = users[i].acl;
            users[i].acl = [];
            for (var j = 0; j < perms.length; ++j) {
              users[i].acl.push({ name: perms[j], has: contains(perms[j], acl) });
            }
          }
          deferred.resolve(users);
        },
        deferred.reject
      );
      return deferred.promise;
    }


    function contains(perm, acl) {
      for (var i = 0; i < acl.length; ++i) {
        if (acl[i] === perm) {
          return true;
        }
      }
      return false;
    }

    return {
      getData: getData
    };
  }
);
