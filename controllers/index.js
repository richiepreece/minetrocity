exports.init = function (app) {
  app.get('/new',
    app.middleware.isLoggedIn,
    app.middleware.render('index/index')
  );

  app.get('/login',
    app.middleware.isNotLoggedIn,
    app.middleware.render('index/index')
  );
};
