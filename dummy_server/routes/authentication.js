module.exports = function (app) {
  app.post('/login', login);
  app.get('/logout', logout);
};

function login(req, res) {
  var user = req.body.username;
  var pass = req.body.password;

  if (user !== 'scott' && user !== 'dallin' && user !== 'richie') {
    var obj = {
      success: false,
      err: 'Invalid Credentials'
    };

    return res.send(obj);
  }

  var obj = {
    username: user,
    success: true,
    acl: [],
    notifications: []
  };

  req.session.user = obj;

  res.send(obj);
}

function logout(req, res) {
  req.session.destroy(
    function () {
      var obj = {
        success: true
      };

      res.send(obj);
    }
  );
}
