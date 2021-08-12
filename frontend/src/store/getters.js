// 로그인 유무 확인
export function isLoggedIn(state) {
  return state.authToken ? true : false
}

export function getUserId(state) {
  return state.userId
}

export function getRoomOwner(state) {
  return state.roomOwner
}

export function getStompClient(state) {
  return state.stompClient
}

export function getParticipantsList(state) {
  return state.participantsList
}

export function getVoteCount(state) {
  return state.voteCount
}

export function getGameTime(state) {
  return state.gameTime
}

export function getGameRound(state) {
  return state.gameRound
}
