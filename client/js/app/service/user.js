/* global angular */
angular.module('minetrocity').factory('user',
  function () {
    'use strict';

    var user = {
      isLoggedIn: false,
      name: '',
      path: '/',
      acl: []
    };

    return user;
  }
);
