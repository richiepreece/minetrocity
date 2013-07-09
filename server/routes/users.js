/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var shared   = require('../shared.js')
  , hash     = require('password-hash')
	, uuid     = require('node-uuid')
	, fs       = require('fs')
	;

module.exports = function (app) {
  app.post('/login', login);
  app.get('/logout', logout);
  app.get('/users', users);
  app.post('/add_user', addUser);
  app.put('/update_user', updateUser);
  app.delete('/delete_user', deleteUser);
};

/**
 * This method logs a user in
 */
function login(request, response, next){
	var responseData = {};

	var userInfo = request.body;
	var user = shared.get('users')[userInfo['username']];

	//If username and password match, we have a valid login
	if(user && user['username'] == userInfo['username'] &&
			hash.verify(userInfo['password'], user['password'])){
		//Set session data
		request.session.user = user;

		responseData['username'] = user['username'];
		responseData['success'] = true;
		responseData['acl'] = user['acl'];
		responseData['notifications'] = shared.get('notifications');
	} else {
		responseData['success'] = false;
		responseData['err'] = 'Username or Password is invalid';
	}

	response.send(responseData);
}

/**
 * This method logs a user out
 */
function logout(request, response, next){
	var responseData = {};

	request.session.destroy(function(){
		responseData['success'] = true;
	});

	response.send(responseData);
}

/**
 * The method returns a list of users
 */
function users(request, response, next){
	var responseData = {};

	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;

		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'VIEW_USERS'){
				isAllowed = true;
			}
		}

		if(isAllowed){
			//Set list of users
			responseData['users'] = shared.get('users');
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
 * This method adds a new user
 */
function addUser(request, response, next){
	var responseData = {};

	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;

		//Check for permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'ADD_USERS'){
				isAllowed = true;
			}
		}

		if(isAllowed){
			var newUser = request.body;

			//Make sure new user data has all of the needed fields
			if(newUser['username'] && newUser['password'] &&
					newUser['email'] && newUser['acl']){
				//Check to make sure username doesn't exist
				if(!shared.get('users')[newUser['username']]){
					//Set user id, hash the password, and add user to list
					newUser['id'] = uuid.v4();
					newUser['password'] = hash.generate(newUser['password']);
					shared.get('users')[newUser['username']] = newUser;

					//Write user file
					fs.writeFileSync(__dirname + '/models/users.json', JSON.stringify(shared.get('users')));

					responseData['id'] = newUser['id'];
					responseData['success'] = true;
				} else {
					responseData['success'] = false;
					responseData['err'] = 'A user exists with that name';
				}
			} else {
				responseData['sucess'] = false;
				responseData['err'] = 'There is not enough new user data';
			}
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
 *	This method updates a user
 */
function updateUser(request, response, next){
	var responseData = {};

	//Checked for a logged in user
	if(request.session.user){
		var isAllowed = false;

		//Check for permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'UPDATE_USERS'){
				isAllowed = true;
			}
		}

		if(isAllowed){
			var updatedUser = request.body;
			var oldUser;

			//Get the user
			for(index in shared.get('users')){
				if(shared.get('users')[index]['id'] == updatedUser['id']){
					oldUser = shared.get('users')[index];
				}
			}

			//If the user exists
			if(oldUser){
				delete shared.get('users')[oldUser['username']];

				//Replace all of the fields specified
				for(index in updatedUser){
					if(index == 'password'){
						oldUser[index] = hash.generate(updatedUser[index]);
					} else {
						oldUser[index] = updatedUser[index];
					}
				}

				//Add updated user and output to file
				shared.get('users')[oldUser['username']] = oldUser;
				fs.writeFileSync('models/users.json', JSON.stringify(shared.get('users')));

				responseData['id'] = updatedUser['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'User does not exist';
			}
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
 * This method deletes a user
 */
function deleteUser(request, response, next){
	var responseData = {};

	//Check for a logged in user
	if(request.session.user){
		var isAllowed = false;

		//Check permissions
		for(index in request.session.user['acl']){
			if(request.session.user['acl'][index] == 'DELETE_USERS'){
				isAllowed = true;
			}
		}

		if(isAllowed){
			var deleteUser = request.body;
			var existingUser;

			//Find user
			for(index in shared.get('users')){
				if(shared.get('users')[index]['id'] == deleteUser['id']){
					existingUser = shared.get('users')[index];
				}
			}

			//If the user exists, then delete and output to file
			if(existingUser && existingUser['id'] == deleteUser['id']){
				delete shared.get('users')[existingUser['username']];

				fs.writeFileSync('models/users.json', JSON.stringify(shared.get('users')));

				responseData['id'] = deleteUser['id'];
				responseData['success'] = true;
			} else {
				responseData['success'] = false;
				responseData['err'] = 'User does not exist';
			}
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
