module.exports = function (app) {
  app.get('/versions', versions);
  app.post('/change_port', changePort);
  app.post('/server_history', serverHistory);
  app.post('/clear_notification', clearNotification);
};

function versions(req, res) {
  res.send('ok');
}
function changePort(req, res) {
  res.send('ok');
}
function serverHistory(req, res) {
  res.send('ok');
}
function clearNotification(req, res) {
  res.send('ok');
}
