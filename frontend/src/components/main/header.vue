<template>
  <el-header v-if="isLoggedIn()" class="main-header" :height="'70px'">
    <a href="/home/all" class="logo">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="60" />
    </a>
    <ul class="nav">
      <li>
        <el-dropdown trigger="click">
          <el-badge :value="6">
            <el-button icon="el-icon-bell" circle></el-button>
          </el-badge>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item icon="el-icon-circle-check">Action 1</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check">Action 2</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check">Action 3</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check">Action 4</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check">Action 5</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </li>
      <li>
        <el-dropdown trigger="click">
          <el-button icon="el-icon-user" circle></el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item icon="el-icon-user" @click="clickUserInfo">내 정보</el-dropdown-item>
              <el-dropdown-item icon="el-icon-back" @click="clickLogout">로그아웃</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </li>
    </ul>
  </el-header>
  <el-header v-if="!isLoggedIn()" class="main-header" :height="'70px'">
    <a href="/home/all" class="logo">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="60" />
    </a>
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
      router.push("/")
    }
    const clickUserInfo = () => {
      //console.log('user') 밑에 router 자기 id로 옮겨지게
      router.push("/home/user/id")
    }

    setInterval(function(){
          store.dispatch('root/requestInviteList', { token: localStorage.getItem('jwt') })
          .then((result) => {
            //받아온 JSON 형태의 초대 리스트를 html에 달아놓는 작업 필요
            // id;
            // fromUser;
            // toUser;
            // url;
            // month;
            // day;
            // hour;
            // min;
            // 삭제시 @DeleteMapping("/delete/{inviteId}") 이 주소
          })
          .catch((error) => {
            console.log(error)
          })
    },5000);

    return { state, isLoggedIn, clickLogin, clickSignup, clickLogout, clickUserInfo }
  },
}
</script>

<style>
.logo {
  float: left;
  margin-top: 10px;
  cursor: pointer;
  -webkit-transform:scale(1);
  -moz-transform:scale(1);
  -ms-transform:scale(1);
  -o-transform:scale(1);
  transform:scale(1);
  -webkit-transition:.3s;
  -moz-transition:.3s;
  -ms-transition:.3s;
  -o-transition:.3s;
  transition:.3s;
}
.logo:hover {
  -webkit-transform:scale(1.2);
  -moz-transform:scale(1.2);
  -ms-transform:scale(1.2);
  -o-transform:scale(1.2);
  transform:scale(1.2);
  z-index: 5000;
}
.nav {
  float: right;
  list-style: none;
  margin-right: 5px;
}
.nav li {
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
  z-index: 100;
}

.main-logo,
.button-wrapper {
  display: inline;
}
</style>