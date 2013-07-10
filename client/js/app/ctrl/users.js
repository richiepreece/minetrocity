angular.module('minetrocity').controller('usersCtrl',
  function ($scope, $http, $location, usersData) {
    usersData.getData().then(
      function (resp) {
        $scope.users = resp;
      },
      function (err) {
        console.error(err);
      }
    );

    $scope.updateUser = function (user) {
      var newUser = {
        id: user.id,
        username: user.username,
        email: user.email,
        acl: []
      };

      for (var i = 0; i < user.acl.length; ++i) {
        if (user.acl[i].has) {
          newUser.acl.push(user.acl[i].name);
        }
      }

      $http.put('/update_user', newUser).then(
        function (resp) {
          console.log('updated successfully!!');
        },
        function (err) {
          console.error(err);
        }
      );
    };

    $scope.newUser = function () {
      $location.path('/newUser');
    };

    $scope.deleteUser = function (user) {
      var json = {
        id: user.id
      };

      $http.post('/delete_user', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            return console.error(d.err);
          }

          console.log('deleted user: ' + user.id);
          for (var i = 0; i < $scope.users.length; ++i) {
            if ($scope.users[i].id === user.id) {
              $scope.users.splice(i, 1);
              break;
            }
          }
        },
        function (err) {
          console.error(err);
        }
      );
    };
  }
);
