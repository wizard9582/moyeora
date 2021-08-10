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
  return $axios.delete(url)
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
