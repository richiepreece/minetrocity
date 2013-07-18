/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var shared   = require('../shared.js')
  , uuid     = require('node-uuid')
  , fs       = require('fs')
  , url      = require('url')
  , https    = require('https')
  ;

module.exports = function (app) {
  app.get('/server_template', serverTemplate);
  app.get('/servers', servers);
  app.post('/start_server', startServer);
  app.post('/stop_server', stopServer);
  app.post('/add_server', addServer);
  app.put('/update_server', updateServer);
  app.post('/delete_server', deleteServer);
  app.post('/restart_server', restartServer);
  app.post('/server_history', serverHistory);
  app.post('/command_server', commandServer);
};

function serverTemplate(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');

  response.send(shared.get('serverproperties'));
}

function servers(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'VIEW_SERVERS')) return fail('You do not have the necessary permissions');

  for(index in shared.get('servers')){
    var curr = shared.get('servers')[index];
    if(shared.get('child' + curr.id) !== undefined){
      curr.running = true;
      curr.history = shared.get('history' + curr.id);
    } else {
      curr.running = false;
    }
  }

  response.send({
    servers : shared.get('servers'),
    success : true
  });

  for(index in shared.get('servers')){
    delete shared.get('servers')[index].running;
    delete shared.get('servers')[index].history;
  }
}

function startServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'START_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) !== undefined && shared.get('child' + server.id) !== null) return fail('The server is already running.');

  var savedServer = shared.get('servers')[server.id];
  var currDir = process.cwd();

  process.chdir('worlds');
  process.chdir(server.id);

  shared.set('child' + server.id, 
      require('child_process').exec('java -Xmx1024M -Xms1024M -jar ../../versions/' +
          savedServer.version.current.type + '/' + savedServer.version.current.id + '/minecraft_server.jar',
          function(err, stdout, stderr){
            shared.set('child' + server.id, null);
            shared.set('output' + server.id, null);
            shared.set('input' + server.id, null);
            shared.set('history' + server.id, null);
          }));

  process.chdir(currDir);

  shared.set('output' + server.id, shared.get('child' + server.id).stderr);
  shared.set('input' + server.id, shared.get('child' + server.id).stdin);
  shared.set('history' + server.id, []);


  shared.get('output' + server.id).on('data', function(data){
    shared.get('history' + server.id).push(data);
    shared.get('io').sockets.emit('msg',{
      id : server.id,
      msg : data
    });
  });

  response.send({
    id : server.id,
    success : true
  });
}

function stopServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'STOP_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) === undefined || shared.get('child' + server.id) === null) return fail('The server isn\'t running.');

  shared.get('input' + server.id).write('/stop\n');

  function recursiveStop(){
    if(shared.get('child' + server.id) === undefined || shared.get('child' + server.id) === null){
      response.send({
        id : server.id,
        success : true
      });
    } else {
      setTimeout(recursiveStop, 100);
    }
  }

  setTimeout(recursiveStop, 100);
}

function restartServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'RESTART_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');

  stopServer(request, { send : function(rsp){
    startServer(request, response, next);
  } }, next);  
}

function addServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'ADD_SERVERS')) return fail('You do not have the necessary permissions');

  var newServer = request.body;
  //TODO: Do validity checks here

  //var fsSafeName = newServer['server_name'].replace(/\W/g, '');
  newServer.id = uuid.v4();

  //Register new server, and write to file
  shared.get('servers')[newServer.id] = newServer;
  fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers'), null, '\t'));

  //Get minecraft_server.jar and create server.properties file
  getServer(newServer);
  createWorld(newServer);

  response.send({
    id : newServer.id,
    success : true
  });
}

function deleteServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'DELETE_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) !== undefined && shared.get('child' + server.id) !== null) return fail('The server is running');

  delete shared.get('servers')[server.id];
  fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers'), null, '\t'));
  fs.rmdirSyncRec('worlds/' + server.id);

  response.send({
    id : server.id,
    success : true
  });
}

function serverHistory(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'VIEW_HISTORIES')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) === undefined || shared.get('child' + server.id) === null) return fail('The server isn\'t running');

  response.send({
    id : server.id,
    success : true,
    history : shared.get('history' + server['id'])
  });
}

function commandServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'COMMAND_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) === undefined || shared.get('child' + server.id) === null) return fail('The server isn\'t running');

  shared.get('input' + server.id).write(server.cmd + '\n');

  response.send({
    id : server.id,
    success : true
  });
}

function updateServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

   if(request.session.user === undefined || request.session.user === null) return fail('You are not logged in');
  if(!hasPermission(request.session.user, 'UPDATE_SERVERS')) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined || server.id === null) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined || shared.get('servers')[server.id] === null) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) !== undefined && shared.get('child' + server.id) !== null) return fail('The server is running');

  var oldServer = shared.get('servers')[server.id];

  if(server.version !== undefined){
    oldServer.version = server.version;
  }

  for(index in server.mainproperties){
    oldServer.mainproperties[index] = server.mainproperties[index];
  }

  for(index in server.properties){
    oldServer.properties[index] = server.properties[index];
  }

  for(index in server.udfproperties){
    oldServer.udfproperties[index] = server.udfproperties[index];
  }

  fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers'), null, '\t'));

  createWorld(oldServer);
  getServer(oldServer);

  response.send({
    id : oldServer.id,
    success : true
  });
}

/***************************
* HELPER METHODS
***************************/

function hasPermission(user, perm){
  //Check permissions
  for(index in user.acl){
    if(user.acl[index] === perm){
      return true;
    }
  }

  return false;
}

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
  var defaultFile = "";

  for(index in newServer.mainproperties){
    defaultFile += index + '=' + newServer.mainproperties[index].current + '\n';
  }

  for(index in newServer.properties){
    defaultFile += index + '=' + newServer.properties[index].current + '\n';
  }

  for(index in newServer.udfproperties){
    defaultFile += index + '=' + newServer.udfproperties[index].current + '\n';
  }

  var currDir = process.cwd();

  process.chdir("worlds");

  //Make sure the server id folder exists
  if(!fs.existsSync(newServer.id)){
    fs.mkdirSync(newServer.id);
  }

  process.chdir(newServer.id);

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
    newServer.version.current.id + "/minecraft_server." + 
    newServer.version.current.id + ".jar";

  var options = {
    host: url.parse(serverUrl).host,
    port: 443,
    path: url.parse(serverUrl).pathname
  };

  var currDir = process.cwd();

  process.chdir('versions');

  //Ensure the version type (release/snapshot) folder exists
  if(!fs.existsSync(newServer.version.current.type)){
    fs.mkdirSync(newServer.version.current.type);
  }

  process.chdir(newServer.version.current.type);

  //Ensure the version folder exists
  if(!fs.existsSync(newServer.version.current.id)){
    fs.mkdirSync(newServer.version.current.id);
  }

  process.chdir(newServer.version.current.id);

  //If the minecraft_server.jar file exists, don't re-download it -- it could be running
  if(!fs.existsSync('minecraft_server.jar')){
    var file = fs.createWriteStream(process.cwd() + '/minecraft_server.jar');

    https.get(options, function(result){
      var size = parseInt(result.headers['content-length'], 10);
      var downloaded = 0;

      result.on('data', function(data){
        downloaded += data.length;
        shared.get('io').sockets.emit('download_update',
          { id : newServer.id, finished : false, percentage : (downloaded / size) * 100 });

        file.write(data);
      }).on('end', function(){
        shared.get('io').sockets.emit('download_update',
          { id : newServer.id, finished : true, percentage : 100 });

        file.end();
      });
    });
  }

  process.chdir(currDir);
}