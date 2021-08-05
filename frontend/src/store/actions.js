import $axios from 'axios'

export function requestLogin ({ state }, payload) {
  console.log('requestLogin', state, payload)
  const url = '/auth/login'
  let body = payload
  return $axios.post(url, body)
}

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

export function requestCreateRoom ({ state }, payload) {
  //테스트 후 token 가져오기 vuex store 기반으로 변경
  //body의 JSON.stringify는 자동으로 되나? 체크
  let token = localStorage.getItem('jwt')

  console.log('requestCreateRoom', state, payload)
  const url = `/room`
  let body = payload

  return $axios({
    method:'post',
    url: url,
    headers: {'Authorization': 'Bearer ' + token},
    body: body
  })
  // let header = {'Authorization': 'Bearer ' + token}
  // return $axios.post(url, header, body)
}

export function requestRoomList ({ state }, payload) {
  console.log('requestRoomList', state, payload)
  const url = `/room/list`
  return $axios.get(url)
}

export function requestUserInfo ({ state }, token) {
  console.log('requestUserInfo', state, token)
  const url = `/users/me`
  const headers = {
    'Authorization': 'Bearer ' + token
  }
  return $axios.get(url, {headers: headers})
}
