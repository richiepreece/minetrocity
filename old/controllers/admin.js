var LENGTH = 10;

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
    var usersArr = [];
    for (key in app.models.users)
      usersArr.push(app.models.users[key])

    var page = parseInt(req.query.page, 10) || 1
      , max  = usersArr.length
      ;

    usersArr.splice(0, (page - 1) * LENGTH);

    if (usersArr.length > LENGTH)
      usersArr.length = LENGTH;

    res.locals({
      users: usersArr,
      page: page,
      maxEntries: max,
      numPerPage: LENGTH
    });
    next();
  };
};
