/* global angular */
angular.module('minetrocity').directive('header',
  function () {
    'use strict';

    function link() {}

    return {
      restrict: 'EA',
      controller: 'headerCtrl',
      link: link,
      templateUrl: 'tmpl/header.html',
      scope: {}
    };
  }
);
