/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */
var shared   = require('../shared.js')
  , fs       = require('fs')
  , url      = require('url')
  , https    = require('https')
  ;

module.exports = function (app) {
  app.get('/versions', versions);
  app.post('/clear_notification', clearNotification);
  app.get('/permissions', permissions);
};

/**
 * This method returns a list of versions
 */
function versions(request, response, next){
  var responseData = {};

  //Check for a logged in user
  if(request.session.user){
    var isAllowed = false;

    //Check permissions
    for(index in request.session.user['acl']){
      if(request.session.user['acl'][index] == 'GET_VERSIONS'){
        isAllowed = true;
      }
    }

    if(isAllowed){
      //Set the list of versions
      responseData['versions'] = shared.get('versions');
    } else {
      responseData['sucess'] = false;
      responseData['err'] = 'You do not have the necessary permissions';
    }
  } else {
    responseData['success'] = false;
    responseData['err'] = 'You are not logged in';
  }

  response.send(responseData);
}

/**
 * This method will clear a notification
 */
function clearNotification(request, response, next){
  var responseData = {};

  //Check for a logged in user
  if(request.session.user){
    var isAllowed = false;

    //Check permissions
    for(index in request.session.user['acl']){
      if(request.session.user['acl'][index] == 'CLEAR_NOTIFICATIONS'){
        isAllowed = true;
      }
    }

    if(isAllowed){
      var notification = request.body;

      //If the notification exists, delete it
      if(shared.get('notifications')[notification['id']]){
        delete shared.get('notifications')[notification['id']];

        shared.get('io').sockets.emit({ notification_id : notification['id'] });

        responseData['id'] = notification['id'];
        responseData['success'] = true;
      }
    } else {
      responseData['success'] = false;
      responseData['err'] = 'You do not have the necessary permissions';
    }
  } else {
    responseData['success'] = false;
    responseData['err'] = 'You are not logged in';
  }

  response.send(responseData);
}

function permissions(request, response, next){
  var responseData = {};

  //Check for a logged in user
  if(request.session.user){
    var isAllowed = false;

    //Check permissions
    for(index in request.session.user['acl']){
      if(request.session.user['acl'][index] == 'CLEAR_NOTIFICATIONS'){
        isAllowed = true;
      }
    }

    if(true){
      responseData['permissions'] = shared.get('permissions');
      responseData['success'] = true;
    } else {
      responseData['success'] = false;
      responseData['err'] = 'You do not have the necessary permissions';
    }
  } else {
    responseData['success'] = false;
    responseData['err'] = 'You are not logged in';
  }

  response.send(responseData);
}
