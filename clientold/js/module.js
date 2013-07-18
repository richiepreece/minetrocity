var app = angular.module('minetrocity', ['ui.bootstrap']).config(
  function ($routeProvider) {
    for (var i = 0; i < mNav.length; ++i) {
      $routeProvider.when(mNav[i].url, {
        templateUrl: mNav[i].tmpl,
        controller: mNav[i].ctrl,
        name: mNav[i].label,
        url: mNav[i].url
      });
    }

    $routeProvider.otherwise({redirectTo: '/'});
  }
);
