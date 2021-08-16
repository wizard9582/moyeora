// ROOT STATE 변수 정의 및 기본값 대입

export default {
  userId: localStorage.getItem('id'),
  authToken: localStorage.getItem('jwt'),
  roomOwner: localStorage.getItem('roomOwner'),
  stompClient: null,
  participantsList: null,
  voteCount: null,
  gameTime: null,
  gameRound: null,
  finalVotePlayer: null,
  finalVoteCount: null,
  doctorSelectPlayer: null,
  mafiaSelectPlayer: null,
  myJob: "(미정)",
  mafiaRoles: null,
  mylife: true, // 현재 내가 죽었는지 살았는지에 대한 boolean 값
  voteStarted: false,
  skipStage: false,
  gameRefresh: false,
  gameStarted: false,
  micOff: false,
  videoOff: false,
}
