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
  //app.post('/change_port', changePort);
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

  if(request.session.user === undefined) return fail('You are not logged in');

  response.send(shared.get('serverproperties'));
}

function servers(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined) return fail('You are not logged in');
    
  var isAllowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'VIEW_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions.');

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

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'START_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) !== undefined) return fail('The server is already running.');

  var savedServer = shared.get('servers')[server.id];
  var currDir = process.cwd();

  process.chdir('worlds');
  process.chdir(server.id);

  shared.set('child' + server.id, 
      require('child_process').exec('java -Xmx1024M -Xms1024M -jar ../../versions/' +
          savedServer.version.current.type + '/' + savedServer.version.current.id + '/minecraft_server.jar',
          function(err, stdout, stderr){
            delete shared.get('child' + server.id);
            delete shared.get('output' + server.id);
            delete shared.get('input' + server.id);
            delete shared.get('history' + server.id);
          }));

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
    msg : data
  });
}

function stopServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'STOP_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');

  if(shared.get('child' + server.id) === undefined){
    response.send({
      id : server.id,
      success : true
    });
  } else {
    shared.get('input' + server.id).write('/stop\n');

    setTimeout(function(){
      stopServer(request, response, next);
    }, 100);
  }
}

function restartServer(request, response, next){
  function fail(reason){
    response.send({
      success : false,
      err : reason
    });
  }

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'RESTART_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');
  //if(shared.get('child' + server.id) === undefined) return fail('The server isn\'t running.');

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

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'ADD_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var newServer = request.body;
  //TODO: Do validity checks here

  //var fsSafeName = newServer['server_name'].replace(/\W/g, '');
  newServer.id = uuid.v4();

  //Register new server, and write to file
  shared.get('servers')[newServer.id] = newServer;
  fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers')));

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

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'DELETE_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) !== undefined) return fail('The server is running');

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

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'VIEW_HISTORIES'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) === undefined) return fail('The server isn\'t running');

  resonse.send({
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

  if(request.session.user === undefined) return fail('You are not logged in');

  var allowed = false;

  //Check permissions
  for(index in request.session.user['acl']){
    if(request.session.user['acl'][index] == 'COMMAND_SERVERS'){
      isAllowed = true;
    }
  }

  if(!isAllowed) return fail('You do not have the necessary permissions');

  var server = request.body;

  if(server.id === undefined) return fail('You must specify a server id');
  if(shared.get('servers')[server.id] === undefined) return fail('The server doesn\'t exist');
  if(shared.get('child' + server.id) === undefined) return fail('The server isn\'t running');

  shared.get('input' + server.id).write(server.cmd + '\n');

  response.send({
    id : server.id,
    success : true
  });
}

/************************************************************************************************************************************************/

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
  var defaultFile = "";

  for(index in newServer.mainproperties){
    newServer += index + '=' + newServer.mainproperties[index].current + '\n';
  }

  for(index in newServer.properties){
    newServer += index + '=' + newServer.properties[index].current + '\n'; 
  }

  for(index in newServer.udfproperties){
    newServer += index + '=' + newServer.udfproperties[index].current + '\n'; 
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