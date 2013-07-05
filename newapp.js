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
  , uuid     = require('node-uuid')
	, hash     = require('password-hash')
  ;

var app    = express()
  , server = http.createServer(app)
  , io     = io.listen(server)
  ;

var sessOptions = {
  key: 'minetrocity.sid',
  secret: "lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP"
};

app.models = {};
app.models.users = JSON.parse(fs.readFileSync('models/users.json'));
app.models.servers = JSON.parse(fs.readFileSync('models/servers.json'));
app.models.permissions = JSON.parse(fs.readFileSync('models/permissions.json'));

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

function createWorld(newServer){
	var defaultFile = "generator-settings=\nallow-nether=true\nlevel-name=%%FS_SAFE_NAME%%\n" + 
		"enable-query=false\nallow-flight=false\nserver-port=%%PORT_NUM%%\nlevel-type=DEFAULT\n" +
		"enable-rcon=false\nforce-gamemode=false\nlevel-seed=\nserver-ip=\nmax-build-height=256\n" +
		"spawn-npcs=true\nwhite-list=false\nspawn-animals=true\nhardcore=false\ntexture-pack=\n" +
		"online-mode=true\npvp=true\ndifficulty=1\ngamemode=0\nmax-players=20\nspawn-monsters=true\n" + 
		"generate-structures=true\nview-distance=10\nmotd=%%SERVER_NAME%%";
		
	var fsSafeName = newServer['server_name'].replace(/\W/g, '');
	
	defaultFile = defaultFile.replace(/%%FS_SAFE_NAME%%/g, fsSafeName);
	defaultFile = defaultFile.replace(/%%SERVER_NAME%%/g, newServer['server_name']);
	defaultFile = defaultFile.replace(/%%PORT_NUM%%/g, newServer['port']);
	
	var currDir = process.cwd();
	
	if(!fs.existsSync("worlds")){
		fs.mkdirSync("worlds");
	}
	
	process.chdir("worlds");
	
	if(!fs.existsSync(newServer['id'])){
		fs.mkdirSync(newServer['id']);
	}
	
	process.chdir(newServer['id']);
	
	fs.writeFileSync('server.properties', defaultFile);
	
	process.chdir(currDir);	
}

function getServer(newServer){	
	var serverUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/" + 
		newServer['version'] + "/minecraft_server." + newServer['version'] + ".jar";
	
	var options = {
		host: url.parse(serverUrl).host,
		port: 443,
		path: url.parse(serverUrl).pathname
	};
	
	var currDir = process.cwd();
	
	if(!fs.existsSync("versions")){
		fs.mkdirSync("versions");
	}
	
	process.chdir("versions");
	
	if(!fs.existsSync(newServer['version_type'])){
		fs.mkdirSync(newServer['version_type']);
	}
	
	process.chdir(newServer['version_type']);
	
	if(!fs.existsSync(newServer['version'])){
		fs.mkdirSync(newServer['version']);
	}
	
	process.chdir(newServer['version']);
	
	if(!fs.existsSync('minecraft_server.jar')){	
		var file = fs.createWriteStream(process.cwd() + '/minecraft_server.jar');
		
		https.get(options, function(result){
			result.on('data', function(data){
				file.write(data);
			}).on('end', function(){
				file.end();
			});
		});
	}
	
	process.chdir(currDir);
}

function getVersions(){
	var mojangVersionUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
	
	var options = {
		host: url.parse(mojangVersionUrl).host,
		port: 443,
		path: url.parse(mojangVersionUrl).path
	}
	
	var request = https.request(options, function(result){		
		if(result.statusCode == 200){		
			result.on('data', function(data){
				var currDir = process.cwd();
			
				if(!fs.existsSync("versions")){
					fs.mkdirSync("versions");
				}
				
				process.chdir("versions");
				
				var file = fs.createWriteStream(process.cwd() + '/versions.json');				
				file.write(data);				
				file.end();
				
				process.chdir(currDir);				
				
				app.models.versions = JSON.parse(data);
			});
		}
	});
	
	request.end();
	
	request.on('error', function(error){
		console.error(error);
	});
}
getVersions();

