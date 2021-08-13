<template>
  <el-dialog title="방 나가기" v-model="state.popupVisible" width="30%" @close="handleClose">
    <span>현재 방에서 나가시겠습니까?</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="danger" @click="leaveGame">나가기</el-button>
        <el-button @click="handleClose">취소</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { leaveRoom } from '@/common/lib/conferenceroom'
// import { stompClient, disconnectSocket } from '@/common/lib/webSocket'

export default {
  name: 'GameClosePop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  setup(props, {emit}) {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()

    const state = reactive({
      popupVisible: computed(() => props.open),
      userName: computed(() => store.getters['root/getUserId']),
      ownerName: computed(() => store.getters['root/getRoomOwner']),
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
    })

    console.log("루트: ",route)
    const roomId = route.params.no;

    const disconnectSocket = () => {
      if (state.stompClient !== null) {
        state.stompClient.disconnect();
      }
      store.commit('root/setStompClient', null);
      console.log("Disconnected");
    }

    const sendToLeave = () => {
      console.log("Send message To Leave ");
      if (state.stompClient && state.stompClient.connected) {
        const msg1 = {
          roomId: roomId,
          fromName: state.userName,
        };
        state.stompClient.send("/pub/leave/"+ roomId, JSON.stringify(msg1), {});
      }
    }

    const leaveGame = function () {
      let token = localStorage.getItem('jwt')
      let roomNum = route.params.no
      store.dispatch('root/requestLeaveRoom', { token: token, roomId: roomNum })
      .then((result) => {
        if(state.userName == state.ownerName){
          sendToLeave()
        }
        if (state.stompClient && state.stompClient.connected) {
          const msg2 = {
              name: state.userName,
          };
          state.stompClient.send("/pub/bye/room/"+ roomId, JSON.stringify(msg2), {});
        }
        store.commit('root/removeRoomOwner')
        for (let player of state.participantsList) {
          if (state.userName == player.userId) {
            let msg = {
              name: player.id
            }
            console.log(player.id)
            state.stompClient.send("/pub/game/end/"+ roomId, JSON.stringify(msg), {});
            break
          }
        }
        disconnectSocket()
        console.log('result : ', result)
        router.push("/home/" + 'all')
        leaveRoom()
      })
      .catch((error) => {
        console.log(error)
      })
    }

    const handleClose = function () {
      emit('closeGameClosePopup')
    }

    return { state, handleClose, leaveGame }
  }
}
</script>

<style>

</style>
