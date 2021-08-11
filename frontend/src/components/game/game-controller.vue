<template>
    <el-button class="game-start-button" v-if="!state.gameStart" @click="clickGameStart">게임 시작</el-button>
    <div class="main-controller">
      <el-button type="primary" @click="micOff">음소거</el-button>
      <el-button type="primary" @click="cameraOff">비디오</el-button>
      <el-button icon="el-icon-close" type="danger" @click="clickGameClose"></el-button>
      <el-button :icon="state.chatIcon" type="primary" @click="clickChat"></el-button>
    </div>
    <el-button class="player-list-button"  @click="clickPlayerList">참가자</el-button>
</template>

<script>
import { reactive, computed } from 'vue'
import { leaveRoom, participants, muteMic, offCam } from '@/common/lib/conferenceroom'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'GameController',

  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()

    const state = reactive({
      gameStart: false,
      chatClicked: false,
      chatIcon: 'el-icon-chat-dot-round',
      userName: computed(() => store.getters['root/getUserId']),
      stompClient: computed(() => store.getters['root/getStompClient']),
    })

    const roomId = route.params.no;

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

    // 게임 시작 버튼
    const clickGameStart = () => {
      console.log('게임 시작!!')
      state.gameStart = !state.gameStart
      gameTimerStart();
    }

    // 참가자 확인
    const clickPlayerList = () => {
      console.log('참가자 명단')
    }

    // 게임방 나가기
    const clickGameClose = () => {
      state.chatClicked = false
      state.chatIcon = 'el-icon-chat-dot-round'
      emit('openGameClosePopup')
    }

    // 마이크 버튼
    const micOff = () => {
      console.log('마이크 끄기 클릭')
      muteMic(state.userName);
    }

    // 카메라 버튼
    const cameraOff = () => {
      console.log('카메라 끄기 클릭')
      offCam(state.userName);
    }

    // 게임 시작 시 타이머 시작
    const gameTimerStart = () => {
      console.log("Send message To Start game timer ");
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          round: 0,
          desc: "morning",
          second : 20,
        };
        state.stompClient.send("/pub/game/morning/"+ roomId, JSON.stringify(msg), {});
      }
    }

    return { state, clickGameStart, clickPlayerList, clickChat, clickGameClose, micOff, cameraOff, gameTimerStart }
  }
}
</script>

<style>
.main-controller {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.game-start-button {
  position: absolute;
  left: 80px;
  transform: translateX(-50%);
}

.player-list-button {
  position: absolute;
  right: 0%;
  transform: translateX(-50%);
}
</style>
