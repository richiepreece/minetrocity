exports.init = function (app) {
  app.get('/server',
    app.middleware.isLoggedIn,
    getServer,
    app.middleware.render('servers/index')
  );
};

function getServer(req, res, next) {
  // read the query string to get the right server
  // default to the first one in the list
  next();
};
