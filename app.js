/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var express  = require('express')
  , http     = require('http')
  , path     = require('path')
  , fs       = require('fs')
  , io       = require('socket.io')
  , url      = require('url')
  , timers   = require('timers')
  , os       = require('os')
  , shared   = require('./tools/shared')
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
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(express.cookieParser());
  app.use(express.session(sessOptions));
  app.use(attachUser);
  app.use(app.router);
  app.use(require('stylus').middleware(__dirname + '/public'));
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function () {
  app.use(express.errorHandler());
});

function attachUser(req, res, next) {
  if (req.session && req.session.user)
    res.locals({ user: req.session.user });
  next();
};

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
var tools = require('./tools/tools');

tools.getVersions();
setInterval(tools.getVersions, 1000 * 60 * 60 * 12);

app.get('/versions', tools.versions);
app.post('/clear_notification', tools.clearNotification);

/**
 * USER METHODS
 */
if(fs.existsSync('models/users.json')){
	shared.set('users', JSON.parse(fs.readFileSync('models/users.json')));
} else {
	shared.set('users', { username : 'admin', password : hash.generate('admin') });
	fs.writeFileSync('models/users.json', JSON.stringify(shared.get('users')));
}
var users = require('./tools/users.js');

app.post('/login', users.login);
app.get('/logout', users.logout);
app.get('/users', users.users);
app.post('/add_user', users.addUser);
app.put('/update_user', users.updateUser);
app.delete('/delete_user', users.deleteUser);

/**
 * SERVER METHODS
 */
if(fs.existsSync('models/server.json')){
	shared.set('servers', JSON.parse(fs.readFileSync('models/servers.json')));
} else {
	shared.set('servers', {});
	fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));
}
var servers = require('./tools/servers.js');
 
app.get('/servers', servers.servers);
app.post('/start_server', servers.startServer);
app.post('/stop_server', servers.stopServer);
app.post('/add_server', servers.addServer);
app.put('/update_server', servers.updateServer);
app.post('/change_port', servers.changePort);
app.delete('/delete_server', servers.deleteServer);
app.post('/restart_server', servers.restartServer);
app.post('/server_history', servers.serverHistory);
app.post('/command_server', servers.commandServer);