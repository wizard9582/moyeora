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
