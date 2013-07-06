exports.init = function (app) {
  app.middleware.acl = acl;
};

function acl(type) {
  return function (req, res, next) {
    if (req.session && req.session.user && req.session.user.type === type) {
      return next();
    }

    res.redirect('/');
  };
};
