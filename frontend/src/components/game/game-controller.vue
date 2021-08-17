<template>
    <el-button class="game-start-button" v-if="isOwner() && state.roomType=='mafia' && !state.gameStarted" :disabled="!canStart" @click="clickGameStart">게임 시작</el-button>
    <div class="main-controller">
      <el-button :icon="state.micIcon" @click="micOff" v-bind:class="{ 'el-button--primary': !state.micOff }" >음소거</el-button>
      <el-button :icon="state.videoIcon" @click="cameraOff" v-bind:class="{ 'el-button--primary': !state.videoOff }" >비디오</el-button>
      <el-button icon="el-icon-close" type="danger" @click="clickGameClose"></el-button>
      <el-button :icon="state.chatIcon" type="primary" @click="clickChat"></el-button>
    </div>
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
      roomType: route.params.type,
      chatClicked: false,
      chatIcon: 'el-icon-chat-dot-round',
      userName: computed(() => store.getters['root/getUserId']),
      ownerName: computed(() => store.getters['root/getRoomOwner']),
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      micOff: false,
      micIcon: 'el-icon-microphone',
      videoOff: false,
      videoIcon: 'el-icon-camera-solid',
      gameStarted: computed(() => store.getters['root/getGameStarted']),
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

    const isOwner = () => {
      return state.userName == state.ownerName
    }

    const canStart = () => {
      if(state.participantsList!=null){
        return (state.participantsList.length > 4)
      }else{
        return 0
      }
    }

    // 게임 시작 버튼
    const clickGameStart = () => {
      console.log('게임 시작!!')
      store.commit('root/startGame')
      gameTimerStart();
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
      state.micOff = !state.micOff
      if(!state.micOff){
        store.commit('root/setMicOff', false);
        state.micIcon = 'el-icon-microphone'
        muteMic(state.userName, false);
      }else{
        //console.log('마이크 스토어 확인: ', store.getters['root/getMicOff'])
        store.commit('root/setMicOff', true);
        state.micIcon = 'el-icon-close'
        muteMic(state.userName, true);
      }
    }

    // 카메라 버튼
    const cameraOff = () => {
      console.log('카메라 끄기 클릭')
      state.videoOff = !state.videoOff
      if(!state.videoOff){
        store.commit('root/setVideoOff', false);
        state.videoIcon = 'el-icon-camera-solid'
        offCam(state.userName, false);
      }else{
        store.commit('root/setVideoOff', true);
        state.videoIcon = 'el-icon-close'
        offCam(state.userName, true);
      }
    }

    // 게임 시작 시 타이머 시작
    const gameTimerStart = () => {
      console.log("Send message To Start game timer ");
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          round: 0,
          desc: "morning",
          second : 30,
          userId : state.userName,
        };
        state.stompClient.send("/pub/game/morning/"+ roomId, JSON.stringify(msg), {});
      }
    }

    // 뒤로가기 막기
    window.history.pushState(null, null, window.location.href);
    window.onpopstate = function () {
        window.history.go(1);
    };


    return { state, clickGameStart, clickChat, clickGameClose, micOff, cameraOff, gameTimerStart, isOwner, canStart }
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
