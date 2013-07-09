angular.module('minetrocity').factory('usersData',
  function ($http, $q) {
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

    return {
      getUsers: getUsers
    };
  }
);
