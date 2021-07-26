
export function setToken (state, token) {
  state.authToken = token
  localStorage.setItem('jwt', token)
}

export function removeToken (state) {
  state.authToken = ''
  localStorage.removeItem('jwt')
}
