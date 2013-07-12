angular.module('minetrocity').directive('checkUser',
  function ($rootScope, $location, user) {
    function link() {
      $rootScope.$on('$routeChangeStart',
        function (e, next, curr) {
          var nextPage = (next || {}).name;

          if (!user.isLoggedIn && nextPage !== 'Login') {
            user.path = next.url;
            $location.path('/login');
          }

          if (user.isLoggedIn && nextPage === 'Login') {
            $location.path('/');
          }

          // Check ACL of curr right here
          // or is it of next...
        }
      );
    }

    return {
      restrict: 'EA',
      link: link,
      scope: {}
    };
  }
);
