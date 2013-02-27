exports.init = function (app) {
  app.get('/admin',
    app.middleware.isLoggedIn,
    app.middleware.acl('admin'),
    getUsers(app),
    app.middleware.render('admin/index')
  );

  app.get('/admin/servers',
    app.middleware.isLoggedIn,
    app.middleware.acl('admin'),
    app.middleware.render('admin/servers')
  );
};

function getUsers(app) {
  return function (req, res, next) {
    res.locals({
      users: app.models.users
    });
    next();
  };
};
