//Needed includes
var express = require('express');
var http = require('http');
var io = require('socket.io');
var fs = require('fs');
var url = require('url');
var timers = require('timers');
var os = require('os');
// END INCLUDES

var app = express();
var server = http.createServer(app);
var io = io.listen(server);

server.listen(8080);

app.configure(function(){
	app.use(express.logger({format: ":method :status :url"}));
	app.use(app.router);
	app.use(express.static(__dirname + "/public"));
});

var needToUpgrade = false;
var upgrading = false;
var restarting = false;

//Where the minecraft_server.jar file is found on the web
var file_url = 'https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft_server.jar';
	
//Options
var options = {
	host: url.parse(file_url).host,
	port: 80,
	path: url.parse(file_url).pathname
};

/*
 * This section will ensure that the minecraft_server.jar file exists in the server/ folder
 */

//Make sure the folder exists
fs.mkdir('server', function(err, stdout, stderr) {
    if (err){
		//If the folder exists, ensure the file exists
		fs.readdir('server', function(err, files){
			if(!err && files.indexOf('minecraft_server.jar') < 0){
				//If the file doesn't exist, download it
				download_file_httpget();
			}
		});
	} else {
		//If the folder doesn't exist, download the file to
		//the newly created folder
		download_file_httpget();	
	}
});

//Download the file to the server folder
var download_file_httpget = function() {
	//get file name
	var file_name = url.parse(file_url).pathname.split('/').pop();
	//Get dir so we can return
	var currDir = process.cwd();
	console.log('We are in ' + currDir);
	//Change dir and download
	process.chdir('server');
	var file = fs.createWriteStream(process.cwd() + '/' + file_name);
	//return to directory
	process.chdir(currDir);

	//Get file
	http.get(options, function(res) {
		res.on('data', function(data) {
				file.write(data);
		}).on('end', function() {
				file.end();
				//Get version on file
				downloadVersion('version');
		});
	});
};

// END SERVER INSTALL


/*
 * This section will upgrade the minecraft_server.jar file
 */

app.get('/upgrade', function(request, response, next){
	response.send('');
	//Notify system of upgrade
	upgrading = true;
	//Stop server (will upgrade once stopped)
	stopServer();
});

//END UPGRADE

//Minecraft Server Interface
var child = null;
var output = null;
var input = null;

//This will start the server
function startServer(){
	//If server is not already running, then proceed
	if(child === null){
		console.log('Starting server');
		var currDir = process.cwd();
		console.log('We are in ' + currDir);
		process.chdir('server');
		//Change directory to 'server' to keep server files in their correct place
		child = require('child_process').exec('java -jar minecraft_server.jar');// nogui');
		process.chdir(currDir);
		output = child.stderr;
		input = child.stdin;
		
		//When server returns information, send that to client
		output.on('data', function(data){
			io.sockets.emit('msg', data);
		});
		
		//When server closes...
		child.on('close', function(){
			console.log('Program closed');
			//Set variables to null
			child = null;
			output = null;
			input = null;
			
			//alert client
			io.sockets.emit('status', 'stopped');
			
			//If upgrading, proceed with upgrade
			if(upgrading){
				download_file_httpget();
			}
			
			//If restarting, proceed with restart
			if(restarting){
				console.log('RESTART START');
				startServer();
				io.sockets.emit('status', 'running');
				restarting = false;
			}
		});
	}
}

//END SERVER INTERFACE

//Stop Server
function stopServer(){
	if(child !== null){
		//If server is running, send it the stop command
		console.log("Got stop command");
		input.write("/stop\n");
	} else {
		//If server is not running, then do nothing
		//unless upgrading or restarting
		console.log('Child appears to be NULL');
		console.log(child);
		if(upgrading){
			download_file_httpget();
		}
		if(restarting){
			console.log('RESTART START');
			startServer();
			io.sockets.emit('status', 'running');
			restarting = false;
		}
	}
}

//When receiving stop command
app.get('/stop', function(request, response, next){
	response.send('');
	console.log('Stopping server');
	//Run stop server function
	stopServer();
});

//END STOP SERVER

//START SERVER
app.get('/start', function(request, response, next){
	response.send('');
	//Run start command
	startServer();	
});

//END START SERVER

//RESTART
app.get('/restart', function(request, response, next){
	response.send('');
	//Notify system of restart
	restarting = true;
	
	console.log('RESTART STOP');
	//Run stop command (will restart when finished)
	stopServer();
});

//END RESTART

//Status request
app.get('/status', function(request, response, next){
	response.send('');
	
	if(child !== null){
		//If running, report
		io.sockets.emit('status', 'running');
	} else {
		//If stopped, report
		io.sockets.emit('status', 'stopped');
	}
});

//END STATUS

