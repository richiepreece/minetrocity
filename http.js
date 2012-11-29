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

//When a user requests the server to start, execute this
app.get('/start', function(request, response, next){
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
	
	response.send('');
});

app.get('/stop', function(requesst, response, next){
	console.log("Got stop command");
	input.write("/stop\n");
	
	child = null;
	output = null;
	input = null;
	response.send('');
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

timers.setInterval(function(){
	if(child !== null){
		io.sockets.emit('status', 'running');
	} else {
		io.sockets.emit('status', 'stopped');
	}
}, 10000);
