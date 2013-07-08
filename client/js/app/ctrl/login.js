angular.module('minetrocity').controller('loginCtrl',
  function ($scope, $http, $location, user) {
    $scope.login = function () {
      var json = {
        username: $scope.user,
        password: $scope.pass
      };

      if (!json.username) {
        $scope.type = 'error';
        $scope.msg = 'You must enter a username.';
        return;
      }

      if (!json.password) {
        $scope.type = 'error';
        $scope.msg = 'You must enter a password.';
        return;
      }

      $scope.type = 'info';
      $scope.msg = 'Logging in';

      $http.post('/login', json).then(
        function (result) {
          var d = result.data;
          if (d.success) {
            user.isLoggedIn = true;
            user.name = d.username;
            user.acl = d.acl;
            $location.path('/');
          }
          else {
            $scope.type = 'error';
            $scope.msg = d.err;
          }
        },
        function (reason) {
          $scope.type = 'error';
          $scope.msg = reason;
        }
      );
    };
  }
);
