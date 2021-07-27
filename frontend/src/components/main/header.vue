<template>
  <el-header v-if="!isLoggedIn()" class="main-header" :height="'70px'">
    <div class="main-logo" @click="clickLogo">
      <img :src="require('@/assets/img-vue.png')" alt="IceBreaking logo" width="50" height="50"/>
    </div>
    <div class="button-wrapper">
      <el-button icon="el-icon-circle-plus-outline" @click="clickSignup">회원 가입</el-button>
      <el-button icon="el-icon-key" type="primary" @click="clickLogin">로그인</el-button>
    </div>
  </el-header>
</template>

<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name:'Header',

  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      fullscreenLoading: false,
    })

    // 로그인 유무 확인
    const isLoggedIn = function () {
      return store.getters['root/isLoggedIn']
    }

    // 로그인 팝업 열기
    const clickLogin = () => {
      emit('openLoginPopup')
    }

    // 회원가입 팝업 열기
    const clickSignup = () => {
      emit('openSignupPopup')
    }

    // 로그아웃
    const clickLogout = () => {
      store.commit('root/removeToken')
      router.push({
        // 수정 필요
        name: 'home'
      })
    }

    return { state, isLoggedIn, clickLogin, clickSignup, clickLogout }
  }
}
</script>

<style>
  .main-header {
    padding: 10px 20px;
    border-bottom: 2px solid lightgray;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .main-logo,
  .button-wrapper {
    display: inline;
  }

</style>
