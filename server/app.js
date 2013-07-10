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

if(!fs.existsSync('models')){
  fs.mkdirSync('models');
}

/**
 * TOOLS
 */
if(fs.existsSync('models/permissions.json')){
  shared.set('permissions', JSON.parse(fs.readFileSync('models/permissions.json')));
} else {
  shared.set('permissions', JSON.parse('{"permissions":["VIEW_USERS","ADD_USERS",' +
    '"UPDATE_USERS","DELETE_USERS","VIEW_SERVERS","START_SERVERS","STOP_SERVERS",' +
    '"ADD_SERVERS","UPDATE_SERVERS","DELETE_SERVERS","RESTART_SERVERS","GET_VERSIONS",' +
    '"CHANGE_PORTS","VIEW_HISTORIES","CLEAR_NOTIFICATIONS","COMMAND_SERVERS"],' +
    '"deprecated_permissions":[]}'));
  fs.writeFileSync('models/permissions.json', JSON.stringify(shared.get('permissions')));
}

shared.set('notifications', []);

/**
 * USER METHODS
 */
if(fs.existsSync('models/users.json')){
  shared.set('users', JSON.parse(fs.readFileSync('models/users.json')));
} else {
  var admin = {
    id: uuid.v4(),
    username: 'admin',
    password: hash.generate('admin'),
    email: 'no_reply@minetrocity.com',
    acl: shared.get('permissions')['permissions']
  };
  shared.set('users', { 'admin' : admin });
  fs.writeFileSync('models/users.json', JSON.stringify(shared.get('users')));
}

/**
 * SERVER METHODS
 */
if(fs.existsSync('models/server.json')){
  shared.set('servers', JSON.parse(fs.readFileSync('models/servers.json')));
} else {
  shared.set('servers', {});
  fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));
}
