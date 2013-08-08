/* global angular, io */
angular.module('minetrocity').directive('socketIo',
  function ($rootScope) {
    'use strict';

    function link() {
      var msgs = ['msg', 'notification', 'notification_cleared', 'download_update'];
      var socket = io.connect();

      var bindMsg = function (msg) {
        socket.on(msg, function (data) {
          $rootScope.$emit(msg, data);
        });
      };

      for (var i = 0; i < msgs.length; ++i) {
        bindMsg(msgs[i]);
      }
    }

    return {
      restrict: 'EA',
      link: link
    };
  }
);
