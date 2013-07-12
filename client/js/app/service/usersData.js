angular.module('minetrocity').factory('usersData',
  function ($http, $q) {
    var perms;

    function getPermissions() {
      var deferred = $q.defer();
      $http.get('/permissions').then(
        function (resp) {
          if (!resp.data.success) {
            return deferred.reject(resp.data.err);
          }
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
          perms = resp[1];
          for (var i = 0; i < users.length; ++i) {
            users[i].acl = getAcl(users[i].acl);
          }
          deferred.resolve(users);
        },
        deferred.reject
      );
      return deferred.promise;
    }

    function getAcl(acl) {
      var temp = [];
      for (var i = 0; i < perms.length; ++i) {
        temp.push({
          name: perms[i],
          has: contains(perms[i], acl)
        });
      }
      return temp;
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
      getData: getData,
      getAcl: getAcl
    };
  }
);
