
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
  for (let player of state.participantsList) {
    player.death = false;
  }
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
export function setMafiaRoles(state, payload) {
  state.mafiaRoles = payload;
}

export function newRoundStart(state) {
  state.voteCount = null;
  state.finalVotePlayer = null;
  state.finalVoteCount = null;
  state.doctorSelectPlayer = null;
  state.mafiaSelectPlayer = null;

  // voteCount 세팅하기
  // voteCount 구조 = [{ userId : [0, pk] }]
  state.voteCount = new Object
  for (let player of state.participantsList) {
    let greetingUserId = player.userId
    state.voteCount[greetingUserId] = [0, player.id]
  }
  console.log('----- new Round Start ----- \n votePlayerList', state.voteCount)
}

export function setMylife(state, payload) {
  state.mylife = payload;
}

export function setDeath(state, payload) {
  for (let player of state.participantsList) {
    if (player.userId === payload) {
      player.death = true;
      break
    }
  }
}

export function resetDeath(state) {
  for (let player of state.participantsList) {
    player.death = false;
  }
  state.gameRefresh = !state.gameRefresh
}

export function startVote(state) {
  state.voteStarted = true
}

export function endVote(state) {
  state.voteStarted = false
}

export function skipStage(state, payload){
  state.skipStage = payload.value
}

export function startGame(state) {
  state.gameStarted = true
}

export function endGame(state) {
  state.gameStarted = false
}

export function setMicOff(state, payload) {
  state.micOff = payload;
}

export function setVideoOff(state, payload) {
  state.videoOff = payload;
}
