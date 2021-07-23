### Socket 통신

- 소켓 통신 연결 및 메세지 받기

  stompClient.connect로 연결 후, stompClient.subscribe로 구독한다.

```
function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        setConnected(true);
        console.log('Connected: ' + frame);
        /* 콘솔창 결과
         * 	Connected: CONNECTED
            heart-beat:0,0
            version:1.1
         * */

        // 결과를 구독할 url을 적어둔다.
        stompClient.subscribe('/sub/chat', function (chat) {
            // 메세지가 왔을 때, 작동할 기능을 구현한다.	
            showChat(JSON.parse(chat.body));
        });
        
        // 구독할 url들 ...
        
	});
}
```



- 메세지 보내기

  stompClient.send를 이용한다.

```
function sendChat() {
	stompClient.send("/pub/chat", {}, 
			JSON.stringify({
				'roomId': roomNumber, 
				'fromName': $("#name").val(), 
				'message': $("#chatMessage").val()
				}));
}
```



##### 예시

- index.html

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Hello WebSocket</title>

<link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- 
  <link href="/main.css" rel="stylesheet">
  -->


<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>


<script src="/app.js"></script>
<!-- 
  <script src="/sockjs.min.js"></script>
  <script src="/stomp.min.js"></script>
   -->

<style>
body{
	padding:25px;
}
*{
	margin: 5px;
}
</style>
</head>
<body>
	<noscript>
		<h2 style="color: #ff0000">Seems your browser doesn't support
			Javascript! Websocket relies on Javascript being enabled. Please
			enable Javascript and reload this page!</h2>
	</noscript>
	<div id="main-content" class="container">
		<div class="row">
			<div class="col-md-6">
				
				<div class="form-group">
					<label for="name">What is your name?</label> <input type="text" id="name" class="form-control" placeholder="Your name here...">
				</div>
			
				<form class="form-inline">
					<button id="room1" class="btn btn-outline-warning" type="button">room1</button>
					<button id="room2" class="btn btn-outline-warning" type="button">room2</button>
					<button id="room3" class="btn btn-outline-warning" type="button">room3</button>
				</form>
				<br>
				<form class="form-inline">
					<div class="form-group">
						<label for="connect">WebSocket connection:</label>
						<button id="connect" class="btn btn-outline-secondary" type="submit">Connect</button>
						<button id="disconnect" class="btn btn-outline-secondary" type="submit"
							disabled="disabled">Disconnect</button>
					</div>
				</form>
			</div>
			<div class="col-md-6">
				<form class="form-inline">
					<div class="form-group">
						<label for="toName">send to...</label> <input type="text"
							id="toName" class="form-control">
					</div>
					<div class="form-group">
						<label for="message">Input Message</label> <input type="text"
							id="chatMessage" class="form-control" placeholder="message.." />
					</div>
					<button id="votebtn" class="btn btn-outline-secondary" type="button">vote</button>
					<button id="chatRoomSendP" class="btn btn-outline-secondary" type="button">Private Send</button>
					<button id="chatRoomSend" class="btn btn-outline-secondary" type="button">Room Send</button>
					<button id="chatSend" class="btn btn-outline-secondary" type="button">Chat Send</button>
					<button id="send" class="btn btn-outline-secondary" type="submit">Send</button>
					
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="conversation" class="table table-striped">
					<thead>
						<tr>
							<th>Greetings</th>
						</tr>
					</thead>
					<tbody id="greetings">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
```



- app.js

```
/**
 * 
 */

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

```


