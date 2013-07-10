angular.module('minetrocity').controller('newUserCtrl',
  function ($scope, $http, $location) {

    $scope.saveUser = function (user, pass, email) {
      var obj = {
        username: user,
        password: pass,
        email: email,
        acl: []
      };

      $http.post('/add_user', obj).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return $scope.msg = d.err;
          }
          $location.path('/users');
        },
        function (err) {
          console.error(err);
          $scope.msg = err.data;
        }
      );
    };

    $scope.back = function () {
      $location.path('/users');
    };
  }
);
