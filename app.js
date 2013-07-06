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
	secret: uuid.v4() + uuid.v4()
};

shared.set('notifications', []);

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

/**
 * This method removes a directory recursively 
 */
fs.rmdirSyncRec = function(path) {
    var files = [];
    if( fs.existsSync(path) ) {
        files = fs.readdirSync(path);
        files.forEach(function(file,index){
            var curPath = path + "/" + file;
            if(fs.statSync(curPath).isDirectory()) { // recurse
                fs.rmdirSyncRec(curPath);
            } else { // delete file
                fs.unlinkSync(curPath);
            }
        });
        fs.rmdirSync(path);
    }
};

/**
 * This method creates the world folder for a new server
 */
function createWorld(newServer){
	//Default minecraft server.properties file
	var defaultFile = "generator-settings=\nallow-nether=true\nlevel-name=%%FS_SAFE_NAME%%\n" + 
		"enable-query=false\nallow-flight=false\nserver-port=%%PORT_NUM%%\nlevel-type=DEFAULT\n" +
		"enable-rcon=false\nforce-gamemode=false\nlevel-seed=\nserver-ip=\nmax-build-height=256\n" +
		"spawn-npcs=true\nwhite-list=false\nspawn-animals=true\nhardcore=false\ntexture-pack=\n" +
		"online-mode=true\npvp=true\ndifficulty=1\ngamemode=0\nmax-players=20\nspawn-monsters=true\n" + 
		"generate-structures=true\nview-distance=10\nmotd=%%SERVER_NAME%%";
	
	//We need to get a file system safe name for the level-name
	var fsSafeName = newServer['server_name'].replace(/\W/g, '');
	
	//Set the properties
	defaultFile = defaultFile.replace(/%%FS_SAFE_NAME%%/g, fsSafeName);
	defaultFile = defaultFile.replace(/%%SERVER_NAME%%/g, newServer['server_name']);
	defaultFile = defaultFile.replace(/%%PORT_NUM%%/g, newServer['port']);
	
	var currDir = process.cwd();
	
	//Make sure a worlds folder exists
	if(!fs.existsSync("worlds")){
		fs.mkdirSync("worlds");
	}
	
	process.chdir("worlds");
	
	//Make sure the server id folder exists
	if(!fs.existsSync(newServer['id'])){
		fs.mkdirSync(newServer['id']);
	}
	
	process.chdir(newServer['id']);
	
	//Write the server.properties file
	fs.writeFileSync('server.properties', defaultFile);
	
	process.chdir(currDir);	
}

/**
 * This method ensures that the minecraft_server.jar file exists for the
 * version specified by the server
 */
