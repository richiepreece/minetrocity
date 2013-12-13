/*
 * Author: Richie Preece
 * Email:  richie@minetrocity.com
 * Copyright 2013 - Minetrocity
 * ALL RIGHTS RESERVED
 */

var shared = require('../shared.js');
var url = require('url');
var https = require('https');
var hash = require('password-hash');
var uuid = require('node-uuid');
var fs = require('fs');
var request = require('request');

module.exports = function(app){
  checkFolders();
  getVersions();
  setInterval(getVersions, 1000 * 60 * 60 * 3); //Check every 3 hours for updated versions
}

function checkFolders(versions){
  console.log('Checking for folders.');

  if(!fs.existsSync('models')){
    console.log('Making models folder');
    fs.mkdirSync('models');
  }

  if(fs.existsSync('models/org.json')){
    shared.set('org', JSON.parse(fs.readFileSync('models/org.json')));
  } else {
    console.log('Making org.json file');
    var root = {
      id: uuid.v4(),
      name: 'root',
      parent: null,
      children: []
    };
    shared.set('org', { 'root': root });
    shared.set('root', root);
    fs.writeFileSync('models/org.json', JSON.stringify(shared.get('org'), null, '  '));
  }

  if(fs.existsSync('models/users.json')){
    shared.set('users', JSON.parse(fs.readFileSync('models/users.json')));
  } else {
    console.log('Making users.json file');
    var admin = {
      id: uuid.v4(),
      username: 'admin',
      password: hash.generate('admin'),
      email: 'no_reply@minetrocity.com',
      org: shared.get('root').id
    };
    shared.set('users', { 'admin' : admin });
    fs.writeFileSync('models/users.json', JSON.stringify(shared.get('users'), null, '  '));
  }

  if(fs.existsSync('models/servers.json')){
    shared.set('servers', JSON.parse(fs.readFileSync('models/servers.json')));
  } else {
    shared.set('servers', {});
    fs.writeFileSync('models/servers.json', JSON.stringify(shared.get('servers'), null, '  '));
  }

  //Make sure a worlds folder exists
  if(!fs.existsSync('worlds')){
    fs.mkdirSync('worlds');
  }
}

/**
 * This method gets the versions.json file from the mojang servers
 */
function getVersions(){
  //Location of the versions.json file
  var mojangVersionUrl = 'https://s3.amazonaws.com/Minecraft.Download/versions/versions.json';

  request(mojangVersionUrl, function(err, res, body){
    body = JSON.parse(body);

    if(!fs.existsSync('versions')){
      fs.mkdirSync('versions');
    }

    fs.writeFileSync('versions/versions.json', JSON.stringify(body, null, '  '));
    shared.set('versions', body);
    defaultprops.version.possible = body.versions;
    shared.set('serverproperties', defaultprops);
  });
}

