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
  , mcServer = require('./mc_server')
	, uuid     = require('node-uuid');
  ;

var app    = express()
  , server = http.createServer(app)
  , io     = io.listen(server)
  ;
	
shared.set('child', null);
shared.set('output', null);
shared.set('input', null);

var sessOptions = {
  key: 'minetrocity.sid',
  secret: "lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP"
};

app.models = {};
app.models.users = JSON.parse(fs.readFileSync('models/users.json'));
app.models.servers = JSON.parse(fs.readFileSync('models/servers.json'));

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

/*************************************************
* Everything above this line can be changed.
* Everything below it is the new stuff.
*************************************************/

app.post('/login', function(request, response, next){
	console.log('Logging in...');
	var data = '';
	
	var userInfo = request.body;
	var responseData = {};
	var user = app.models.users[userInfo['username']];
	
	if(user && user['username'] == userInfo['username'] &&
			user['password'] == userInfo['password']){
		console.log('Valid user');
		
		responseData['username'] = user['username'];
		responseData['success'] = true;
		responseData['acl'] = user['acl'];
		responseData['notifications'] = []; //TODO: get notifications
		
		request.session.user = user;
	} else {
		console.log('Invalid user');
		
		responseData['success'] = false;
		responseData['err'] = 'Username or Password is invalid';
	}
	
	response.send(responseData);
});

app.get('/logout', function(request, response, next){
	request.session.destroy(function(){
		var responseData = {};
		responseData['success'] = true;
		
		response.send(responseData);
	});
});

app.get('/users', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		responseData['users'] = app.models.users;
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/add_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var newUser = request.body;
		newUser['id'] = uuid.v4();
		app.models.users[newUser['username']] = newUser;
		
		fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
		
		responseData['id'] = newUser['id'];
		responseData['success'] = true;
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.put('/update_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var updatedUser = request.body;
		var oldUser;
		
		for(index in app.models.users){
			if(app.models.users[index]['id'] == updatedUser['id']){
				oldUser = app.models.users[index];
			}
		}
		
		if(oldUser){
			delete app.models.users[oldUser['username']];
			
			for(index in updatedUser){
				oldUser[index] = updatedUser[index];
			}
			
			app.models.users[oldUser['username']] = oldUser;
			
			fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
			
			responseData['id'] = updatedUser['id'];
			responseData['success'] = true;
		} else {
			responseData['success'] = false;
			responseData['err'] = 'User does not exist';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/delete_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var deleteUser = request.body;
		var existingUser = app.models.users[deleteUser['username']];
		
		if(existingUser && existingUser['id'] == deleteUser['id'] &&
				existingUser['username'] == deleteUser['username']){
			delete app.models.users[deleteUser['username']];

			fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
			
			responseData['id'] = deleteUser['id'];
			responseData['success'] = true;
		} else {
			responseData['success'] = false;
			responseData['err'] = 'User does not exist';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.get('/servers', function(request, response, next){
	responseData = {};
	
	if(request.session.user){
		responseData['servers'] = app.models.servers;
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/start_server', function(request, response, next){
});

app.post('/stop_server', function(request, response, next){
});

app.post('/add_server', function(request, response, next){
});

app.put('/update_server', function(request, response, next){
});

app.post('/delete_server', function(request, response, next){
});

app.post('/restart_server', function(request, response, next){
});

app.get('/versions', function(request, response, next){
});

app.post('/change_port', function(request, response, next){
});

app.post('/server_history', function(request, response, next){
});

app.post('/clear_notification', function(request, response, next){
});