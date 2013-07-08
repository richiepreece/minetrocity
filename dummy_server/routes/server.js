module.exports = function (app) {
  app.get('/servers', serverss);
  app.post('/add_server', addServer);
  app.put('/update_server', updateServer);
  app.delete('/delete_server', deleteServer);

  app.post('/start_server', startServer);
  app.post('/stop_server', stopServer);
  app.post('/restart_server', restartServer);
};

var servers = [];

function serverss(req, res) {
  res.send(servers);
}

function addServer(req, res) {
  var guid = Math.random() * 99999;

  var server = {
    id: guid,
    name: req.body.server_name,
    port: req.body.port,
    version: req.body.version,
    type: req.body.version_type,
    running: false
  };
  servers.push(server);

  var obj = {
    id: guid,
    server_name: req.body.server_name,
    success: true
  };
  res.send(obj);
}

function updateServer(req, res) {
  res.send('ok');
}

function deleteServer(req, res) {
  res.send('ok');
}

function startServer(req, res) {
  res.send('ok');
}

function stopServer(req, res) {
  res.send('ok');
}

function restartServer(req, res) {
  res.send('ok');
}
