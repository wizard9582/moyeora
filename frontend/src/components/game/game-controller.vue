<template>
    <el-button type="primary">음소거 버튼</el-button>
    <el-button type="primary">비디오 버튼</el-button>
    <el-button icon="el-icon-close" type="danger" @click="clickGameClose"></el-button>
    <el-button :icon="state.chatIcon" type="primary" @click="clickChat"></el-button>
</template>

<script>
import { reactive } from 'vue'

export default {
  name: 'GameController',

  setup(props, {emit}) {

    const state = reactive({
      chatClicked: false,
      chatIcon: 'el-icon-chat-dot-round',
    })

    // 채팅창 열기
    const clickChat = () => {
      if (state.chatClicked) {
        state.chatClicked = false
        state.chatIcon = 'el-icon-chat-dot-round'
        emit('closeGameChat')
      } else {
        state.chatClicked = true
        state.chatIcon = 'el-icon-s-comment'
        emit('openGameChat')
      }
    }

    // 게임방 나가기
    const clickGameClose = () => {
      state.chatClicked = false
      state.chatIcon = 'el-icon-chat-dot-round'
      emit('openGameClosePopup')
    }

    return { state, clickChat, clickGameClose }
  }
}
</script>

<style>

</style>
