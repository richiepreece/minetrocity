/* global angular */
angular.module('minetrocity').directive('footer',
  function () {
    'use strict';

    function link() {}

    return {
      restrict: 'EA',
      controller: 'footerCtrl',
      link: link,
      templateUrl: 'tmpl/footer.html',
      scope: {}
    };
  }
);
