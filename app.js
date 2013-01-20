var express  = require('express')
  , http     = require('http')
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
  key: 'myApp.sid',
  secret: "secret-key-goes-here"
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

server.listen(app.get('port'), function () {
  console.log("Express server listening on port " + app.get('port'));
});



setupTimers();

function setupTimers() {
  timers.setInterval(calculateCPUStats, 1000);
  timers.setInterval(downloadVersion, 1000 * 60);// * 60 * 60);
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

function downloadVersion(file) {
  console.log('Getting server version for file ' + file);
  
  if (!file)
    file = 'server';

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
  // canReadVersion = false; // What is this?

  http.get(options, function (res) {
    res.on('data', function (data) {
        file.write(data);
    }).on('end', function () {
        file.end();
        
        if (shared.get('upgrading')) {
          io.sockets.emit('upgraded');
          shared.set('upgrading', false);
        } else {
          checkUpdateStatus();
        }
    });
  });
};

function checkUpdateStatus() {
  console.log('Checking for update');
  
  var version;
  var server_version;
  
  fs.readFile('version_info/version', 'utf-8', function (err, data) {
    if (err) return;

    version = data.split('\n');
  
    for (var i = 0; i < version.length; i++) {
      if (version[i] !== '') {
        version[i] = version[i].split('=');
        for (var j = 0; j < version[i].length; j++) {
          version[i][j] = version[i][j].replace('\r', '');
          version[i][j] = version[i][j].split('.');
        }
      } else {
        version.splice(i--, 1);
      }
    }

    fs.readFile('version_info/server', 'utf-8', function (err, data) {
      if (err) return;

      server_version = data.split('\n');

      for (var i = 0; i < server_version.length; i++) {
        if (server_version[i] !== '') {
          server_version[i] = server_version[i].split('=');
          for (var j = 0; j < server_version[i].length; j++) {
            server_version[i][j] = server_version[i][j].replace('\r', '');
            server_version[i][j] = server_version[i][j].split('.');
          }
        } else {
          server_version.splice(i--, 1);
        }
      }

      for (var i = 0; i < version.length; i++) {
        if (version[i][0] == 'mc') {
          for (var j = 0; j < version[i][1].length; j++) {
            if (version[i][1][j] < server_version[i][1][j]) {
              console.log('Time to upgrade');
              io.sockets.emit('upgrade', true);
              shared.set('needToUpgrade', true);
              return;
            }
          }
        }
      }

      console.log('No upgrade needed');
      shared.set('needToUpgrade', false);
    });
  }); 
};
