angular.module('minetrocity').controller('headerCtrl',
  function ($scope, $location, $http, user) {
    $scope.nav = mNav;

    $scope.user = user;

    $scope.getItem = function (url) {
      $location.path(url);
    };

    $scope.logout = function () {
      $http.get('/logout').then(
        function (resp) {
          if (resp.data.success) {
            user.isLoggedIn = false;
            user.name = '';
            user.acl = [];
            $location.path('/login');
          }
          else {
            console.log('couldn\'t logout: ' + resp.data.err);
          }
        },
        function (err) {
          console.log('couldn\'t logout: ' + err);
        }
      );
    };
  }
);
