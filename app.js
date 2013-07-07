var express  = require('express')
  , http     = require('http')
  , https    = require('https')
  , path     = require('path')
  , fs       = require('fs')
  , io       = require('socket.io')
  , url      = require('url')
  , timers   = require('timers')
  , os       = require('os')
  , shared   = require('./shared')
  //, mcServer = require('./mc_server')
  , uuid     = require('node-uuid')
	, hash     = require('password-hash')
  ;
	
var sslOptions = {
	key: fs.readFileSync('ssl/key.pem'),//, { encoding: 'utf8' }),
	cert: fs.readFileSync('ssl/cert.pem')//, { encoding: 'utf8' })
}

var app    = express()
  , server = http.createServer(app)
	//, sslserver = https.createServer(sslOptions, app)
  , io     = io.listen(server)
  ;

var sessOptions = {
  key: 'minetrocity.sid',
	secret: uuid.v4() + uuid.v4()
};

shared.set('notifications', []);

app.models = {};
app.models.users = JSON.parse(fs.readFileSync('models/users.json'));
app.models.servers = JSON.parse(fs.readFileSync('models/servers.json'));
app.models.permissions = JSON.parse(fs.readFileSync('models/permissions.json'));

app.configure(function () {
  app.set('port', process.env.VCAP_APP_PORT || process.env.PORT || 3000);
	app.set('sslPort', 443);
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
/*
sslServer.listen(app.get('sslPort'), function(){
	console.log("Express server listening on port " + app.get('sslPort'));
});*/

/*************************************************
* Everything above this line can be changed.
* Everything below it is the new stuff.
*************************************************/

/**
 * TOOLS
 */
var tools = require('./tools/tools');
tools.getVersions();

setInterval(tools.getVersions, 1000 * 60 * 60 * 12);

app.get('/versions', tools.versions);
app.post('/clear_notification', tools.clearNotification);

/**
 * USER METHODS
 */
shared.set('users', JSON.parse(fs.readFileSync('models/users.json')));
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
shared.set('servers', JSON.parse(fs.readFileSync('models/servers.json')));
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