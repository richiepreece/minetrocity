exports.init = function (app) {
  app.middleware.isLoggedIn = isLoggedIn;
  app.middleware.isNotLoggedIn = isNotLoggedIn;
};

function isLoggedIn(req, res, next) {
  if (req.session && req.session.user) {
    return next();
  }
  res.redirect('/login');
};

function isNotLoggedIn(req, res, next) {
  if (req.session && req.session.user) {
    return res.redirect('/');
  }
  next();
};
