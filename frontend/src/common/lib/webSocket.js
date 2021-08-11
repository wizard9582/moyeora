// var ws = new WebSocket('wss://' + "172.26.11.225:8443" + '/groupcall');
var ws = new WebSocket('wss://' + "localhost:8443" + '/groupcall');

// ws.onopen = function () {
// 	// subscribe to some channels
// 	ws.send(JSON.stringify({
// 		//.... some message the I must send when I connect ....
// 	}))
// };
	
ws.onclose = function(e) {
    console.log('쿠렌토 Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
    setTimeout(function() {
		ws = new WebSocket('wss://' + "localhost:8443" + '/groupcall');
    }, 1000);
  };

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	console.log('state : ',ws.readyState)
	if (ws.readyState != 1)
		ws.onopen = () => ws.send(jsonMessage);
	else
		ws.send(jsonMessage);
}

// import Stomp from 'webstomp-client'
// import SockJS from 'sockjs-client'

// const serverURL = "/websocket"
// let socket = new SockJS(serverURL);
// var stompClient = Stomp.over(socket);
// // function socketConnect() {
// // 	if (!stompClient || !stompClient.connected) {
// // 		stompClient = Stomp.over(socket);
// // 	}
// // }

// function disconnectSocket() {
// 	if (stompClient !== null) {
// 	  stompClient.disconnect();
// 	}
// 	setConnected(false);
// 	console.log("Disconnected");
//   }
  

export {sendMessage, ws};
