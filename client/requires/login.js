
$('.user').focus();
var t = null;

$('.login').click(function (e) {
  login();
});

$('.user, .pass').keypress(function (e) {
  if (e.keyCode === 13)
    login();
});

function login() {
  var user = $('.user').val()
    , pass = $('.pass').val()
    ;

  if (user === '' || pass === '')
    return showMessage('error', 'You must enter a username and password.');

  showMessage('ok', 'Validating...');

  $.post('/login', { user: user, pass: pass }, function (data) {
    if (!data.success)
      return showMessage('error', data.msg);

    window.location.reload();
  });
};

function showMessage(type, msg) {
  $('.message').text(msg);
  $('.message').removeClass('fadeout');
  clearTimeout(t);
  t = setTimeout(function () { $('.message').addClass('fadeout'); }, 2000)

  if (type === 'error') {
    $('.message').addClass('error');
    $('.user').focus();
  } else {
    $('.message').removeClass('error');
  }
};
