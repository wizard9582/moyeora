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
}
