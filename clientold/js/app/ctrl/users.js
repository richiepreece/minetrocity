angular.module('minetrocity').controller('usersCtrl',
  function ($scope, $http, usersData, alerts) {
    function getData() {
      usersData.getData().then(
        function (resp) {
          $scope.users = resp;
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    }
    getData();

    ////////////////////////////////////////////////////////////////////////////////
    //-- Modal Stuff -------------------------------------------------------------//
    ////////////////////////////////////////////////////////////////////////////////
    $scope.modalOpts = { backdropFade: true, dialogFade: true };

    $scope.openModal = function () {
      $scope.showModal = true;
    };

    $scope.closeModal = function () {
      $scope.showModal = false;
    };

    $scope.createUser = function (user, pass, email) {
      var obj = {
        username: user,
        password: pass,
        email: email,
        acl: []
      };

      alerts.create('info', 'Creating User...');
      $http.post('/add_user', obj).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }
          alerts.create('success', 'User Created!');
          obj.id = d.id;
          obj.acl = usersData.getAcl(obj.acl);
          $scope.users.push(obj);

          $scope.user = '';
          $scope.pass = '';
          $scope.email = '';
          $scope.showModal = false;
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    ////////////////////////////////////////////////////////////////////////////////
    //-- Server Actions ----------------------------------------------------------//
    ////////////////////////////////////////////////////////////////////////////////
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

      alerts.create('info', 'Updating User...');
      $http.put('/update_user', newUser).then(
        function (resp) {
          alerts.create('success', 'Updated User!');
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };

    $scope.deleteUser = function (user) {
      var json = {
        id: user.id
      };

      alerts.create('info', 'Deleting User...');
      $http.post('/delete_user', json).then(
        function (resp) {
          var d = resp.data;
          if (!d.success) {
            console.error(d.err);
            return alerts.create('error', d.err);
          }

          alerts.create('success', 'Deleted User!');
          for (var i = 0; i < $scope.users.length; ++i) {
            if ($scope.users[i].id === user.id) {
              $scope.users.splice(i, 1);
              break;
            }
          }
        },
        function (err) {
          console.error(err);
          alerts.create('error', err);
        }
      );
    };
  }
);
