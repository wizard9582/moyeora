// 로그인 유무 확인
export function isLoggedIn(state) {
  return state.authToken ? true : false
}

export function getUserId(state) {
  return state.userId
}
