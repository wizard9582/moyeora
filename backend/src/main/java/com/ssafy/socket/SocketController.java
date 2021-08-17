package com.ssafy.socket;

import com.ssafy.db.entity.Mafia;
import com.ssafy.db.entity.MatchHistory;
import com.ssafy.db.entity.UserConference;
import com.ssafy.db.repository.MafiaRepository;
import com.ssafy.db.repository.MafiaRepositorySupport;
import com.ssafy.db.repository.MatchHistoryRepositorySupport;
import com.ssafy.db.repository.UserConferenceRepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

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

	@Autowired
	MafiaRepository mafiaRepository;

	@Autowired
	MafiaRepositorySupport mafiaRepositorySupport;

	@Autowired
	UserConferenceRepositorySupport userConferenceRepositorySupport;

	@Autowired
	MatchHistoryRepositorySupport matchHistoryRepositorySupport;

	String[] descs = {"morning", "vote", "judge", "finalvote", "night", "result", "end"};
	int[] seconds = {20, 10, 20, 10, 20, 10, 0};

	final String[][] jobs ={
			{"mafia","police","doctor","citizen","citizen"}, //5명 기준
			{"mafia","police","doctor","citizen","citizen","citizen"}, //6명 기준
			{"mafia","police","doctor","citizen","citizen","citizen","mafia"}, //7명 기준
			{"mafia","police","doctor","citizen","citizen","citizen","mafia","citizen"}, //8명 기준
			{"mafia","mafia","mafia","police","doctor","citizen","citizen","citizen","citizen"}, //9명 기준
			{"mafia","mafia","mafia","police","doctor","citizen","citizen","citizen","citizen","citizen"}, //10명 기준
	};

	// 게임 끝났는지 판단
	@MessageMapping("/game/end/{roomId}")
	public void getGameStatus(@DestinationVariable String roomId, HelloMessage helloMessage) {
		String [] msg = helloMessage.getName().split(",");
		//System.out.println("split 결과: "+Arrays.toString(msg));

		if (msg.length != 1) {
			long userId = Long.parseLong(msg[1]);
			mafiaRepositorySupport.killUser(userId, Long.parseLong(roomId));
		}

		// 전체 방에 있는 사람 리스트
		List<Mafia> allPlayerList = mafiaRepositorySupport.getAllPlayerByRoomId(Long.parseLong(roomId));

		// 살아있는 사람들 리스트
		List<Mafia> playerList = mafiaRepositorySupport.getPlayerByRoomId(Long.parseLong(roomId));
		
		int liveMafiaCnt = 0;
		int liveCitizenCnt =0;

		//현재 생존한 직업별 플레이어 수 세기
		for(Mafia m : playerList){
			if(m.getRole().equals("mafia")) {
				liveMafiaCnt++;
			}else{
				liveCitizenCnt++;
			}
		}

		/*
		gameStatus는 Client에게 전달될 값,
		"on"일 경우는 계속 게임 진행,
		"mafia"일 경우 마피아 승
		"citizen"일 경우 시민 승
		 */
		String gameStatus = "on";

		if(liveMafiaCnt==0){
			gameStatus="citizen";
			mafiaRepositorySupport.deleteMafia(Long.parseLong(roomId));
		}else if(liveMafiaCnt>=liveCitizenCnt){
			gameStatus="mafia";
			mafiaRepositorySupport.deleteMafia(Long.parseLong(roomId));
		}

		if (!gameStatus.equals("on")) {
			for (Mafia m : allPlayerList) {
				// total ++
				long userId = m.getUserConference().getUser().getId();
				MatchHistory matchHistory = matchHistoryRepositorySupport.getMatchHistoryByRole(userId, m.getRole());
				matchHistoryRepositorySupport.updateTotalMatchHistory(matchHistory, userId, m.getRole());
				if ((m.getRole().equals("mafia") && gameStatus.equals("mafia")) || (!m.getRole().equals("mafia") && gameStatus.equals("citizen"))) {
					// win++
					matchHistoryRepositorySupport.updateWinMatchHistory(matchHistory, userId, m.getRole());
				}
			}
		}


		template.convertAndSend("/sub/game/end/"+roomId, msg[0]+","+gameStatus);
	}

	@MessageMapping("/game/morning/{roomId}")
	public void gameMorning(MafiaChat chat, @DestinationVariable String roomId) {
		//직업 분배 시작
		if(chat.getRound()==0){
			Collections.shuffle(Arrays.asList(jobs[0]));
			//System.out.println(Arrays.toString(jobs[0])+", "+jobs[0].length);

			int idx = 0;
			//System.out.println(chat.getUserId()+"가 소켓 통신 중");
			List<UserConference> userConferenceList = userConferenceRepositorySupport.getUserConferenceByRoomNum(roomId);

			Collections.shuffle(Arrays.asList(jobs[userConferenceList.size()-5]));
			//System.out.println(Arrays.toString(jobs[0])+", "+jobs[0].length);
			List<String> mafiaList = new ArrayList<>();

			for(UserConference uc : userConferenceList){
				Mafia mafia = new Mafia();
				mafia.setUserConference(uc);
				mafia.setRole(jobs[userConferenceList.size()-5][idx++]);
				mafia.setStatus(0);
				System.out.println(mafia.getUserConference().getUser().getUserId()+ " => " +mafia.getRole());
				mafiaRepositorySupport.saveRole(mafia);
				String msg = "{\"role\":\""+mafia.getRole()+"\", \"same\":null}";
				if(!mafia.getRole().equals("mafia"))
					template.convertAndSend("/sub/game/start/"+roomId+"/"+mafia.getUserConference().getUser().getUserId(),msg);
				else
					mafiaList.add(mafia.getUserConference().getUser().getUserId());
			}

			for (int i = 0; i < mafiaList.size(); i++) {
				String msg = "{\"role\":\"mafia\", \"same\":\""+mafiaList.toString()+"\"}";
				template.convertAndSend("/sub/game/start/"+roomId+"/"+mafiaList.get(i), msg);
			}
		}
		//직업 분배 끝

		// 2 : 1
		int period = chat.getSecond()/3;
		System.out.println("morning - 타이머 period 설정: "+period);
		// morning인지 vote인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				System.out.println("타이머 작동 "+check[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 아침이다!!
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), check[0], period*2, roomId));
				}else if(check[0] == 2){
					// 투표할 시간이다!!
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), check[0]-1, period, roomId));
				}else if(check[0] == 3){
					// 타이머 끝!! 최후 변론 또는 밤으로 넘어가자
					template.convertAndSend("/sub/game/morning/"+roomId,gameTimer(chat.getRound(), descs.length-1, 0, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,(period*1000));
	}

	@MessageMapping("/game/judge/{roomId}")
	public void gameJudge(MafiaChat chat, @DestinationVariable String roomId) {
		// 1 : 1
		int period = chat.getSecond()/2;
		System.out.println("judge - 타이머 period 설정: "+period);
		// 최후변론(judge)인지 최종투표(finalvote)인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("타이머 작동 "+round[0]+", "+desc[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 최후 변론 시간이다!!
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), 2+check[0], period, roomId));
				}else if(check[0] == 1){
					// 최종 투표할 시간이다!!
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), 2+check[0]-1, period, roomId));
				}else if(check[0] == 2){
					// 타이머 끝!! 밤으로 넘어가자
					template.convertAndSend("/sub/game/judge/"+roomId,gameTimer(chat.getRound(), descs.length-1, 0, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,(period*1000));
	}

	@MessageMapping("/game/night/{roomId}")
	public void gameNight(MafiaChat chat, @DestinationVariable String roomId) {
		// 2 : 1
		int period = chat.getSecond()/3;
		System.out.println("night - 타이머 period 설정: "+period);
		// morning인지 vote인지 체크하는 변수 check
		final int[] check = {0};
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				//System.out.println("타이머 작동 "+round[0]+", "+desc[0]+", 라운드 수: "+chat.getRound());
				if(check[0] == 0){
					// 밤이다!!
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), 4+check[0], period*2, roomId));
				}else if(check[0] == 2){
					// 결과다!!
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), 4+check[0]-1, period, roomId));
				}else if(check[0] == 3){
					// 타이머 끝!! 게임 끝 또는 아침으로 넘어가자
					template.convertAndSend("/sub/game/night/"+roomId,gameTimer(chat.getRound(), descs.length-1, 0, roomId));
					timer.cancel();
				}
				check[0] = check[0]+1;
			}
		};
		// 실행된 후 0초뒤, 10초마다 실행
		timer.schedule(tt,0,(period*1000));
	}

	//@SendTo("/sub/game/start/{roomId}")
	public MafiaChat gameTimer(int round, int desc, int period, @DestinationVariable String roomId) {
		//System.out.println("타이머 함수 진입 "+round+", "+desc);
		MafiaChat mc = new MafiaChat();
		mc.setRound(round);
		mc.setDesc(descs[desc]);
		mc.setSecond(period);
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
