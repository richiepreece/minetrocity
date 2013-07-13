angular.module('minetrocity').directive('socketIo',
  function ($rootScope) {
    function link() {
      var msgs = ['msg', 'notification', 'notification_cleared', 'download_update'];
      var socket = io.connect();
      for (var i = 0; i < msgs.length; ++i) {
        (function (msg) {
          socket.on(msg, function (data) {
            console.log('GOT A MESSAGE ' + msg);
            console.log(data);
            $rootScope.$emit(msg, data);
          });
        }(msgs[i]));
      }
    }

    return {
      restrict: 'EA',
      link: link
    };
  }
);
