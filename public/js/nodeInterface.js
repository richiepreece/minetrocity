$(function(){
	var socket = io.connect();
	var length = 200;
	var loggedIn = false;
	var upgrading = false;
	var settings = null;
	console.log('connecting');
	
	function showLogin(){
		$("#login").show(length);
		$("#settingsSection").hide();
		$("#cpuGraphSection").hide();
		$(".console").hide();		
		
		$(".leftDiv").hide();
		$(".rightDiv").hide();
	}
	
	function showConsole(){
		$("#login").hide();
		$("#settingsSection").hide();
		$("#cpuGraphSection").hide();
		$(".console").show(length);		
		
		$(".leftDiv").show(length);
		$(".rightDiv").show(length);
		$("#logout").show(length);
		$("#console").show(length);
		$("#settings").show(length);
		$("#cpuGraph").show(length);
		
		$("#command")[0].focus();
	}
	
	function showSettings(){
		$("#login").hide();
		$("#settingsSection").show(length);
		$("#cpuGraphSection").hide();
		$(".console").hide();		
		
		$(".leftDiv").show(length);
		$(".rightDiv").show(length);
		$("#logout").show(length);
		$("#console").show(length);
		$("#settings").show(length);
		$("#cpuGraph").show(length);
	}
	
	function showGraph(){
		$("#login").hide();
		$("#settingsSection").hide();
		$("#cpuGraphSection").show(length);
		$(".console").hide();		
		
		$(".leftDiv").show(length);
		$(".rightDiv").show(length);
		$("#logout").show(length);
		$("#console").show(length);
		$("#settings").show(length);
		$("#cpuGraph").show(length);
	}
	
	function showRestart(){
		$("#restart").show(length);
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
	
	$("#cpuGraph").click(function(){
		showGraph();
	});
	
	$("#submitSettings").click(function(){
		console.log("need to save");
		$("#restart").show(length);
	});
	
	$("#upgradeButton").click(function(){
		console.log("upgrading...");
		$("#startStop").hide();
		$("#upgrade").hide();
		$("#upgrading").show(length);
		$.get('/upgrade');
		upgrading = true;
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
	
	socket.on('upgrade', function(data){
		console.log('Time to upgrade');
		if(loggedIn && data){
			$("#upgrade").show(length);
		}
	});
	
	socket.on('upgraded', function(data){
		console.log('Upgraded');
		if(loggedIn){
			upgrading = false;
			$("#startStop").show(length);
			$("#upgrading").hide();
		}
	});
	
	socket.on('cpustats', function(data){
		if(loggedIn){			
			drawMem(data['totalMem'], data['freeMem']);
			drawAvgCpu(data['cpu']);
			drawCpu(data['cpu']);
			
			$(".rightDiv").show(length);
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
	
	var cpuArray = null;
	
	function drawCpu(cpus)
	{
		if(cpuArray == null){
			cpuArray = new Array();
			
			var i = 0;
			for(curr in cpus){
				cpuArray[i++] = RGraph.array_pad([], 60);
			}
		}
		
		RGraph.Clear(document.getElementById("cpu"));
		RGraph.ObjectRegistry.Clear();

		var line = new RGraph.Line('cpu', cpuArray);
		line.Set('chart.colors', ['black', 'blue', 'green', 'red']);
		line.Set('chart.linewidth', 1);
		line.Set('chart.filled', false);
		line.Set('chart.ymax', 100);
		line.Set('chart.numxticks', 10);
		line.Set('chart.ylabels.count', 3);
		line.Set('chart.title', 'CPU Usage (%)');
		line.Set('chart.labels', ['Now','60s']);
		line.Draw();
		
		for(var i = 0; i < cpuArray.length; i++){
			cpuArray[i] = [cpus['cpu' + (i + 1)]].concat(cpuArray[i]);
			cpuArray[i].pop();
		}
	}
	
	mem = RGraph.array_pad([], 60);
	function drawMem(total, free){
		RGraph.Clear(document.getElementById("mem"));
		RGraph.ObjectRegistry.Clear();
		
		var line = new RGraph.Line('mem', mem);
		line.Set('chart.colors', ['red']);
		line.Set('chart.linewidth', 2);
		line.Set('chart.filled', false);
		line.Set('chart.ymax', 100);
		line.Set('chart.numxticks', 6);
		line.Set('chart.ylabels.count', 3);
		line.Set('chart.title', 'Memory Usage (%)');
		line.Set('chart.labels', ['Now', Math.round((total - free) / (1024 * 1024)) + "/" + Math.round(total / (1024 * 1024)) + " MB", '60s']);
		line.Draw();
		
		mem = [Math.round(100 * ((total - free) / total))].concat(mem)
		mem.pop();
	}
	
	avgCpu = RGraph.array_pad([], 60);
	function drawAvgCpu(cpus){
		RGraph.Clear(document.getElementById("avgCpu"));
		RGraph.ObjectRegistry.Clear();
		
		var cputot = 0;
		var tot = 0;
		
		for(curr in cpus){
			cputot += cpus[curr];
			tot++;
		}
		
		cputot /= tot;
		
		
		var line = new RGraph.Line('avgCpu', avgCpu);
		line.Set('chart.colors', ['red']);
		line.Set('chart.linewidth', 2);
		line.Set('chart.filled', false);
		line.Set('chart.ymax', 100);
		line.Set('chart.numxticks', 6);
		line.Set('chart.ylabels.count', 3);
		line.Set('chart.title', 'Average CPU Usage (%)');
		line.Set('chart.labels', ['Now', cputot + "%", '60s']);
		line.Draw();
		
		avgCpu = [cputot].concat(avgCpu);
		avgCpu.pop();
	}
});