angular.module('minetrocity').factory('user',
  function () {
    var user = {
      isLoggedIn: false,
      name: '',
      acl: []
    };

    return user;
  }
);
