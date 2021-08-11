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
	@MessageMapping("/vote/room/{roomId}")
	@SendTo("/sub/vote/room/{roomId}")
	public Chat vote(Chat chat, @DestinationVariable String roomId) {
		return new Chat(roomId, chat.getFromName(), chat.getToName(), null);
	}

	// 방 폭파 메세지
	@MessageMapping("/leave/{roomId}")
	@SendTo("/sub/leave/{roomId}")
	public Chat leave(Chat chat, @DestinationVariable String roomId) {
		return new Chat(roomId, chat.getFromName(), "leave");
	}


	//////////////////////////////////////////// 게임 타이머 로직 ////////////////////////////////////////////

	@Autowired
	private SimpMessagingTemplate template;

	String[] descs = {"morning", "vote", "judge", "finalvote", "night", "result", "end"};
	int[] seconds = {20, 10, 20, 10, 20, 10, 0};


	@MessageMapping("/game/morning/{roomId}")
	public void gameMorning(MafiaChat chat, @DestinationVariable String roomId) {
		// morning인지 vote인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				System.out.println("타이머 작동 "+check[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 아침이다!!
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), check[0], roomId));
				}else if(check[0] == 2){
					// 투표할 시간이다!!
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), check[0]-1, roomId));
				}else if(check[0] == 3){
					// 타이머 끝!! 최후 변론 또는 밤으로 넘어가자
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), descs.length-1, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,10000);
	}

	@MessageMapping("/game/judge/{roomId}")
	public void gameJudge(MafiaChat chat, @DestinationVariable String roomId) {
		// 최후변론(judge)인지 최종투표(finalvote)인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("타이머 작동 "+round[0]+", "+desc[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 최후 변론 시간이다!!
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), 2+check[0], roomId));
				}else if(check[0] == 2){
					// 최종 투표할 시간이다!!
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), 2+check[0]-1, roomId));
				}else if(check[0] == 3){
					// 타이머 끝!! 밤으로 넘어가자
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), descs.length-1, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,10000);
	}

	@MessageMapping("/game/night/{roomId}")
	public void gameNight(MafiaChat chat, @DestinationVariable String roomId) {
		// morning인지 vote인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("타이머 작동 "+round[0]+", "+desc[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 밤이다!!
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), 4+check[0], roomId));
				}else if(check[0] == 2){
					// 결과다!!
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), 4+check[0]-1, roomId));
				}else if(check[0] == 3){
					// 타이머 끝!! 게임 끝 또는 아침으로 넘어가자
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), descs.length-1, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,10000);
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
