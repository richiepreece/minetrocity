var    http = require('http'),
         fs = require('fs'),
    express = require('express'),
     stylus = require('stylus'),
         io = require('socket.io');

var    app = express(),
    server = http.createServer(app),
        io = io.listen(server);

console.log(__dirname);
var sessOptions = {
  key: 'minetrocity.sid',
  secret: "lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP"
};

app.set('port', process.env.VCAP_APP_PORT || process.env.PORT || 3000);

app.configure(
  function () {
    app.use(express.favicon());
    app.use(express.logger('dev'));
    app.use(express.bodyParser());
    app.use(express.methodOverride());
    app.use(express.cookieParser());
    app.use(express.session(sessOptions));
    app.use(app.router);
    app.use(stylus.middleware({
      debug: true,
      src: 'client',
      dest: 'client'
    }));
    app.use(express.static('client'));
    app.use(express.errorHandler());
  }
);

fs.readdirSync(__dirname + '/routes').forEach(
  function (file) {
    require('./routes/' + file)(app);
  }
);

server.listen(app.get('port'),
  function () {
    console.log("Express server listening on port " + app.get('port'));
  }
);
