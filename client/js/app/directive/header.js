angular.module('minetrocity').directive('header',
  function () {
    function link(scope, el, attrs, ctrl) {}

    return {
      restrict: 'EA',
      controller: 'headerCtrl',
      link: link,
      templateUrl: 'tmpl/header.html',
      scope: {}
    };
  }
);
