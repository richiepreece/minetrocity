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
  getVersions();
  setInterval(getVersions, 1000 * 60 * 60 * 12);

  app.get('/versions', versions);
  app.post('/clear_notification', clearNotification);
};


exports.getVersions = getVersions;
exports.versions = versions;
exports.clearNotification = clearNotification;

/**
 * This method gets the versions.json file from the mojang servers
 */
function getVersions(){
	//Location of the versions.json file
	var mojangVersionUrl = "https://s3.amazonaws.com/Minecraft.Download/versions/versions.json";

	var options = {
		host: url.parse(mojangVersionUrl).host,
		port: 443,
		path: url.parse(mojangVersionUrl).path
	}

	//Get the file
	var request = https.request(options, function(result){
		if(result.statusCode == 200){
			result.on('data', function(data){
				var currDir = process.cwd();

				//Ensure the versions folder exists
				if(!fs.existsSync("versions")){
					fs.mkdirSync("versions");
				}

				process.chdir("versions");

				//Write out to a file for safe keeping
				var file = fs.createWriteStream(process.cwd() + '/versions.json');
				file.write(data);
				file.end();

				process.chdir(currDir);

				//Set the versions data for use (this way we have no IO when versions are requested)
				shared.set('versions', JSON.parse(data));
			});
		}
	});

	request.end();
}

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
