$(function(){
	var socket = io.connect();
	var length = 200;
	var loggedIn = false;
	var upgrading = false;
	var settings = null;
	var selected = document.getElementById('console');
	console.log('connecting');
	
	function clickedItem(item){
		$("#" + selected.id).removeClass('selected');
		$(item).addClass('selected');
		selected = item;
	}
	
	$(".menuItem").click(function(){
		$("#" + selected.id + "Div").hide();
		clickedItem(this);
		$("#" + selected.id + "Div").show();
		
		switch(this.id){
			case 'logout':
				if(loggedIn){
					$("#menu").hide();
					$("#content").width('100%');					
					loggedIn = false;
				} else {
					$("#menu").show();
					$("#content").width('80%');
					$("#console").click();
					loggedIn = true;
				}
			case 'console':
				$("#command")[0].focus();
				break;
			case 'users':
				break;
			case 'settings':
				$.get('/settings');
				break;
			case 'cpu':
				break;
			default:
				break;
		}
	});
	
	$("#loginButton").click(function(){
		$("#logout").click();
	});
	
	$("#startServer").click(function(){
		if(this.value === "Start"){
			console.log('Starting');
			$.get('/start');
		} else if(this.value === "Stop"){
			console.log('Stopping');
			$.get('/stop');
		}
	});
	
	socket.on('status', function(data){
		console.log('Status received ' + data);
		if(loggedIn){
			if(data === 'running'){
				console.log('Setting value to Stop');
				$("#startServer")[0].value = "Stop";
				if(!upgrading){
					$("#startStop").show(length);
				}
			} else if(data === 'stopped'){
				console.log('Setting value to Start');
				$("#startServer")[0].value = "Start";
				if(!upgrading){
					$("#startStop").show(length);
				}
			}
		}
	});
	
	$("#submitCommand").click(function(){
		if($('#command')[0].value.length > 0){
			var cmd = $('#command')[0].value;
			$('#command')[0].value = "";
			
			console.log('Sending command ' + cmd);
			$.post('/cmd', cmd);
		}
		
		$("#command")[0].focus();
	});
	
	$("#command").keyup(function(event){
		if(event.keyCode == 13){			//If enter, then execute click on submit button
			$("#submitCommand").click();
		}
	});
	
	socket.on('msg', function(data){
		console.log('message received');
		data = data.replace('<', '&lt;');
		data = data.replace('>', '&gt;');
		$('.output').append('<div class="log">' + data + '</div>');
		
		$(".output").scrollTop($(".output")[0].scrollHeight);
	});
	
	$("#submitSettings").click(function(){
		console.log("need to save");
		
		$("#restartServer").show();
		var settingsDiv = $("#settingsDiv")[0].childNodes;
		
		var retArray = new Array();
		var currIndex = 0;
		
		for(var i = 1; i < settingsDiv.length; i++){
			var currSetting = settingsDiv[i].childNodes;
			for(var j = 0; j < currSetting.length; j++){
				switch(currSetting[j].tagName){
					case 'SELECT':
						retArray[currIndex] = new Array();
						retArray[currIndex].push(currSetting[j].id);
						retArray[currIndex++].push(currSetting[j].options[currSetting[j].selectedIndex].value);
						break;
					case 'INPUT':
						if(currSetting[j].type !== 'button'){
							retArray[currIndex] = new Array();
							retArray[currIndex].push(currSetting[j].id);
							retArray[currIndex++].push(currSetting[j].value);
						}
						break;
					default:
						//Do nothing
						break;
				}
			}
		}
		$.post('/settings', JSON.stringify(retArray));
	});
	
	socket.on('settings', function(data){
		settings = data;
		
		for(temp in data){
			var curr = data[temp];
			
			var inCurr = new Array();
			
			for(attrib in curr){
				inCurr.push(attrib);
			}
			
			var element = document.getElementById(curr[inCurr[0]] + curr[inCurr[1]]);
			
			if(element == null){
				element = document.getElementById(curr[inCurr[0]]);
				if(element != null){
					element.value = curr[inCurr[1]];
				}
			} else {
				element.selected = true;
			}
		}
	});
	
	$("#restartServer").click(function(){
		$.get('/restart');
		$("#restartServer").hide();
	});
	
	$("#upgradeServer").click(function(){
		console.log("upgrading...");
		$("#startServer").hide();
		$("#upgradeServer").hide();
		$("#upgrading").show();
		$.get('/upgrade');
		upgrading = true;
	});
	
	socket.on('upgrade', function(data){
		console.log('Time to upgrade');
		if(loggedIn){
			$("#upgradeServer").show();
		}
	});
	
	socket.on('upgraded', function(data){
		console.log('Upgraded');
		if(loggedIn){
			upgrading = false;
			$("#startServer").show();
			$("#upgrading").hide();
		}
	});
});