var defaultprops =
{
  "types": {
    "boolean": "checkbox",
    "intlist": "dropdown",
    "stringlist": "dropdown",
    "speclist" : "dropdown",
    "string": "text",
    "number": "number"
  },
  "version" : {
    "title" : "Version",
    "type" : "speclist",
    "possible" : [],
    "default" : {},
    "current" : {},
    "help_text" : "This is the minecraft version that this server will be using."
  },
  "mainproperties" : {
    "motd": {
      "title" : "Server Title",
      "type" : "string",
      "possible" : [],
      "default" : "A Minecraft Server",
      "current" : "A Minecraft Server",
      "help_text" : "This is the message that is displayed in the server list of the client, below the name.\n\tThe MOTD does support color and formatting codes.\n\tIf the MOTD is over 59 characters, the server list will likely report a communication error."
    },
    "server-port": {
      "title": "Server Port",
      "type": "number",
      "possible": [
        1,
        65534
      ],
      "default": 25565,
      "current": 25565,
      "help_text": "Changes the port the server is hosting (listening) on. This port must be forwarded if the server is hosted in a network using NAT (If you have a home router/firewall)."
    }
  },
  "properties": {
    "allow-flight": {
      "title": "Allow Flight",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Allows users to use flight on your server while in Survival mode, if they have a mod that provides flight installed.\nWith allow-flight enabled griefers will possibly be more common, because it will make their work easier. In Creative mode this has no effect.\n\tfalse - Flight is not allowed (players in air for at least 5 seconds will be kicked).\n\ttrue - Flight is allowed, and used if the player has a fly mod installed."
    },
    "allow-nether": {
      "title": "Allow Nether",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Allows players to travel to the Nether.\n\tfalse - Nether portals will not work.\n\ttrue - The server will allow portals to send players to the Nether."
    },
    "difficulty": {
      "title": "Difficulty",
      "type": "intlist",
      "possible": [
        "Peaceful",
        "Easy",
        "Normal",
        "Hard"
      ],
      "default": 1,
      "current": 1,
      "help_text": "Defines the difficulty (such as damage dealt by mobs and the way hunger and poison affects players) of the server.\n\t0 - Peaceful\n\t1 - Easy\n\t2 - Normal\n\t3 - Hard"
    },
    "enable-query": {
      "title": "Enable Query",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Enables GameSpy4 protocol server listener. Used to get information about server."
    },
    "enable-rcon": {
      "title": "Enable Remote Console",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Enables remote access to the server console."
    },
    "enable-command-block": {
      "title": "Enable Command Blocks",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Enables command blocks\nThis option is not generated when you start the server, it only gets generated when you try to use a command block."
    },
    "force-gamemode": {
      "title": "Force Game Mode",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Force players to join in the default gamemode.\n\tfalse - Players will join in the gamemode they left in.\n\ttrue - Players will always join in the default gamemode."
    },
    "gamemode": {
      "title": "Game Mode",
      "type": "intlist",
      "possible": [
        "Survival",
        "Creative",
        "Adventure"
      ],
      "default": 0,
      "current": 0,
      "help_text": "Defines the mode of gameplay.\n\t0 - Survival\n\t1 - Creative\n\t2 - Adventure"
    },
    "generate-structures": {
      "title": "Generate Structures",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Defines whether structures (such as villages) will be generated.\n\tfalse - Structures will not be generated in new chunks.\n\ttrue - Structures will be generated in new chunks.\nNote: Dungeons and Nether Fortresses will still generate if this is set to false."
    },
    "generator-settings": {
      "title": "Generator Settings",
      "type": "string",
      "possible": [],
      "default": "",
      "current": "",
      "help_text": "The settings used to customize Superflat world generation. See Superflat for possible settings and examples."
    },
    "hardcore": {
      "title": "Hardcore Mode",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "If set to true, players will be permanently banned if they die."
    },
    "level-name": {
      "title" : "Level Name",
      "type" : "string",
      "possible" : [],
      "default" : "world",
      "current" : "world",
      "help_text" : "The \"level-name\" value will be used as the world name and its folder name. You may also copy your saved game folder here, and change the name to the same as that folder's to load it instead.\n\tCharacters such as ' (apostrophe) may need to be escaped by adding a backslash before them."
    },
    "level-seed": {
      "title": "Level Seed",
      "type": "string",
      "possible": [],
      "default": "",
      "current": "",
      "help_text": "Add a seed for your world, as in Singleplayer.\n\tSome examples are: minecraft, 404, 1a2b3c"
    },
    "level-type": {
      "title": "Level Type",
      "type": "stringlist",
      "possible": [
        "DEFAULT",
        "FLAT",
        "LARGEBIOMES"
      ],
      "default": "DEFAULT",
      "current": "DEFAULT",
      "help_text": "Determines the type of map that is generated.\n\tDEFAULT - Standard world with hills, valleys, water, etc.\n\tFLAT - A flat world with no features, meant for building.\n\tLARGEBIOMES - Same as default but all biomes are larger."
    },
    "max-build-height": {
      "title": "Max Build Height",
      "type": "number",
      "possible": [],
      "default": 256,
      "current": 256,
      "help_text": "The maximum height in which building is allowed. Terrain may still naturally generate above a low height limit."
    },
    "max-players": {
      "title": "Max Players",
      "type": "number",
      "possible": [
        0,
        2147483647
      ],
      "default": 20,
      "current": 20,
      "help_text": "The maximum number of players that can play on the server at the same time. Note that if more players are on the server it will use more resources. Note also, op player connections are not supposed to count against the max players, but ops currently cannot join a full server. Extremely large values for this field result in the client-side user list being broken."
    },
    "online-mode": {
      "title": "Online Mode",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Server checks connecting players against minecraft's account database. Only set this to false if your server is not connected to the Internet. Hackers with fake accounts can connect if this is set to false! If minecraft.net is down or inaccessible, no players will be able to connect if this is set to true. Setting this variable to off purposely is called \"cracking\" a server, and servers that are presently with online mode off are called \"cracked\" servers.\n\ttrue - Enabled. The server will assume it has an Internet connection and check every connecting player.\n\tfalse - Disabled. The server will not attempt to check connecting players."
    },
    "op-permission-level": {
      "title": "Op Permission Level",
      "type": "intlist",
      "possible": [
        1,
        2,
        3,
        4
      ],
      "default": 4,
      "current": 4,
      "help_text": "Sets permission level for ops.\n\t1 - Ops can bypass spawn protection.\n\t2 - Ops can use /clear, /difficulty, /effect, /gamemode, /gamerule, /give, and /tp, and can edit command blocks.\n\t3 - Ops can use /ban, /deop, /kick, and /op.\n\t4 - Ops can use /stop."
    },
    "pvp": {
      "title": "Person vs. Person",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Enable PvP on the server. Players shooting themselves with arrows will only receive damage if PvP is enabled.\n\tNote: Indirect damage sources spawned by players (such as lava, fire, TNT and to some extent water, sand and gravel) will still deal damage to other players.\n\ttrue - Players will be able to kill each other.\n\tfalse - Players cannot kill other players (also known as Player versus Environment (PvE))."
    },
    "query.port": {
      "title": "Query Port",
      "type": "number",
      "possible": [
        1,
        65534
      ],
      "default": 25565,
      "current": 25565,
      "help_text": "Sets the port for the query server (see enable-query)."
    },
    "rcon.password": {
      "title": "Remote Console Password",
      "type": "string",
      "possible": [],
      "default": "",
      "current": "",
      "help_text": "Sets the password to rcon."
    },
    "rcon.port": {
      "title": "Remote Console Port",
      "type": "number",
      "possible": [
        1,
        65534
      ],
      "default": 65534,
      "current": 65534,
      "help_text": "Sets the port to rcon."
    },
    "server-ip": {
      "title": "Server IP",
      "type": "string",
      "possible": [],
      "default": "",
      "current": "",
      "help_text": "Set this if you want the server to bind to a particular IP. It is strongly recommended that you leave server-ip blank!\n\tSet to blank, or the IP you want your server to run (listen) on."
    },
    "snooper-enabled": {
      "title": "Snooper Enabled",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Sets whether the server sends snoop data regularly to http://snoop.minecraft.net.\n\tfalse - disable snooping.\n\ttrue - enable snooping."
    },
    "spawn-animals": {
      "title": "Spawn Animals",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Determines if animals will be able to spawn.\n\ttrue - Animals spawn as normal.\n\tfalse - Animals will immediately vanish.\nTip: if you have major lag, turn this off/set to false."
    },
    "spawn-monsters": {
      "title": "Spawn Monsters",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Determines if monsters will be spawned.\n\ttrue - Enabled. Monsters will appear at night and in the dark.\n\tfalse - Disabled. No monsters.\nThis does nothing if difficulty = 0 (peaceful) Unless your difficulty is not set to 0, when a monster can still spawn from a Monster Spawner. Tip: if you have major lag, turn this off/set to false."
    },
    "spawn-npcs": {
      "title": "Spawn Non Player Characters",
      "type": "boolean",
      "possible": [],
      "default": true,
      "current": true,
      "help_text": "Determines if villagers will be spawned.\n\ttrue - Enabled. Villagers will spawn.\n\tfalse - Disabled. No villagers."
    },
    "spawn-protection": {
      "title": "Spawn Protection",
      "type": "number",
      "possible": [],
      "default": 16,
      "current": 16,
      "help_text": "Determines the radius of the spawn protection. Note: Setting this to 0 will not disable spawn protection. 0 will protect the single block at the spawn point. 1 will protect a 3x3 area centered on the spawn point. 2 will protect 5x5, 3 will protect 7x7, etc. This option is not generated on the first server start and appears when the first player joins."
    },
    "texture-pack": {
      "title": "Texture Pack",
      "type": "string",
      "possible": [],
      "default": "",
      "current": "",
      "help_text": "Server prompts client to download texture pack upon join. This link must be a direct link to the actual texture pack .zip file. High-resolution texture packs must be less than or equal to 10,000,000 bytes (approx 9.54MB) in size."
    },
    "view-distance": {
      "title": "View Distance",
      "type": "number",
      "possible": [
        3,
        15
      ],
      "default": 10,
      "current": 10,
      "help_text": "Sets the amount of world data the server sends the client, measured in chunks in each direction of the player (radius, not diameter). It determines the server-side viewing distance. The \"Far\" viewing distance is 16 chunks, sending 1089 total chunks (the amount of chunks that the server will load can be seen in the debug screen). \"Normal\" view distance is 8, for 289 chunks.\n10 is the default/recommended. If you have major lag, reduce this value."
    },
    "white-list": {
      "title": "White List",
      "type": "boolean",
      "possible": [],
      "default": false,
      "current": false,
      "help_text": "Enables a whitelist on the server.\nWith a whitelist enabled, users not on the whitelist will be unable to connect. Intended for private servers, such as those for real-life friends or strangers carefully selected via an application process, for example.\n\tNote - Ops are automatically white listed, and there is no need to add them to the whitelist.\n\tfalse - No white list is used.\n\ttrue - The file white-list.txt is used to generate the white list."
    }
  },
  "udfproperties": {}
};
