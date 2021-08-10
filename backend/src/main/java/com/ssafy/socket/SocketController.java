package com.ssafy.socket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SocketController {
	
	// 환영 메세지
	@MessageMapping("/hello/room/{roomId}") // 클라이언트에서 /hello 를 통해 메세지 전달
	@SendTo("/sub/greetings/room/{roomId}") // 이쪽으로 결과를 리턴
	public Greeting greeting(HelloMessage message, @DestinationVariable String roomId) throws Exception {
		return new Greeting(HtmlUtils.htmlEscape(message.getName()));
	}

	// 퇴장 메세지
	@MessageMapping("/bye/room/{roomId}")
	@SendTo("/sub/bye/room/{roomId}")
	public Greeting bye(HelloMessage message, @DestinationVariable String roomId) throws Exception {
		return new Greeting(HtmlUtils.htmlEscape(message.getName()));
	}
	
	@MessageMapping("/chat")
	@SendTo("/sub/chat")
	public Chat chat(Chat chat) throws Exception {
	  return new Chat(chat.getRoomId(), chat.getFromName(), chat.getMessage());
	}
	
	// 방 전체 메세지
	@MessageMapping("/chat/room/{roomId}")
	@SendTo("/sub/chat/room/{roomId}")
	public Chat room(Chat chat, @DestinationVariable String roomId) {
	    return new Chat(roomId, chat.getFromName(), chat.getMessage());
	}
	
	// 방에 있는 개인에게 메세지
	@MessageMapping("/chat/room/{roomId}/{userId}")
	@SendTo("/sub/chat/room/{roomId}/{userId}")
	public Chat person(Chat chat, @DestinationVariable String roomId, @DestinationVariable String userId) {
	    return new Chat(roomId, chat.getFromName(), chat.getToName(), chat.getMessage());
	}
	
	// 마피아 투표
	@MessageMapping("/vote/room/{roomId}/{userId}")
	@SendTo("/sub/vote/room/{roomId}/{userId}")
	public Chat vote(Chat chat, @DestinationVariable String roomId, @DestinationVariable String userId) {
		return new Chat(roomId, chat.getFromName(), chat.getToName(), null);
	}

	// 방 폭파 메세지
	@MessageMapping("/leave/{roomId}")
	@SendTo("/sub/leave/{roomId}")
	public Chat leave(Chat chat, @DestinationVariable String roomId) {
		return new Chat(roomId, chat.getFromName(), "leave");
	}

	String[] descs = {"morning",""};

	// 게임 시작 및 1라운드 낮
	@MessageMapping("/game/start/{roomId}")
	@SendTo("/sub/game/start/{roomId}")
	public MafiaChat startGame(MafiaChat chat, @DestinationVariable String roomId) {

		return new MafiaChat();
	}

	@SendTo("/sub/game/start/{roomId}")
	public MafiaChat gameTimer(int round, int desc, @DestinationVariable String roomId) {

		return new MafiaChat();
	}

	/*
	 * 참고자료 정리
	 * 
	 * 기본 틀 코드 : https://hwiveloper.dev/2019/01/10/spring-boot-stomp-websocket/
	 * sts 공식 예제 : https://spring.io/guides/gs/messaging-stomp-websocket/
	 * SendToUser 세션 이용 : https://hyeooona825.tistory.com/89
	 * 메세지 인터셉터 등록 : https://hongjuzzang.github.io/springboot/springboot_stomp2/
	 * Vue 라이브러리 등록 : https://velog.io/@skyepodium/vue-spring-boot-stomp-%EC%9B%B9%EC%86%8C%EC%BC%93
	 * @DestinationVariable, @Payload 관련 코드 : https://stackoverflow.com/questions/27047310/path-variables-in-spring-websockets-sendto-mapping
	 * 
	 * gradle dependcies code : https://www.webjars.org/
	 * 
	 * */
}