function getServer(newServer){	
	//The minecraft_server.jar file is housed in this location
	var serverUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/" + 
		newServer['version'] + "/minecraft_server." + newServer['version'] + ".jar";
	
	var options = {
		host: url.parse(serverUrl).host,
		port: 443,
		path: url.parse(serverUrl).pathname
	};
	
	var currDir = process.cwd();
	
	//Ensure the versions folder exists
	if(!fs.existsSync("versions")){
		fs.mkdirSync("versions");
	}
	
	process.chdir("versions");
	
	//Ensure the version type (release/snapshot) folder exists
	if(!fs.existsSync(newServer['version_type'])){
		fs.mkdirSync(newServer['version_type']);
	}
	
	process.chdir(newServer['version_type']);
	
	//Ensure the version folder exists
	if(!fs.existsSync(newServer['version'])){
		fs.mkdirSync(newServer['version']);
	}
	
	process.chdir(newServer['version']);
	
	//If the minecraft_server.jar file exists, don't re-download it -- it could be running
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

/**
 * This method gets the versions.json file from the mojang servers
 */
function getVersions(){
	//Location of the versions.json file
	var mojangVersionUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
	
	var options = {
		host: url.parse(mojangVersionUrl).host,
		port: 443,
		path: url.parse(mojangVersionUrl).path
	}
	
	//Get the file
	var request = https.request(options, function(result){		
		if(result.statusCode == 200){		
			result.on('data', function(data){
				var currDir = process.cwd();
			
				//Ensure the versions folder exists
				if(!fs.existsSync("versions")){
					fs.mkdirSync("versions");
				}
				
				process.chdir("versions");
				
				//Write out to a file for safe keeping
				var file = fs.createWriteStream(process.cwd() + '/versions.json');				
				file.write(data);				
				file.end();
				
				process.chdir(currDir);				
				
				//Set the versions data for use (this way we have no IO when versions are requested)
				app.models.versions = JSON.parse(data);
			});
		}
	});
	
	request.end();
}
getVersions(); //TODO: set this as a timer for every 12 hours

/**
 * This method logs a user in
 */
app.post('/login', function(request, response, next){
	var responseData = {};
	
	var userInfo = request.body;
	var user = app.models.users[userInfo['username']];
	
	//If username and password match, we have a valid login
	if(user && user['username'] == userInfo['username'] &&
			hash.verify(userInfo['password'], user['password'])){
		//Set session data
		request.session.user = user;
		
		responseData['username'] = user['username'];
		responseData['success'] = true;
		responseData['acl'] = user['acl'];
		responseData['notifications'] = shared.get('notifications');
	} else {
		responseData['success'] = false;
		responseData['err'] = 'Username or Password is invalid';
	}
	
	response.send(responseData);
});

app.get('/logout', function(request, response, next){
	var responseData = {};
	
	request.session.destroy(function(){
		responseData['success'] = true;
	});
	
	response.send(responseData);
});

/**
 * The method returns a list of users
 */
app.get('/users', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Set list of users
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

/**
 * This method adds a new user
 */
app.post('/add_user', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check for permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'ADD_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var newUser = request.body;
			
			//Make sure new user data has all of the needed fields
			if(newUser['username'] && newUser['password'] &&
					newUser['email'] && newUser['acl']){			
				//Check to make sure username doesn't exist
				if(!app.models.users[newUser['username']]){
					//Set user id, hash the password, and add user to list
					newUser['id'] = uuid.v4();
					newUser['password'] = hash.generate(newUser['password']);
					app.models.users[newUser['username']] = newUser;
					
					//Write user file
					fs.writeFileSync('models/users.json', JSON.stringify(app.models.users));
					
					responseData['id'] = newUser['id'];
					responseData['success'] = true;
				} else {
					responseData['success'] = false;
					responseData['err'] = 'A user exists with that name';
				}
			} else {
				responseData['sucess'] = false;
				responseData['err'] = 'There is not enough new user data';
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

/**
 *	This method updates a user
 */
app.put('/update_user', function(request, response, next){
	var responseData = {};
	
	//Checked for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check for permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'UPDATE_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var updatedUser = request.body;
			var oldUser;
			
			//Get the user
			for(index in app.models.users){
				if(app.models.users[index]['id'] == updatedUser['id']){
					oldUser = app.models.users[index];
				}
			}
			
			//If the user exists
			if(oldUser){
				delete app.models.users[oldUser['username']];
				
				//Replace all of the fields specified
				for(index in updatedUser){
					if(index == 'password'){
						oldUser[index] = hash.generate(updatedUser[index]);
					} else {
						oldUser[index] = updatedUser[index];
					}
				}
				
				//Add updated user and output to file
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

/**
 * This method deletes a user
 */
app.delete('/delete_user', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'DELETE_USERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var deleteUser = request.body;
			var existingUser;
			
			//Find user
			for(index in app.models.users){
				if(app.models.users[index]['id'] == deleteUser['id']){
					existingUser = app.models.users[index];
				}
			}
			
			//If the user exists, then delete and output to file
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

/**
 * This method returns the list of servers
 */
app.get('/servers', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Set list of servers
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

/**
 * This method starts a server
 */
app.post('/start_server', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'START_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var serverToStart = request.body;			
			var server = app.models.servers[serverToStart['id']];
			
			//If server exists
			if(server){
				var currDir = process.cwd();
				
				//Navigate to the world folder
				if(fs.existsSync('worlds')){
					process.chdir('worlds');
					
					//Navigate to the server-specific folder
					if(fs.existsSync(serverToStart['id'])){
						process.chdir(serverToStart['id']);
						
						//If the server isn't running, start it
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
							
							//Watch for data coming through the console. Add it to the history and broadcast
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

/**
 * This method stops a server
 */
app.post('/stop_server', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'STOP_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var server = request.body;
			
			//Make sure the server is running before you try to stop it
			if(shared.get('input' + server['id'])){
				//Stop the server using the /stop command
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

/**
 * This method adds a server
 */
app.post('/add_server', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'ADD_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			var newServer = request.body;
			
			//Make sure we have all the needed info
			if(newServer['server_name'] && newServer['port'] &&
					newServer['version'] && newServer['version_type']){
				//Get filesystem safe name and assign an id
				var fsSafeName = newServer['server_name'].replace(/\W/g, '');
				newServer['id'] = uuid.v4();
				
				//Register new server, and write to file
				app.models.servers[newServer['id']] = newServer;				
				fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
				
				//Get minecraft_server.jar and create server.properties file
				getServer(newServer);
				createWorld(newServer);
				
				responseData['id'] = newServer['id'];
				responseData['success'] = true;
				
			} else {
				responseData['success'] = false;
				responseData['err'] = 'There is not enough new server data';
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

/**
 * This method updates a server
 */
app.put('/update_server', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'UPDATE_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Get updated data and the existing server
			var updatedServer = request.body;
			var oldServer = app.models.servers[updatedServer['id']];
			
			if(oldServer){
				//Make sure the server isn't running before we update
				if(!shared.get('child' + oldServer['id'])){
					//Grab old data that will need to be used
					var oldFsSafeName = oldServer['server_name'].replace(/\W/g, '');	
					var oldName = oldServer['server_name'];
					var oldPort = oldServer['port'];
					
					//Update with specified data
					for(index in updatedServer){
						oldServer[index] = updatedServer[index];
					}

					//Grab new data that will need to be used
					var newFsSafeName = oldServer['server_name'].replace(/\W/g, '');
					var newName = oldServer['server_name'];
					var newPort = oldServer['port'];
					
					//Output all servers
					fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
					
					var currDir = process.cwd();
					
					//Navigate to the worlds folder
					if(fs.existsSync('worlds')){
						process.chdir('worlds');
						
						//Navigate to the server-specific folder
						if(fs.existsSync(oldServer['id'])){
							process.chdir(oldServer['id']);
							
							//If the world-name folder exists, rename it
							if(fs.existsSync(oldFsSafeName)){
								fs.renameSync(oldFsSafeName, newFsSafeName);
							}
							
							//Open the server.properties, and make sure all data is accurate
							var serverProps = fs.readFileSync('server.properties', { encoding : 'utf8' });
							var serverProps = serverProps.replace(oldFsSafeName, newFsSafeName);
							var serverProps = serverProps.replace(oldName, newName);
							var serverProps = serverProps.replace('server-port=' + oldPort, 'server-port=' + newPort);
							
							fs.writeFileSync('server.properties', serverProps);
						}
					}
					
					process.chdir(currDir);
					
					//Make sure we have the correct minecraft_server.jar file
					getServer(oldServer);
					
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

/**
 * This method deletes a server
 */
app.delete('/delete_server', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'DELETE_SERVERS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Get server to delete, and find it in the list
			var deleteServer = request.body;
			var oldServer = app.models.servers[deleteServer['id']];
			
			if(oldServer){
				//Delete the server from the list and output to file
				delete app.models.servers[oldServer['id']];
				fs.writeFileSync('models/servers.json', JSON.stringify(app.models.servers));
				
				var currDir = process.cwd();
				
				//Navigate to the worlds folder
				if(fs.existsSync('worlds')){
					process.chdir('worlds');
					
					//Recursively delete the server-specific folder
					if(fs.existsSync(oldServer['id'])){
						fs.rmdirSyncRec(oldServer['id']);
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

/**
 * This method restarts a server
 */
app.post('/restart_server', function(request, response, next){
});

/**
 * This method returns a list of versions
 */
app.get('/versions', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'GET_VERSIONS'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Set the list of versions
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

/**
 * This method changes the port of a server
 */
app.post('/change_port', function(request, response, next){
});

/**
 * This method returns the console history of a server
 */
app.post('/server_history', function(request, response, next){
	var responseData = {};
	
	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;
		
		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_HISTORIES'){
				isAllowed = true;
			}
		}
	
		if(isAllowed){
			//Get server info
			var server = request.body;
			
			//If there is a history for the id, set it
			if(shared.get('history' + server['id'])){				
				responseData['id'] = server['id'];
				responseData['success'] = true;
				responseData['history'] = shared.get('history' + server['id']);
			} else {
				responseData['success'] = false;
				responseData['err'] = 'Server is not running or has no history';
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

/**
 * This method will clear a notification
 */
app.post('/clear_notification', function(request, response, next){
});