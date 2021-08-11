package com.ssafy.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Timer;
import java.util.TimerTask;

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

	@Autowired
	private SimpMessagingTemplate template;

	String[] descs = {"morning","vote", "night"};
	int[] seconds = {60,20,30};

	// 게임 시작 및 1라운드 낮
	@MessageMapping("/game/start/{roomId}")
	public void startGame(MafiaChat chat, @DestinationVariable String roomId) {
		final int[] round = {0};
		final int[] desc = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("타이머 작동 "+round[0]+", "+desc[0]+", 라운드 수: "+chat.getRound());
				// 진행해야 하는 라운드 수가 끝난 경우 게임이 끝나야 한다.
				if(chat.getRound()+1 == round[0]){
					timer.cancel();
				}else{
					template.convertAndSend("/sub/game/start/"+roomId,gameTimer(round[0], desc[0], roomId));
					if(desc[0] == 2){
						round[0] = round[0]+1;
						desc[0] = 0;
					}else{
						desc[0] = desc[0]+1;
					}
				}
			}
		};
		// 실행된 후 0초뒤, 5초마다 실행
		timer.schedule(tt,0,5000);
	}

	//@SendTo("/sub/game/start/{roomId}")
	public MafiaChat gameTimer(int round, int desc, @DestinationVariable String roomId) {
		//System.out.println("타이머 함수 진입 "+round+", "+desc);
		MafiaChat mc = new MafiaChat();
		mc.setRound(round);
		mc.setDesc(descs[desc]);
		mc.setSecond(seconds[desc]);
		return mc;
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
