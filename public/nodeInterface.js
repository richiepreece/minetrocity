$(function(){
	var socket = io.connect();
	var length = 200;
	var loggedIn = false;
	console.log('connecting');
	
	$("#login").click(function(){
		loggedIn = true;
		$("#login").hide();
		//$("#startStop").show(length);
		$(".console").show(length);
		$(".leftDiv").show(length);
		$("#logout").show(length);
	});
	
	$("#logout").click(function(){
		loggedIn = false;
		$("#login").show(length);
		//$("#startStop").show(length);
		$(".console").hide();
		$(".leftDiv").hide();
		$("#logout").hide();
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
	
	$("#submitCommand").click(function(){
		var cmd = $('#command')[0].value;
		
		console.log('Sending command ' + cmd);
		$.post('/cmd', cmd);
	});
});