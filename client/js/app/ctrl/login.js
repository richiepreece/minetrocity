angular.module('minetrocity').controller('loginCtrl',
  function ($scope, $http, $location, user, alerts) {
    function checkForUser() {
      $http.get('/curr_user').then(
        function (resp) {
          user.isLoggedIn = true;
          user.name = resp.data.username;
          user.acl = resp.data.acl;
          $location.path(user.path || '/');
        },
        function (err) {
          console.error('Could Not Find Pre-Logged In User.');
          console.error(err);
        }
      );
    }
    checkForUser();

    $scope.login = function (username, pass) {
      var json = {
        username: username,
        password: pass
      };

      if (!json.username) {
        return alerts.create('error', 'You must enter a username.');
      }

      if (!json.password) {
        return alerts.create('error', 'You must enter a password.');
      }

      alerts.create('info', 'Logging in');
      $http.post('/login', json).then(
        function (result) {
          var d = result.data;
          if (d.success) {
            user.isLoggedIn = true;
            user.name = d.username;
            user.acl = d.acl;
            alerts.create('success', 'Logged In!');
            $location.path(user.path || '/');
          }
          else {
            console.error(d.err);
            alerts.create('error', d.err);
          }
        },
        function (reason) {
          console.error(reason);
          alerts.create('error', reason);
        }
      );
    };
  }
);
