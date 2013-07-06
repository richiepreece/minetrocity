var express  = require('express')
  , http     = require('http')
  , https		 = require('https')
  , path     = require('path')
  , fs       = require('fs')
  , io       = require('socket.io')
  , url      = require('url')
  , timers   = require('timers')
  , os       = require('os')
  , shared   = require('./shared')
  , mcServer = require('./mc_server')
  ;

var app    = express()
  , server = http.createServer(app)
  , io     = io.listen(server)
  ;

shared.set('needToUpgrade', false);
shared.set('upgrading', false);
shared.set('restarting', false);
mcServer.setVersionFunction(downloadVersion);
mcServer.downloadServer();

shared.set('child', null);
shared.set('output', null);
shared.set('input', null);

var sessOptions = {
  key: 'minetrocity.sid',
  secret: "lyYw/^uWM rnZgEr6mt?v8]%|o,|%,|X9O<0K:nJt^wur^k2n&7j>df8zs7/xfsP"
};

app.models = {};
app.models.users = JSON.parse(fs.readFileSync('models/users.json'));

(function generateTestData() {
  for (var i = 0; i < 100; ++i) {
    var rand = Math.floor(Math.random() * 100000);
    app.models.users["testUser" + rand] = {
      "name":  "TestUser" + rand,
      "pass":  "test",
      "email": "test@minetrocity.com",
      "type":  "member"
    };
  };
}());

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

function initMiddlewares(path) {
  app.middleware = {};
  initFiles(path, 'middlewares');
};

function initControllers(path) {
  initFiles(path, 'controllers');
};

function initFiles(path, type) {
  path = path || __dirname + '/' + type;
  var files = fs.readdirSync(path);
  console.info('Loading ' + type + ' for path ' + path);

  files.forEach(function(file) {
    var fullPath = path + '/' + file;
    var stats = fs.statSync(fullPath);

    if (stats.isFile()) {
      initFile(fullPath, type);
    } else if (stats.isDirectory()) {
      initFiles(fullPath, type);
    }
  });
};

function initFile(file, type) {
  var match = /^(.*?\/([A-Za-z_]*))\.js$/.exec(file);
  if (!match) return;
  var asset = require(file);
  if (asset && typeof asset.init === 'function') {
    console.info('    Loading ' + type + ' ' + match[2] + ' (' + file + ')');
    asset.init(app, io);
  }
};

initMiddlewares();
initControllers();

function attachUser(req, res, next) {
  if (req.session && req.session.user)
    res.locals({ user: req.session.user });
  next();
};

server.listen(app.get('port'), function () {
  console.log("Express server listening on port " + app.get('port'));
});

setupTimers();

function setupTimers() {
  //timers.setInterval(calculateCPUStats, 1000);
  timers.setInterval(getVersions, 1000 * 60);// * 60 * 60);
};

function calculateCPUStats() {
  var toSend = {
    totalMem: os.totalmem(),
    freeMem: os.freemem(),
    cpu: {}
  };

  var cpus = os.cpus();
  
  for(var i = 0; i < cpus.length; i++){
    var cpu = cpus[i];
    var total = 0;
    
    for (type in cpu.times) {
      //console.log("CPU" + (i + 1) + " " + type + " " + cpu.times[type]);
      total += cpu.times[type];
    }
    
    var times = cpu.times;
    var idle = times.idle;
    //console.log("CPU " + (i + 1) + " is " + (100 - Math.round(100 * (idle / total))) + "% used");
    toSend.cpu['cpu' + (i + 1)] = 100 - Math.round(100 * (idle / total));
  }
  
  //console.log(toSend);
  io.sockets.emit('cpustats', toSend);
};

/*********************************
* New versioning stuff found here
*********************************/

var versions = null;

function getServer(ver, rel){	
	var serverUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/" + ver + "/minecraft_server." + ver + ".jar";
	
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
	
	if(!fs.existsSync(rel)){
		fs.mkdirSync(rel);
	}
	
	process.chdir(rel);
	
	if(!fs.existsSync(ver)){
		fs.mkdirSync(ver);
	}
	
	process.chdir(ver);
	
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
				
				versions = JSON.parse(data);
				
				/*For testing only...
				var dir = "versions/";
				
				versionList = versions['versions'];
				
				for(curr in versionList){
					getServer(versionList[curr]['id'], versionList[curr]['type']);
				}
				
				//End testing only*/
			});
		}
	});
	
	request.end();
	
	request.on('error', function(error){
		console.error(error);
	});
}

getVersions();