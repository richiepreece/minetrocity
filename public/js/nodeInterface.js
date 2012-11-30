$(function(){
	var socket = io.connect();
	var length = 200;
	var loggedIn = false;
	var settings = null;
	console.log('connecting');
	
	function showLogin(){
		$("#login").show(length);
		$("#settingsSection").hide();
		$(".console").hide();
		
		
		$(".leftDiv").hide();
	}
	
	function showConsole(){
		$("#login").hide();
		$("#settingsSection").hide();
		$(".console").show(length);
		
		
		$(".leftDiv").show(length);
		$("#logout").show(length);
		$("#console").show(length);
		$("#settings").show(length);
		
		$("#command")[0].focus();
	}
	
	function showSettings(){
		$("#login").hide();
		$("#settingsSection").show(length);
		$(".console").hide();
		
		
		$(".leftDiv").show(length);
		$("#logout").show(length);
		$("#console").show(length);
		$("#settings").show(length);
	}
	
	$("#login").click(function(){
		loggedIn = true;
		$.get('/status');
		showConsole();
	});
	
	$("#logout").click(function(){
		loggedIn = false;
		showLogin();
	});
	
	$("#settings").click(function(){
		showSettings();
		console.log('Requesting settings');
		$.get('/settings');
	});
	
	$("#console").click(function(){
		showConsole();
	});
	
	socket.on('msg', function(data){
		console.log('message received');
		data = data.replace('<', '&lt;');
		data = data.replace('>', '&gt;');
		$('.output').append('<div class="log">' + data + '</div>');
		
		$(".output").scrollTop($(".output")[0].scrollHeight);
	});
	
	socket.on('status', function(data){
		console.log('Status received ' + data);
		if(loggedIn){
			if(data === 'running'){
				console.log('Setting value to Stop');
				$("#startServer")[0].value = "Stop";
				$("#startStop").show(length);
			} else if(data === 'stopped'){
				console.log('Setting value to Start');
				$("#startServer")[0].value = "Start";
				$("#startStop").show(length);
			}
		}
	});
	
	socket.on('settings', function(data){
		settings = data;
		console.log(settings);
	});
	
	$("#startServer").click(function(){
		if(this.value === "Start"){
			console.log('Starting');
			$.get('/start');
			this.value = "Stop";
		} else if(this.value === "Stop"){
			console.log('Stopping');
			$.get('/stop');
			this.value = "Start";
		}
	});
	
	$("#restartButton").click(function(){
		$.get('/restart');
		$("#restart").hide();
	});
	
	$("#submitCommand").click(function(){
		if($('#command')[0].value.length > 0){
			var cmd = $('#command')[0].value;
			$('#command')[0].value = "";
			
			console.log('Sending command ' + cmd);
			$.post('/cmd', cmd);
		}
	});
	
	$("#command").keyup(function(event){
		if(event.keyCode == 13){			//If enter, then execute click on submit button
			$("#submitCommand").click();
		}
	});
});