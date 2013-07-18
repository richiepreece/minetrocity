angular.module('minetrocity').directive('footer',
  function () {
    function link(scope, el, attrs, ctrl) {}

    return {
      restrict: 'EA',
      controller: 'footerCtrl',
      link: link,
      templateUrl: 'tmpl/footer.html',
      scope: {}
    };
  }
);
