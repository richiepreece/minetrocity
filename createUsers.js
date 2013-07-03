var fs = require('fs');
var uuid = require('node-uuid');
var hash = require('password-hash');

var users = {};

var currUser = users['richiepreece'] = {};
currUser['id'] = uuid.v4();
currUser['username'] = 'richiepreece';
currUser['password'] = hash.generate('preece');
currUser['email'] = 'richie@minetrocity.com';
currUser['acl'] = ["test1", "test2", "test3"];

var currUser = users['dallinosmun'] = {};
currUser['id'] = uuid.v4();
currUser['username'] = 'dallinosmun';
currUser['password'] = hash.generate('osmun');
currUser['email'] = 'dallin@minetrocity.com';
currUser['acl'] = ["test1", "test2"];

var currUser = users['scottbiery'] = {};
currUser['id'] = uuid.v4();
currUser['username'] = 'scottbiery';
currUser['password'] = hash.generate('biery');
currUser['email'] = 'scott@minetrocity.com';
currUser['acl'] = ["test1"];

fs.writeFileSync('models/users.json', JSON.stringify(users));

var servers = {};

var currServer = servers['test1'] = {};
currServer['id'] = uuid.v4();
currServer['server_name'] = 'test1';
currServer['port'] = 1;
currServer['version'] = '1.6.1';
currServer['version_type'] = 'release';

var currServer = servers['test2'] = {};
currServer['id'] = uuid.v4();
currServer['server_name'] = 'test2';
currServer['port'] = 2;
currServer['version'] = '1.5.1';
currServer['version_type'] = 'release';

var currServer = servers['test3'] = {};
currServer['id'] = uuid.v4();
currServer['server_name'] = 'test3';
currServer['port'] = 3;
currServer['version'] = '1.5.2';
currServer['version_type'] = 'release';

fs.writeFileSync('models/servers.json', JSON.stringify(servers));