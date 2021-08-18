import $axios from 'axios'

// AUTH REQUEST
export function requestLogin ({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = '/auth/login'
  let body = payload
  return $axios.post(url, body)
}


// USER REQUEST
export function requestRegister ({ state }, payload) {
  console.log('requestRegister', state, payload)
  const url = '/users'
  let body = payload
  return $axios.post(url, body)
}

export function requestCheckID ({ state }, payload) {
  console.log('requestCheckID', state, payload)
  const url = `/users/${payload.id}`
  return $axios.get(url)
}

export function requestUserInfo ({ state }, token) {
  console.log('requestUserInfo', state, token)
  const url = `/users/me`
  const headers = {
    'Authorization': 'Bearer ' + token
  }
  return $axios.get(url, {headers})
}

//---------- 이 밑으로 게시판 관련 api ----------

export function requestPostBoard ({ state }, payload) {
  console.log('requestPostBoard', state, payload)
  const url = `/post/register`
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'post',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: payload
  })
}
export function requestUpdateBoard ({ state }, payload) {
  console.log('requestUpdateoard', state, payload)
  const url = `/post/update/` + payload.postId
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'PATCH',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: payload
  })
}
export function requestCheckWriter ({ state }, payload) {
  console.log('requestCheckWriter', state, payload)
  const url = `/post/writer/` + payload.postId
  let token = localStorage.getItem('jwt')
  return $axios({
    method:'get',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
  })
}
export function requestBoard ({ state }, payload) {
  console.log('requestBoard', state, payload)
  const url = `/post/`
  return $axios.get(url)
}
export function requestNotice ({ state }, payload) {
  console.log('requestNotice', state, payload)
  const url = `/post/notice`
  return $axios.get(url)
}
export function requestBoardById ({ state }, payload) {
  console.log('requestBoardById', state, payload)
  const url = `/post/posts/` + payload.postId
  return $axios.get(url)
}
export function requestDeleteBoard ({ state }, payload) {
  console.log('requestDeleteBoard', state, payload)
  const url = `/post/` + payload.postId
  let token = localStorage.getItem('jwt')
  return $axios({
    method:'delete',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
  })
}

// ---------- 이 밑으로 게임방 관련 api ----------
// ROOM REQUEST
export function requestCreateRoom ({ state }, payload) {
  console.log('requestCreateRoom', state, payload)
  const url = '/room'
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'post',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: payload
  })
}

export function requestRoomList ({ state }, payload) {
  console.log('requestRoomList')
  const url = `/room/roomList`
  return $axios.get(url)
}

export function requestRoomInfo ({ state }, payload) {
  console.log('requestRoomInfo', state, payload)
  const url = `/room/${payload.roomId}`
  return $axios.get(url)
}

export function requestEnterRoom ({ state }, payload) {
  console.log('requestEnterRoom', state, payload)
  const url = `/room/enter?roomId=${payload.roomId}`
  const headers = {
    'Authorization': 'Bearer ' + payload.token
  }
  // const params = { roomId: payload.roomId }
  return $axios.get(url, {headers})
}

export function requestLeaveRoom ({ state }, payload) {
  console.log('requestLeaveRoom', state, payload)
  const url = `/room/leave?roomId=${payload.roomId}`
  const headers = {
    'Authorization': 'Bearer ' + payload.token
  }
  // const params = { roomId: payload.roomId }
  return $axios.delete(url, {headers})
}

export function requestByPolice ({ state }, payload) {
  console.log('requestByPolice', state, payload)
  const url = `/mafia/police?roomId=${payload.roomId}`
  // const params = { roomId: payload.roomId }
  return $axios.get(url)
}

// ----------------초대 하기 및 초대 받기 API 시작----------------------
export function requestRecommandFriendList ({ state }, payload) {
  console.log('requestRecommandFriendList', state, payload)
  const url = `/search?name=${payload.friendName}`
  return $axios.get(url)
}

export function requestInvite ({ state }, payload) {
  console.log('requestInvite', state, payload)
  const url = '/invite'
  let body = payload
  return $axios.post(url,body)
}

export function requestInviteList ({ state }, payload) {
  const url = '/invite/list'
  const headers = {
    'Authorization': 'Bearer ' + payload.token
  }
  // const params = { roomId: payload.roomId }
  return $axios.get(url, {headers})
}

export function deleteInvite ({ state }, payload) {
  console.log('deleteInvite', state, payload)
  const url = '/invite/delete/' + payload.id
  let body = payload
  return $axios.delete(url,body)
}
// ----------------초대 하기 및 초대 받기 API 끝----------------------

// ----------------친구 요청 API 시작----------------------

//친구 목록 불러오기
export function requestFriendList ({ state }, payload) {
  const url = '/relation'
  const headers = {
    'Authorization': 'Bearer ' + payload.token
  }
  // const params = { roomId: payload.roomId }
  return $axios.get(url, {headers})
}

//친구 요청 하기
export function requestFriend ({ state }, payload) {
  console.log('requestFriend', state, payload)
  const url = '/relation'
  //body => { toUser : 친구할 사람 }
  let body = payload
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'post',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: body
  })
}

//친구 요청 수락 하기
export function requestMakeFriend ({ state }, payload) {
  console.log('requestMakeFriend', state, payload)
  const url = '/relation/make'
  //body => { toUser : 친구할 사람 }
  let body = payload
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'post',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: body
  })
}

//친구 삭제 하기
export function requestDeleteFriend ({ state }, payload) {
  console.log('requestDeleteFriend', state, payload)
  const url = '/relation/delete'

   //body => { toUser : 친구할 사람 }
  let body = payload
  let token = localStorage.getItem('jwt')

  return $axios({
    method:'delete',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    data: body
  })
}
// ----------------친구 요청 API 끝----------------------
// ----------------전적 API 시작 -----------------------
export function requestMyStat ({ state }, payload) {
  const url = '/users/matchHistory'
  const headers = {
    'Authorization': 'Bearer ' + payload.token
  }
  // const params = { roomId: payload.roomId }
  return $axios.get(url, {headers})
}