app.post('/login', function(request, response, next){
	console.log('Logging in...');
	var data = '';
	
	var userInfo = request.body;
	var responseData = {};
	var user = app.models.users[userInfo['username']];
	
	if(user && user['username'] == userInfo['username'] &&
			hash.verify(userInfo['password'], user['password'])){
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
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			responseData['users'] = app.models.users;
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/add_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'ADD_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var newUser = request.body;
			
			if(!app.models.users[newUser['username']]){
				newUser['id'] = uuid.v4();
				newUser['password'] = hash.generate(newUser['password']);
				app.models.users[newUser['username']] = newUser;
				
				fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
				
				responseData['id'] = newUser['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'A user exists with that name';
			}
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.put('/update_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'UPDATE_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
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
					if(index == 'password'){
						oldUser[index] = hash.generate(updatedUser[index]);
					} else {
						oldUser[index] = updatedUser[index];
					}
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
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.delete('/delete_user', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'DELETE_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var deleteUser = request.body;
			var existingUser;
			
			for(index in app.models.users){
				if(app.models.users[index]['id'] == deleteUser['id']){
					existingUser = app.models.users[index];
				}
			}
			
			if(existingUser && existingUser['id'] == deleteUser['id']){
				delete app.models.users[existingUser['username']];

				fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
				
				responseData['id'] = deleteUser['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'User does not exist';
			}
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.get('/servers', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			responseData['servers'] = app.models.servers;
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/start_server', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'START_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var serverToStart = request.body;			
			var server;
			
			for(index in app.models.servers){
				if(app.models.servers[index]['id'] == serverToStart['id']){
					server = app.models.servers[index];
				}
			}
			
			if(server){
				var currDir = process.cwd();
				
				if(fs.existsSync('worlds')){
					process.chdir('worlds');
					
					if(fs.existsSync(serverToStart['id'])){
						process.chdir(serverToStart['id']);
						
						if(!shared.get('child' + server['id'])){
							shared.set('child' + server['id'],
								require('child_process').exec('java -Xmx1024M -Xms1024M -jar ../../versions/' +
									server['version_type'] + '/' + server['version'] + '/minecraft_server.jar',
									function(err, stdout, stderr){
										shared.set('child' + server['id'], null);
										shared.set('output' + server['id'], null);
										shared.set('input' + server['id'], null);
										shared.set('history' + server['id'], null);
										
										//TODO: alert users of closed server
									}));
							
							shared.set('output' + server['id'], shared.get('child' + server['id']).stderr);
							shared.set('input' + server['id'], shared.get('child' + server['id']).stdin);
							shared.set('history' + server['id'], []);
							
							shared.get('output' + server['id']).on('data', function(data){
								shared.get('history' + server['id']).push(data);
								//TODO: share with users
							});
							
							responseData['id'] = server['id'];
							responseData['success'] = true;
							responseData['history'] = []; //TODO: set history
						} else {
							responseData['success'] = false;
							responseData['err'] = 'The server is already running';
							responseData['history'] = shared.get('history' + server['id']);
						}
					} else {
						responseData['success'] = false;
						responseData['err'] = 'The server files could not be found';
					}
				} else {
					responseData['success'] = false;
					responseData['err'] = 'The server files could not be found';
				}
				
				process.chdir(currDir);
			} else {
				responseData['success'] = false;
				responseData['err'] = 'The server could not be found';
			}
		} else {
			responseData['success'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/stop_server', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'STOP_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var server = request.body;
			
			if(shared.get('input' + server['id'])){
				shared.get('input' + server['id']).write("/stop\n");
				
				responseData['id'] = server['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'Server is not running';
			}
		} else {
			responseData['success'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/add_server', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'ADD_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var newServer = request.body;
			var fsSafeName = newServer['server_name'].replace(/\W/g, '');
			newServer['id'] = uuid.v4();
			
			if(!app.models.servers[newServer['id']]){
				app.models.servers[newServer['id']] = newServer;
				
				fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
				
				getServer(newServer);
				createWorld(newServer);
				
				responseData['id'] = newServer['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'The server already exists';
			}
		} else {
			responseData['success'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.put('/update_server', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'UPDATE_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var updatedServer = request.body;
			var oldServer = app.models.servers[updatedServer['id']];
			
			if(oldServer){
				if(!shared.get('child' + oldServer['id'])){
					var oldFsSafeName = oldServer['server_name'].replace(/\W/g, '');	
					var oldName = oldServer['server_name'];
					var oldPort = oldServer['port'];
					
					for(index in updatedServer){
						oldServer[index] = updatedServer[index];
					}
					
					var newFsSafeName = oldServer['server_name'].replace(/\W/g, '');
					var newName = oldServer['server_name'];
					var newPort = oldServer['port'];
					
					fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
					
					var currDir = process.cwd();
					
					if(fs.existsSync('worlds')){
						process.chdir('worlds');
						
						if(fs.existsSync(oldServer['id'])){
							process.chdir(oldServer['id']);
							
							if(fs.existsSync(oldFsSafeName)){
								fs.renameSync(oldFsSafeName, newFsSafeName);
							}
							
							var serverProps = fs.readFileSync('server.properties', { encoding : 'utf8' });
							var serverProps = serverProps.replace(oldFsSafeName, newFsSafeName);
							var serverProps = serverProps.replace(oldName, newName);
							var serverProps = serverProps.replace('server-port=' + oldPort, 'server-port=' + newPort);
							
							getServer(oldServer);
							
							fs.writeFileSync('server.properties', serverProps);
						}
					}
					
					process.chdir(currDir);
					
					responseData['id'] = updatedServer['id'];
					responseData['success'] = true;					
				} else {
					responseData['success'] = false;
					responseData['err'] = "Server is running. Can't update.";
				}
			} else {
				responseData['success'] = false;
				responseData['err'] = 'User does not exist';
			}
		} else {
			responseData['success'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.delete('/delete_server', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'DELETE_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var deleteServer = request.body;
			var oldServer = app.models.servers[deleteServer['id']];
			
			if(oldServer){
				delete app.models.servers[oldServer['id']];

				fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
				
				var currDir = process.cwd();
				
				if(fs.existsSync('worlds')){
					process.chdir('worlds');
					
					if(fs.existsSync(oldServer['id'])){
						//fs.rmdirSync(fsSafeName);
						//TODO: Recursive delete
					}
				}
				
				process.chdir(currDir);
				
				responseData['id'] = deleteServer['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'Server does not exist';
			}
		} else {
			responseData['success'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/restart_server', function(request, response, next){
});

app.get('/versions', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'GET_VERSIONS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			responseData['versions'] = app.models.versions;
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/change_port', function(request, response, next){
});

app.post('/server_history', function(request, response, next){
	var responseData = {};
	
	if(request.session.user){
		var isAllowed = false;
		
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_HISTORIES'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var server = request.body;
			
			if(shared.get('history' + server['id'])){				
				responseData['id'] = server['id'];
				responseData['success'] = true;
				responseData['history'] = shared.get('history' + server['id']);
			} else {
				responseData['success'] = false;
				responseData['err'] = 'Server is not running';
			}	
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
});

app.post('/clear_notification', function(request, response, next){
});