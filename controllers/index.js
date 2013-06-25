exports.init = function (app) {
  app.get('/',
    app.middleware.isLoggedIn,
    app.middleware.render('index/dashboard')
  );

  app.get('/login',
    app.middleware.isNotLoggedIn,
    app.middleware.render('index/login')
  );

  app.get('/settings',
    app.middleware.isLoggedIn,
    app.middleware.render('index/settings')
  );
};
