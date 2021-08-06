var stompClient = null;
let roomNumber = 1;
let username = 'a';
let toName = 'a';

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  }
  else {
    $("#conversation").hide();
  }
  $("#greetings").html("");
}

function connect() {
  // WebSocketConfig.java에서 최초통신하기 위한 엔드포인트 url을 사용하여 생성자를 만든다.
  var socket = new SockJS('/websocket');
  stompClient = Stomp.over(socket);
  
  // SockJS와 stomp client를 통해 연결을 시도.
  stompClient.connect({}, function (frame) {
	username = $("#name").val();
	
    setConnected(true);
    console.log('Connected: ' + frame);
    /* 콘솔창 결과
     * 	Connected: CONNECTED
		heart-beat:0,0
		version:1.1
     * */
    
    // 결과를 구독할 url을 적어둔다.
    stompClient.subscribe('/sub/greetings', function (greeting) {
      showGreeting(JSON.parse(greeting.body).content);
    });
    
    stompClient.subscribe('/sub/chat', function (chat) {
    	showChat(JSON.parse(chat.body));
    });
    
    stompClient.subscribe('/sub/chat/room/'+roomNumber, function (chat) {
    	showRoomChat(JSON.parse(chat.body));
    });
    
    stompClient.subscribe('/sub/chat/room/'+roomNumber+'/'+username, function (chat) {
    	showRoomChatP(JSON.parse(chat.body));
    });
    
    stompClient.subscribe('/sub/vote/room/'+roomNumber+'/'+username, function (chat) {
    	showVote(JSON.parse(chat.body))
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  // /app/hello로 JSON 파라미터를 메세지 body로 전송.
  stompClient.send("/pub/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
  $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

/* Chat과 관련된 메서드 추가 */
function sendChat() {
	stompClient.send("/pub/chat", {}, 
			JSON.stringify({
				'roomId': roomNumber, 
				'fromName': $("#name").val(), 
				'message': $("#chatMessage").val()
				}));
}

function showChat(chat) {
  $("#greetings").append("<tr><td>" + chat.fromName + " : " + chat.message + "</td></tr>");
}

function sendRoomChat() {
	console.log('방번호: ',roomNumber)
	stompClient.send("/pub/chat/room/"+roomNumber, {}, 
			JSON.stringify({
				'roomId': roomNumber, 
				'fromName': $("#name").val(), 
				'message': $("#chatMessage").val()
				}));
}

function showRoomChat(chat) {
  $("#greetings").append("<tr><td>" + chat.roomId+"번 방 ㅣ "+chat.fromName + " : " + chat.message + "</td></tr>");
}

function sendRoomChatP() {
	stompClient.send("/pub/chat/room/"+roomNumber+"/"+toName, {}, 
			JSON.stringify({
				'roomId': roomNumber, 
				'fromName': $("#name").val(), 
				'toName': $("#toName").val(), 
				'message': $("#chatMessage").val()
				}));
}

function showRoomChatP(chat) {
	$("#greetings").append("<tr><td>" + chat.fromName + " >>> " + chat.toName + "(private) : " +chat.message + "</td></tr>");
}

function sendvote() {
	stompClient.send("/pub/vote/room/"+roomNumber+"/"+toName, {}, 
			JSON.stringify({
				'roomId': roomNumber, 
				'fromName': $("#name").val(), 
				'toName': $("#toName").val(), 
				'message': $("#chatMessage").val()
				}));
}

function showVote(chat) {
	  $("#greetings").append("<tr><td>" + chat.toName + " 표 추가!! </td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $( "#connect" ).click(function() { connect(); });
  $( "#disconnect" ).click(function() { disconnect(); });
  $( "#send" ).click(function() { sendName(); });
  $( "#chatSend" ).click(function(){ sendChat(); }); // 추가
  $( "#chatRoomSend" ).click(function(){ sendRoomChat(); });
  $( "#chatRoomSendP" ).click(function(){ toName = $("#toName").val(); console.log(toName,"에게 전송"); sendRoomChatP(); });
  $( "#votebtn" ).click(function(){ toName = $("#toName").val(); console.log(toName,"에게 투표"); sendvote(); });
  $( "#room1" ).click(function(){ roomNumber = 1; console.log("Change Room ",roomNumber); });
  $( "#room2" ).click(function(){ roomNumber = 2; console.log("Change Room ",roomNumber);});
  $( "#room3" ).click(function(){ roomNumber = 3; console.log("Change Room ",roomNumber); });
});
