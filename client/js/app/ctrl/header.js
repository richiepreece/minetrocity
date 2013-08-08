/* global angular, mNav */
angular.module('minetrocity').controller('headerCtrl',
  function ($scope, $location, $http, user, alerts) {
    'use strict';

    $scope.nav = mNav;
    $scope.user = user;

    $scope.getItem = function (url) {
      $location.path(url);
    };

    $scope.logout = function () {
      $http.get('/logout').then(
        function (resp) {
          if (!resp.data.success) {
            return alerts.create('error', resp.data.err);
          }
          user.isLoggedIn = false;
          user.name = '';
          user.acl = [];
          $location.path('/login');
        },
        function (err) {
          alerts.create('error', err);
        }
      );
    };
  }
);
