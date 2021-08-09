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

import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

const serverURL = "/websocket"
let socket = new SockJS(serverURL);
var stompClient = Stomp.over(socket);

function socketConnect() {
	if (!stompClient.connected) {
		socket = new SockJS(serverURL);
		stompClient = Stomp.over(socket);
	}
}

export {sendMessage, ws, stompClient, socketConnect};
