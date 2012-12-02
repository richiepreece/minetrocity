//Needed includes
var express = require('express');
var http = require('http');
var io = require('socket.io');
var fs = require('fs');
var url = require('url');
var timers = require('timers');

var app = express();
var server = http.createServer(app);
var io = io.listen(server);

server.listen(8080);

app.configure(function(){
	app.use(express.logger({format: ":method :status :url"}));
	app.use(app.router);
	app.use(express.static(__dirname + "/public"));
});

/*
var handler = function(request, response, next){
	console.log('sending message');
	io.sockets.emit('msg', 'hello');
	response.send('');		
}

app.get('/send', handler);

io.sockets.on('connection', function(socket){
	console.log('connection received');
});
*/

/*
 * This section will ensure that the minecraft_server.jar file exists in the server/ folder
 */
var file_url = 'https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft_server.jar';

//Make sure the folder exists
fs.mkdir('server', function(err, stdout, stderr) {
    if (err){
		//If it exists, we'll assume that the server exists... for now
	} else {
		download_file_httpget(file_url);	
	}
});

var download_file_httpget = function(file_url) {
	var options = {
		host: url.parse(file_url).host,
		port: 80,
		path: url.parse(file_url).pathname
	};

	var file_name = url.parse(file_url).pathname.split('/').pop();
	var currDir = process.cwd();
	console.log('We are in ' + currDir);
	process.chdir('server');
	var file = fs.createWriteStream(process.cwd() + '/' + file_name);
	process.chdir(currDir);

	http.get(options, function(res) {
		res.on('data', function(data) {
				file.write(data);
		}).on('end', function() {
				file.end();
		});
	});
};

var child = null;
var output = null;
var input = null;

function startServer(){
	if(child === null){
		console.log('Starting server');
		var currDir = process.cwd();
		console.log('We are in ' + currDir);
		process.chdir('server');
		child = require('child_process').exec('java -jar minecraft_server.jar');
		process.chdir(currDir);
		output = child.stderr;
		input = child.stdin;
		
		output.on('data', function(data){
			io.sockets.emit('msg', data);
		});
		
		child.on('close', function(){
			console.log('Program closed');

			child = null;
			output = null;
			input = null;
			
			io.sockets.emit('status', 'stopped');
		});
	}
}

function stopServer(){
	if(child !== null){
		console.log("Got stop command");
		input.write("/stop\n");
	} else {
		console.log('Child appears to be NULL');
		console.log(child);
	}
}

//When a user requests the server to start, execute this
app.get('/start', function(request, response, next){
	response.send('');
	startServer();	
});

app.get('/stop', function(request, response, next){
	response.send('');
	console.log('Stopping server');
	stopServer();
});

app.get('/status', function(request, response, next){
	response.send('');
	
	if(child !== null){
		io.sockets.emit('status', 'running');
	} else {
		io.sockets.emit('status', 'stopped');
	}
});

app.get('/restart', function(request, response, next){
	response.send('');
	
	console.log('RESTART STOP');
	stopServer();
	timers.setTimeout(function(){
		console.log('RESTART START');
		startServer();
		io.sockets.emit('status', 'running');
	}, 2000);
});

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

app.post('/cmd', function(request, response, next){
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
	response.send('');
});
