exports.init = function (app) {
  app.get('/admin',
    app.middleware.isLoggedIn,
    app.middleware.render('admin/index')
  );
};
