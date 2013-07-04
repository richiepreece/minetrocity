angular.module('minetrocity').controller('footerCtrl',
  function ($scope) {
    $scope.year = new Date().getFullYear();
  }
);
