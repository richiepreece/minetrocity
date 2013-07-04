var mNav = [
  {
    label: 'Main',
    url:  '/',
    tmpl: 'tmpl/main.html',
    ctrl: 'mainCtrl'
  },
  {
    label: 'Login',
    url:  '/login',
    tmpl: 'tmpl/login.html',
    ctrl: 'loginCtrl'
  },
  {
    label: 'Servers',
    url:  '/servers',
    tmpl: 'tmpl/servers.html',
    ctrl: 'serversCtrl'
  },
];

var app = angular.module('minetrocity', []).config(
  function ($routeProvider) {
    for (var i = 0; i < mNav.length; ++i) {
      $routeProvider.when(mNav[i].url, {
        templateUrl: mNav[i].tmpl,
        controller: mNav[i].ctrl
      });
    }

    $routeProvider.otherwise({redirectTo: '/'});
  }
);
