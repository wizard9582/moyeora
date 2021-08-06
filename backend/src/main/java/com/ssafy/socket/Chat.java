package com.ssafy.socket;

import lombok.Getter;

/* *
 * 채팅 메세지
 * roomId : 방 번호
 * fromName : 발신자
 * toName : 수신자
 * message : 내용
 * */

@Getter
public class Chat {
	private String roomId;
	private String fromName;
	private String toName;
	private String message;
	
	public Chat() {
		
	}
	
	public Chat(String roomId, String fromName, String message) {
		this.roomId = roomId;
		this.fromName = fromName;
		this.message = message;
	}
	
	public Chat(String roomId, String fromName, String toName, String message) {
		this.roomId = roomId;
		this.fromName = fromName;
		this.toName = toName;
		this.message = message;
	}
}
