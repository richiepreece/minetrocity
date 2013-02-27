var shared = require('../shared');
var mcServer = require('../mc_server');

exports.init = function (app, io) {
  return; // old controller

  app.get('/stop',
    stopServer(io)
  );

  app.get('/start',
    startServer(io)
  );

  app.get('/restart',
    restartServer(io)
  );

  app.get('/upgrade',
    upgradeServer(io)
  );

};


function stopServer(io) {
  return function (req, res, next) {
    if (res) res.send('');
    console.log('Stopping server');

    var child = shared.get('child');
    var input = shared.get('input');

    if (child !== null) {
      //If server is running, send it the stop command
      console.log("Got stop command");
      input.write("/stop\n");
    } else {
      //If server is not running, then do nothing
      //unless upgrading or restarting
      console.log('Child appears to be NULL');
      console.log(child);
      if (shared.get('upgrading')) {
        mcServer.download_file_httpget();
      }
      if(shared.get('restarting')){
        console.log('RESTART START');
        startServer(io)();
        io.sockets.emit('status', 'running');
        shared.set('restarting', false);
      }
    }
  };
};

function startServer(io) {
  return function (req, res, next) {
    if (res) res.send('');

    var child = shared.get('child');

    //If server is not already running, then proceed
    if (child === null) {
      console.log('Starting server');
      var currDir = process.cwd();
      console.log('We are in ' + currDir);
      process.chdir('server');
      //Change directory to 'server' to keep server files in their correct place
      child = require('child_process').exec('java -Xmx1024M -Xms1024M -jar minecraft_server.jar', function (err, stdout, stderr) {
        console.log('Program closed');
        //Set variables to null

        shared.set('child', null);
        shared.set('output', null);
        shared.set('input', null);

        //alert client
        io.sockets.emit('status', 'stopped');

        //If upgrading, proceed with upgrade
        if (shared.get('upgrading')) {
          download_file_httpget();
        }
        
        //If restarting, proceed with restart
        if (shared.get('restarting')) {
          console.log('RESTART START');
          startServer(io)();
          shared.set('restarting', false);
        }
      });// nogui');

      process.chdir(currDir); 

      shared.set('child', child);
      shared.set('output', child.stderr);
      shared.set('input', child.stdin);

      console.log('emitting status');
      io.sockets.emit('status', 'running');   

      var output = child.stderr;
      //When server returns information, send that to client
      output.on('data', function (data) {
        io.sockets.emit('msg', data);
      });
    }
  };
};

function restartServer(io) {
  return function (req, res, next) {
    res.send('');
    //Notify system of restart
    shared.set('restarting', true);
    console.log('RESTART STOP');
    //Run stop command (will restart when finished)
    stopServer(io)();
  };
};

/*
 * This section will upgrade the minecraft_server.jar file
 */

function upgradeServer(io) {
  return function (req, res, next) {
    res.send('');
    //Notify system of upgrade
    shared.set('upgrading', true);
    //Stop server (will upgrade once stopped)
    stopServer(io)();
  };
};
