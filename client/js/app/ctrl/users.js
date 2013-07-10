angular.module('minetrocity').controller('usersCtrl',
  function ($scope, $http, usersData) {
    usersData.getData().then(
      function (resp) {
        $scope.users = resp;
      },
      function (err) {
        console.log(err);
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
          console.log('could not update');
        }
      );
    };
  }
);