app.get('/settings', function(request, response, next){
	response.send('');
	
	//Read in settings file
	fs.readFile('server/server.properties', 'utf-8', function(err, data){
		if(err){
			console.log('server.properties doesn\'t exist');
			io.sockets.emit('settings', null);
			return;
		}
		console.log('server.properties exists, reading in...');		
		var settings = data.split('\n');
	
		for(var i = 0; i < settings.length; i++){
			console.log(settings[i]);
			
			if(settings[i] !== '' && settings[i].indexOf('#') == -1){
				settings[i] = settings[i].split('=');
				for(var j = 0; j < settings[i].length; j++){
					settings[i][j] = settings[i][j].replace('\r', '');
				}
			} else {
				console.log('Removing ' + settings[i]);
				settings.splice(i--, 1);
			}
		}
		
		console.log(settings);
		io.sockets.emit('settings', settings);
	});
});

app.post('/settings', function(request, response, next){
	response.send('');
	console.log('Getting posted settings');
	
	var data = '';
	
	request.on('data', function(d){
		data += d;
	});
	
	request.on('end', function(){
		console.log(JSON.parse(data));
	});
});

app.post('/cmd', function(request, response, next){
	response.send('');
	console.log('Getting a post command');
	
	if(input !== null){
		var data = '';
		
		request.on('data', function(d){
			data += d;
		});
		
		request.on('end', function(){
			console.log('Got command ' + data);
			input.write(data + "\n");
		});
	} else {
		console.log('Server not running');
	}
	
	console.log('Responding');
});

timers.setInterval(function(){	
	var toSend = new Object();
	
	toSend['totalMem'] = os.totalmem();
	toSend['freeMem'] = os.freemem();
	toSend['cpu'] = new Object();
	
	var cpus = os.cpus();
	
	for(var i = 0; i < cpus.length; i++){
		var cpu = cpus[i];
		var total = 0;
		
		for(type in cpu.times){
			//console.log("CPU" + (i + 1) + " " + type + " " + cpu.times[type]);
			total += cpu.times[type];
		}
		
		var times = cpu.times;
		var idle = times.idle;
		//console.log("CPU " + (i + 1) + " is " + (100 - Math.round(100 * (idle / total))) + "% used");
		toSend['cpu']['cpu' + (i + 1)] = 100 - Math.round(100 * (idle / total));
	}
	
	//console.log(toSend);
	io.sockets.emit('cpustats', toSend);
}, 1000);

function downloadVersion(file){
	console.log('Getting server version for file ' + file);
	
	if(!file){
		file = 'server';
	}
	
	var options = {
		host: url.parse('http://richiepreece.com/version').host,
		port: 80,
		path: url.parse('http://richiepreece.com/version').pathname
	};

	var file_name = file;
	var currDir = process.cwd();
	console.log('We are in ' + currDir);
	process.chdir('version_info');
	var file = fs.createWriteStream(process.cwd() + '/' + file_name);
	process.chdir(currDir);
	canReadVersion = false;

	http.get(options, function(res) {
		res.on('data', function(data) {
				file.write(data);
		}).on('end', function() {
				file.end();
				
				if(upgrading){
					io.sockets.emit('upgraded');
					upgrading = false;
				} else {
					checkUpdateStatus();
				}
		});
	});
}

timers.setInterval(downloadVersion, 1000 * 60);// * 60 * 60);

function checkUpdateStatus(){
	console.log('Checking for update');
	
	var version;
	var server_version;
	
	fs.readFile('version_info/version', 'utf-8', function(err, data){
		if(err){
			return;
		}	
		version = data.split('\n');
	
		for(var i = 0; i < version.length; i++){			
			if(version[i] !== ''){
				version[i] = version[i].split('=');
				for(var j = 0; j < version[i].length; j++){
					version[i][j] = version[i][j].replace('\r', '');
					version[i][j] = version[i][j].split('.');
				}
			} else {
				version.splice(i--, 1);
			}
		}
		
		fs.readFile('version_info/server', 'utf-8', function(err, data){
			if(err){
				return;
			}	
			server_version = data.split('\n');
		
			for(var i = 0; i < server_version.length; i++){			
				if(server_version[i] !== ''){
					server_version[i] = server_version[i].split('=');
					for(var j = 0; j < server_version[i].length; j++){
						server_version[i][j] = server_version[i][j].replace('\r', '');
						server_version[i][j] = server_version[i][j].split('.');
					}
				} else {
					server_version.splice(i--, 1);
				}
			}
			
			for(var i = 0; i < version.length; i++){
				if(version[i][0] == 'mc'){
					for(var j = 0; j < version[i][1].length; j++){
						if(version[i][1][j] < server_version[i][1][j]){
							console.log('Time to upgrade');
							io.sockets.emit('upgrade', true);
							needToUpgrade = true;
							return;
						}
					}
				}
			}
			
			console.log('No upgrade needed');
			needToUpgrade = false;
		});
	});	
}
