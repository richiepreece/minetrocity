/* jshint node:true */
/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var express  = require('express')
  , https    = require('https')
  , http     = require('http')
  , stylus   = require('stylus')
  , fs       = require('fs')
  , io       = require('socket.io')
  , url      = require('url')
  , timers   = require('timers')
  , os       = require('os')
  , shared   = require('./shared')
  , uuid     = require('node-uuid')
  , hash     = require('password-hash')

  , app      = express()
  ;

app.set('port', process.env.PORT || 3001);

shared.set('io', io);

var options = {
  key: fs.readFileSync('server/certs/privatekey.pem').toString(),
  cert: fs.readFileSync('server/certs/certificate.pem').toString()
};

var sessOptions = {
  key: 'minetrocity.sid',
  secret: 'lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP'
};

var server = https.createServer(options, app);
io = io.listen(server);

var ioProdConfig = function () {
  'use strict';

  io.enable('browser client minification');
};
io.configure('staging', ioProdConfig);
io.configure('production', ioProdConfig);

var devConfig = function () {
  'use strict';

  app.use(express.favicon());
  app.use(stylus.middleware({
    debug: true,
    src: 'client',
    dest: 'client'
  }));
  app.use(express.static('client'));
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(express.cookieParser());
  app.use(express.session(sessOptions));
  app.use(app.router);
  app.use(express.errorHandler());
};
app.configure('development', devConfig);
app.configure('localdev', devConfig);

var prodConfig = function () {
  'use strict';

  app.use(express.favicon());
  app.use(express.static('build'));
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(express.cookieParser());
  app.use(express.session(sessOptions));
  app.use(app.router);
};
app.configure('staging', prodConfig);
app.configure('production', prodConfig);

fs.readdirSync(__dirname + '/routes').forEach(
  function (file) {
    require('./routes/' + file)(app);
  }
);

server.listen(app.get('port'), function () {
  console.log("Express server listening on port " + app.get('port') + ' in environment ' + app.get('env'));
});

/**
 * httpApp is used to listen and forward any http/80 traffic
 * to an ecrpted https/443 connection for security
 */
var httpApp = express();
http.createServer(httpApp).listen(3000);
httpApp.get('*', function(req, res, next){
  res.redirect('https://' + //Forward traffic to https
    req.headers.host.split(/:/g)[0] + //Strip off any ports
    ":" + app.get('port') + //Add the https port
    req.url); //Add the original request url
});

/**
 * Socket stuff.... maybe
 */
shared.set('sockets', {});
io.sockets.on('connection', function(socket){
  shared.get('sockets')[socket.id] = socket;

  socket.on('disconnect', function(){
    delete shared.get('sockets')[socket.id];
  });
});
