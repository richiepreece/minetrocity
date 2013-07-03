module.exports = function (app) {
  app.get('/users', users);
  app.post('/add_user', addUser);
  app.put('/update_user', updateUser);
  app.delete('/delete_user', deleteUser);
};

var users = [];

function users(req, res) {
  res.send(users);
}

function addUser(req, res) {
  var guid = Math.random() * 99999;

  var user = {
    id: guid,
    username: req.body.username,
    password: req.body.password,
    email: req.body.email,
    acl: req.body.acl
  };

  users.push(user);

  var obj = {
    id: guid,
    success: true
  };

  res.send(obj);
}

function updateUser(req, res) {
  var id = req.body.id;

  for (var i = 0; i < users.length; ++i) {
    if (users[i].id === id) {

      users[i].username = req.body.username || users[i].username;
      users[i].password = req.body.password || users[i].password;
      users[i].email = req.body.email || users[i].email;
      users[i].acl = req.body.acl || users[i].acl;

      var obj = {
        id: id,
        success: true
      };
      return res.send(obj);
    }
  }

  var obj = {
    success: false,
    err: 'User not Found'
  };

  res.send(obj);
}

function deleteUser(req, res) {
  var id = req.body.id;

  for (var i = 0; i < users.length; ++i) {
    if (users[i].id === id) {

      users.splice(i, 1);

      var obj = {
        id: id,
        success: true
      };
      return res.send(obj);
    }
  }

  var obj = {
    success: false,
    err: 'User not Found'
  };

  res.send(obj);
}
