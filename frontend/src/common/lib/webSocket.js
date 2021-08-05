var ws = new WebSocket('wss://' + "localhost:8443" + '/groupcall');

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	console.log('state : ',ws.readyState)
	if (ws.readyState == 0)
		ws.onopen = () => ws.send(jsonMessage);
	else
		ws.send(jsonMessage);
}

export {sendMessage, ws};
