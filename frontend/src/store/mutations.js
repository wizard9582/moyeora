
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

export function setMyJob(state, job) {
  if(job == "reset"){
    state.myJob = "(미정)"
  }else if(job == "mafia"){
    state.myJob = "마피아"
  }else if(job == "citizen"){
    state.myJob = "시민"
  }else if(job == "doctor"){
    state.myJob = "의사"
  }else{
    state.myJob = "경찰"
  }
}
