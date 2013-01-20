var url = require('url')
  , fs   = require('fs')
  ;

//Where the minecraft_server.jar file is found on the web
var file_url        = 'https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft_server.jar'
  , nop             = function () {}
  , downloadVersion = nop;

//Options
var options = {
  host: url.parse(file_url).host,
  port: 80,
  path: url.parse(file_url).pathname
};

/*
 * This section will ensure that the minecraft_server.jar file exists in the server/ folder
 */

//Make sure the folder exists
var downloadServer = function (cb) {
  cb = cb || nop;

  fs.mkdir('server', function (err, stdout, stderr) {
    if (err) {
      //If the folder exists, ensure the file exists
      fs.readdir('server', function (err, files) {
        if (!err && files.indexOf('minecraft_server.jar') < 0) // If the file doesn't exist, download it
          download_file_httpget(cb);
        else
          cb();
      });
    } else {
      //If the folder doesn't exist, download the file to
      //the newly created folder
      download_file_httpget(cb);
    }
  });
};

//Download the file to the server folder
var download_file_httpget = function (cb) {
  cb = cb || nop;

  //get file name
  var file_name = url.parse(file_url).pathname.split('/').pop();
  //Get dir so we can return
  var currDir = process.cwd();
  console.log('We are in ' + currDir);
  //Change dir and download
  process.chdir('server');
  var file = fs.createWriteStream(process.cwd() + '/' + file_name);
  //return to directory
  process.chdir(currDir);

  //Get file
  http.get(options, function (res) {
    res.on('data', function (data) {
        file.write(data);
    }).on('end', function () {
        file.end();
        //Get version on file
        downloadVersion('version');
        cb();
    });
  });
};

var setVersionFunction = function (func) {
  downloadVersion = func;
};

exports.setVersionFunction = setVersionFunction;
exports.downloadServer = downloadServer;
exports.download_file_httpget = download_file_httpget;