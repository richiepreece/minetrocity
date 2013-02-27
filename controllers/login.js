exports.init = function (app) {
  app.post('/login',
    login
  );

  app.post('/logout',
    logout
  );
};

var users = {
  richie: {
    name: 'Richie',
    pass: 'preece',
    type: 'admin'
  },
  scott: {
    name: 'Scott',
    pass: 'biery',
    type: 'admin'
  },
  dallin: {
    name: 'Dallin',
    pass: 'osmun',
    type: 'admin'
  }
};

function login(req, res, next) {
  var user = req.body.user.toLowerCase()
    , pass = req.body.pass.toLowerCase()
    ;

  if (users[user] && users[user].pass === pass) {
    req.session.user = users[user];
    return res.send({ success: true });
  }

  res.send({ success: false, msg: 'Incorrect Username or Password.' });
};

function logout(req, res, next) {
  req.session.destroy();
  res.send({ success: true });
};
