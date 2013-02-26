exports.init = function (app) {
  app.post('/login',
    login
  );

  app.post('/logout',
    logout
  );
};

var users = {
  richie: 'preece',
  scott:  'biery',
  dallin: 'osmun'
};

function login(req, res, next) {
  var user = req.body.user.toLowerCase()
    , pass = req.body.pass.toLowerCase()
    ;

  if (users[user] && users[user] === pass) {
    req.session.user = {
      name: user
    };
    return res.send({ success: true });
  }

  res.send({ success: false, msg: 'Incorrect Username or Password.' });
};

function logout(req, res, next) {
  req.session.destroy();
  res.send({ success: true });
};
