var shared   = require('./shared.js')
	, uuid     = require('node-uuid')
	, fs       = require('fs')
	, url      = require('url')
	, https    = require('https')
	;

exports.servers = servers;
exports.startServer = startServer;
exports.stopServer = stopServer;
exports.addServer = addServer;
exports.updateServer = updateServer;
exports.deleteServer = deleteServer;
exports.restartServer = restartServer;
exports.serverHistory = serverHistory;
exports.changePort = changePort;

/**
 * This method returns the list of servers
 */
function servers(request, response, next){
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
			responseData['servers'] = shared.get('servers');
		} else {
			responseData['sucess'] = false;
			responseData['err'] = 'You do not have the necessary permissions';
		}
	} else {
		responseData['success'] = false;
		responseData['err'] = 'You are not logged in';
	}
	
	response.send(responseData);
}

/**
 * This method starts a server
 */
function startServer(request, response, next){
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
			var server = shared.get('servers')[serverToStart['id']];
			
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
}

/**
 * This method stops a server
 */
function stopServer(request, response, next){
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
				shared.get('input' + server['id']).write('/stop\n');	
				
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
}

/**
 * This method adds a server
 */
function addServer(request, response, next){
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
				shared.get('servers')[newServer['id']] = newServer;				
				fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));
				
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
}

/**
 * This method updates a server
 */
function updateServer(request, response, next){
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
			var oldServer = shared.get('servers')[updatedServer['id']];
			
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
					fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));
					
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
}

/**
 * This method deletes a server
 */
function deleteServer(request, response, next){
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
			var oldServer = shared.get('servers')[deleteServer['id']];
			
			if(oldServer){
				//Delete the server from the list and output to file
				delete shared.get('servers')[oldServer['id']];
				fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));
				
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
}

/**
 * This method changes the port of a server
 */
function changePort(request, response, next){
}

/**
 * This method restarts a server
 */
function restartServer(request, response, next){
}

/**
 * This method returns the console history of a server
 */
function serverHistory(request, response, next){
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
			
			//Make sure the server exists
			if(shared.get('servers')[server['id']]){			
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
				responseData['err'] = 'The server does not exist';
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
}

/***************************
* HELPER METHODS
***************************/

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