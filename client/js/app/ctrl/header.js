angular.module('minetrocity').controller('headerCtrl',
  function ($scope, $location) {
    $scope.nav = mNav;

    $scope.getItem = function (item) {
      $location.path(item.url);
    }
  }
);
