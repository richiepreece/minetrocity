angular.module('minetrocity').factory('user',
  function () {
    var user = {
      isLoggedIn: false,
      name: '',
      path: '/',
      acl: []
    };

    return user;
  }
);
