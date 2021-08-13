
export function setToken (state, token) {
  state.authToken = token
  localStorage.setItem('jwt', token)
}

export function removeToken (state) {
  state.authToken = ''
  localStorage.removeItem('jwt')
}

export function setUserId(state, id) {
  state.userId = id
  localStorage.setItem('id', id)
}

export function removeUserId (state) {
  state.userId = ''
  localStorage.removeItem('id')
}

export function setRoomOwner(state, id) {
  state.roomOwner = id
  localStorage.setItem('roomOwner', id)
}

export function removeRoomOwner(state) {
  state.roomOwner = ''
  localStorage.removeItem('roomOwner')
}

export function setStompClient(state, payload) {
  state.stompClient = payload
}

export function setParticipantsList(state, payload) {
  state.participantsList = payload
}

export function setVoteCount(state, payload) {
  state.voteCount = payload
}

export function voteTo(state, user) {
  state.voteCount[user][0] += 1
}

export function setGameRound(state, payload) {
  state.gameTime = payload.second
  state.gameRound = payload.round
}

export function setFinalVotePlayer(state, user) {
  state.finalVotePlayer = user
}

export function setFinalVoteCount(state) {
  state.finalVoteCount = [0, 0]
}

export function voteFinalVote(state, vote) {
  if (vote === 'kill') {
    state.finalVoteCount[0]++
  } else {
    state.finalVoteCount[1]++
  }
}

export function setDoctorSelectPlayer(state, user) {
  state.doctorSelectPlayer = user
}

export function setMafiaSelectPlayer(state, user) {
  state.mafiaSelectPlayer = user
}
