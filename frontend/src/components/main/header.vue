<template>
  <el-header v-if="isLoggedIn()" class="main-header" :height="'70px'">
    <a href="/home" class="logo">
      <img :src="require('@/assets/img-vue.png')" alt="IceBreaking logo" width="50" height="50"/>
    </a>
    <ul class="nav">
      <li>
        <el-badge :value="6">
          <el-button icon="el-icon-bell" circle v-on:click="clickOnBell" ></el-button>
        </el-badge>
      </li>
      <li>
        <el-button icon="el-icon-user" circle v-on:click="clickOnUser" ></el-button>
      </li>
    </ul>
  </el-header>
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

  setup(props, {emit}){
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

    const clickOnBell = () => {
      console.log('bell')
    }
    const clickOnUser = () => {
      console.log('user')
    }

    return { state, isLoggedIn, clickLogin, clickSignup, clickLogout, clickOnBell, clickOnUser }
  }
}
</script>

<style>
.logo {
  float: left;
  margin-top: 10px;
}
.nav{
  float: right;
  list-style: none;
  margin-right: 5px;
}
.nav li{
  float: left;
  margin: 5px;
}

.main-header {
  padding: 10px 20px;
  border-bottom: 2px solid lightgray;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
}

.main-logo,
.button-wrapper {
  display: inline;
}

</style>
