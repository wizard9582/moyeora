<template>
  <el-header class="game-header">
    <div class="main-logo">
      <img :src="require('@/assets/img-vue.png')" alt="IceBreaking logo" width="50" height="50"/>
    </div>
    <div class="gameroom-desc">
      <span>김싸피의 마피아게임</span>
    </div>
    <div class="button-wrapper">
      <el-button icon="el-icon-message" @click="clickInvite"></el-button>
      <el-button icon="el-icon-bell" circle @click="clickOnBell" ></el-button>
      <el-button icon="el-icon-user" circle @click="clickOnUser" ></el-button>
    </div>
  </el-header>
</template>

<script>
export default {
  name: 'GameHeader',

  setup(props, {emit}) {
    // 초대하기 팝업 열기
    const clickInvite = () => {
      emit('openInvitePopup')
      console.log('invite')
    }

    const clickOnBell = () => {
      console.log('bell')
    }

    const clickOnUser = () => {
      console.log('user')
    }

    return { clickInvite, clickOnBell, clickOnUser }
  }
}
</script>

<style>

.game-header {
  padding: 10px 20px;
  border-bottom: 2px solid lightgray;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  z-index: 9999;
}

.main-logo,
.button-wrapper {
  display: inline;
}
</style>
