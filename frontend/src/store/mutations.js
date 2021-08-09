
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
  state.userId = id
  localStorage.removeItem('id')
}

export function setRoomOwner(state, id) {
  state.roomOwner = id
  localStorage.setItem('roomOwner', id)
}
