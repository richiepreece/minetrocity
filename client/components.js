$(function () {
  $('.dropdown-title').click(function (e) {
    for (var i = 0; i < $('.dropdown-options').length; ++i)
      if ($('.dropdown-options')[i] !== $(this).closest('.dropdown').find('.dropdown-options')[0])
        $($('.dropdown-options')[i]).hide();

    $(this).closest('.dropdown').find('.dropdown-options').slideToggle(100);

    e.stopPropagation();
    e.preventDefault();
  });

  $('html').click(function (e) {
    $('.dropdown-options').hide();
  });

  $('.logout').click(function (e) {
    $.post('/logout', {}, function () {
      window.location = '/';
    });
  });

  $('.show-settings').click(function (e) {
    window.location = '/settings';
  });

  $('.show-servers').click(function (e) {
    window.location = '/server';
  });

  $('.show-admin').click(function (e) {
    window.location = '/admin';
  });

  $('.main-nav .title').click(function (e) {
    window.location = '/';
  });
});
