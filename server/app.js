/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var express  = require('express')
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
  , server   = http.createServer(app)
  , io       = io.listen(server)
  ;

shared.set('io', io);

var sessOptions = {
  key: 'minetrocity.sid',
  secret: 'lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP'
};

app.configure(function () {
  app.set('port', process.env.VCAP_APP_PORT || process.env.PORT || 3000);
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
});

app.configure('development', function () {
  app.use(express.errorHandler());
});

fs.readdirSync(__dirname + '/routes').forEach(
  function (file) {
    require('./routes/' + file)(app);
  }
);

server.listen(app.get('port'), function () {
  console.log("Express server listening on port " + app.get('port'));
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