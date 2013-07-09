angular.module('minetrocity').controller('usersCtrl',
  function ($scope, usersData) {
    $scope.users = usersData.getUsers();
  }
);
