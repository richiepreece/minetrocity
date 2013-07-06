exports.init = function (app) {
  app.post('/login',
    login(app)
  );

  app.post('/logout',
    logout
  );
};

function login(app) {
  return function (req, res, next) {
    var users = app.models.users;
    var user = req.body.user.toLowerCase()
      , pass = req.body.pass.toLowerCase()
      ;

    if (users[user] && users[user].pass === pass) {
      req.session.user = users[user];
      return res.send({ success: true });
    }

    res.send({ success: false, msg: 'Incorrect Username or Password.' });
  };
};

function logout(req, res, next) {
  req.session.destroy();
  res.send({ success: true });
};
