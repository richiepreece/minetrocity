var shared = require('../shared');
var mcServer = require('../mc_server');

exports.init = function (app) {
  return; // old controller

  app.post('/ban',
    banPlayer
  );

  app.post('/pardon',
    pardonPlayer
  );

  app.post('/banip',
    banIP
  );

  app.post('/pardonip',
    pardonIP
  );

  app.post('/whitelistadd',
    whiteListAdd
  );

  app.post('/whitelistremove',
    whiteListRemove
  );

  app.get('/whiteliston',
    whiteListOn
  );

  app.get('/whitelistoff',
    whiteListOff
  );

  app.post('/op',
    op
  );

  app.post('/deop',
    deop
  );

  app.post('/cmd',
    cmd
  );

};

function banPlayer(req, res, next) {
  res.send('');
  console.log('Banning user');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {
    data = JSON.parse(data);

    var input = shared.get('input');

    if (input !== null) {
      // ban playerid reason
      input.write("/ban " + data[0] + " " + data[1] + "\n");
    } else {
      // add code to edit banned file if stream isn't open
    }
  });
};

function pardonPlayer(req, res, next) {
  res.send('');
  console.log('Pardoning user');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      // /pardon playerid
      input.write("/pardon " + data + "\n");
    } else {
      //add code to edit banned file if stream isn't open
    }
  });
};

function banIP(req, res, next) {
  res.send('');
  console.log('Banning ip');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {
    data = JSON.parse(data);
    
    var input = shared.get('input');

    if (input !== null) {
      // /ban-ip ip reason
      input.write("/ban " + data[0] + " " + data[1] + "\n");
    } else {
      //add code to edit banned file if stream isn't open
    }
  });
};

function pardonIP(req, res, next) {
  res.send('');
  console.log('Pardoning ip');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      // /pardon-ip playerid
      input.write("/pardon-ip " + data + "\n");
    } else {
      //add code to edit banned file if stream isn't open
    }
  });
};

function whiteListAdd(req, res, next) {
  res.send('');
  console.log('Whitelisting player');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      // /pardon-ip playerid
      input.write("/whitelist add " + data + "\n");
    } else {
      //add code to edit banned file if stream isn't open
    }
  });
};

function whiteListRemove(req, res, next) {
  res.send('');
  console.log('De-whitelisting player');

  var data = '';

  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      // /pardon-ip playerid
      input.write("/whitelist remove " + data + "\n");
    } else {
      //add code to edit banned file if stream isn't open
    }
  });
};


function whiteListOn(req, res, next) {
  res.send('');

  var input = shared.get('input');

  if (input !== null) {
    input.write("/whitelist on\n");
  } else {
    // :)
  }
};

function whiteListOff(req, res, next) {
  res.send('');

  var input = shared.get('input');

  if (input !== null) {
    input.write("/whitelist off\n");
  } else {
    // :)
  }
};

function op(req, res, next) {
  res.send('');
  console.log('Opping user');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      input.write("/op " + data + "\n");
    } else {
      //add code to edit op file if stream isn't open
    }
  });
};

function deop(req, res, next) {
  res.send('');
  console.log('De-opping user');
  
  var data = '';
  
  req.on('data', function (d) {
    data += d;
  });
  
  req.on('end', function () {

    var input = shared.get('input');

    if (input !== null) {
      input.write("/deop " + data + "\n");
    } else {
      //add code to edit op file if stream isn't open
    }
  });
};

function cmd(req, res, next) {
  res.send('');
  console.log('Getting a post command');

  var input = shared.get('input');

  if (input !== null) {
    var data = '';
    
    req.on('data', function (d) {
      data += d;
    });
    
    req.on('end', function () {
      console.log('Got command ' + data);
      input.write(data + "\n");
    });
  } else {
    console.log('Server not running');
  }
  
  console.log('Responding');
};
