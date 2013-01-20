var fs       = require('fs')
  , shared   = require('../shared')
  , mcServer = require('../mc_server')
  ;

exports.init = function (app, io) {

  app.get('/status',
    getStatus(io)
  );

  app.get('/settings',
    getSettings(io)
  );

  app.post('/settings',
    saveSettings
  );

};


function getStatus(io) {
  return function (req, res, next) {
    res.send('');
    
    if (shared.get('child') !== null) {
      //If running, report
      io.sockets.emit('status', 'running');
    } else {
      //If stopped, report
      io.sockets.emit('status', 'stopped');
    }
  };
};

function getSettings(io) {
  return function (req, res, next) {
    res.send('');
    
    //Read in settings file
    fs.readFile('server/server.properties', 'utf-8', function (err, data) {
      if (err) {
        console.log('server.properties doesn\'t exist');
        io.sockets.emit('settings', null);
        return;
      }
      console.log('server.properties exists, reading in...');
      var settings = data.split('\n');
    
      for (var i = 0; i < settings.length; i++) {
        console.log(settings[i]);
        
        if (settings[i] !== '' && settings[i].indexOf('#') == -1) {
          settings[i] = settings[i].split('=');
          for (var j = 0; j < settings[i].length; j++)
            settings[i][j] = settings[i][j].replace('\r', '');
        } else {
          console.log('Removing ' + settings[i]);
          settings.splice(i--, 1);
        }
      }
      
      console.log(settings);
      io.sockets.emit('settings', settings);
    });
  };
};

function saveSettings(req, res, next) {
  res.send('');
  console.log('Getting posted settings');
  
  var data = '';
  
  req.on('data', function(d){
    data += d;
  });
  
  req.on('end', function(){
    var settings = JSON.parse(data);
    
    var outputString = '';
    
    for (var i = 0; i < settings.length; i++) {
      for (var j = 0; j < settings[i].length; j++) {
        outputString += settings[i][j];
        if (j === 0)
          outputString += '=';
      }

      outputString += "\r\n";
    }

    console.log(outputString);

    fs.writeFile('server/server.properties', outputString, function (err) {
      if (!err)
        console.log('Settings saved.');
    });
  });
};
