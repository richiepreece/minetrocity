var app = angular.module('minetrocity', []).config(
  function ($routeProvider) {
    // var nav = _hg_.nav;
    // for (var i = 0; i < nav.length; i++) {
    //     var section = nav[i];
    //     for (var ii = 0; ii < section.items.length; ii++) {
    //         var item = section.items[ii];
    //         if (item.tmpl) {
    //             $routeProvider.when(item.url, {
    //                 templateUrl: item.tmpl,
    //                 controller: item.ctrl
    //             });
    //         }
    //     }
    // }

    $routeProvider.when('/', {
      templateUrl: 'tmpl/main.html',
      controller: 'mainCtrl'
    });

    $routeProvider.otherwise({redirectTo: '/'});
  }
);
