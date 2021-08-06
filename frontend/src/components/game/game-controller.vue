<template>
    <el-button type="primary" @click="micOff">음소거 버튼</el-button>
    <el-button type="primary" @click="cameraOff">비디오 버튼</el-button>
    <el-button icon="el-icon-close" type="danger" @click="clickGameClose"></el-button>
    <el-button :icon="state.chatIcon" type="primary" @click="clickChat"></el-button>
</template>

<script>
import { reactive, computed } from 'vue'
import { leaveRoom, participants, muteMic, offCam } from '@/common/lib/conferenceroom'
import { useStore } from 'vuex'

export default {
  name: 'GameController',

  setup(props, {emit}) {
    const store = useStore()

    const state = reactive({
      chatClicked: false,
      chatIcon: 'el-icon-chat-dot-round',
      userName: computed(() => store.getters['root/getUserId'])
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

    const micOff = () => {
      console.log('마이크 끄기 클릭')
      muteMic(state.userName);
    }

    const cameraOff = () => {
      console.log('카메라 끄기 클릭')
      offCam(state.userName);
    }

    return { state, clickChat, clickGameClose, micOff, cameraOff }
  }
}
</script>

<style>

</style>
