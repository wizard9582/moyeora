var ws = new WebSocket('wss://' + "localhost:8443" + '/groupcall');

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	ws.onopen = () => ws.send(jsonMessage);
}

export {sendMessage, ws};
