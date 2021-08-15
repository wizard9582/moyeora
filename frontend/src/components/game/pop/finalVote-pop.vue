<template>
  <el-dialog title="최종 투표" v-model="state.popupVisible" width="50%" @close="handleClose">
    <div class="final-vote">
      <span>{{state.finalVotePlayer}} 님의 운명은?</span>
      <div>
        <el-button class="kill-button" type="danger" @click="clickKill" :disabled="!state.selected">죽이자</el-button>
        <el-button class="save-button" type="primary" @click="clickSave" :disabled="!state.selected">살리자</el-button>
      </div>
    </div>
    <!-- <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">취소</el-button>
      </span>
    </template> -->
  </el-dialog>
</template>

<script>
import { reactive, computed, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'FinalVotePop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  setup(props, {emit}) {
    const route = useRoute()
    const store = useStore()
    const roomId = route.params.no;

    const state = reactive({
      popupVisible: computed(() => props.open),
      userName: computed(() => store.getters['root/getUserId']),
      ownerName: computed(() => store.getters['root/getRoomOwner']),
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      finalVotePlayer: computed(() => store.getters['root/getFinalVotePlayer']),
      voteCount: computed(() => store.getters['root/getFinalVoteCount']),
      selected: computed(() => store.getters['root/getVoteStarted']),
    })

    const clickKill = function () {
      //store.commit('root/voteFinalVote', 'kill')
      document.querySelector('.kill-button').style.backgroundColor = 'red'
      state.selected = !state.selected
      if (state.stompClient && state.stompClient.connected) {
        const msg = { round : 1, fromName: 'final', toName: 'kill' }
        state.stompClient.send("/pub/vote/room/"+ roomId, JSON.stringify(msg), {});
      }
      emit('closeFinalVotePopup')
    }

    const clickSave = function () {
      //store.commit('root/voteFinalVote', 'save')
      document.querySelector('.save-button').style.backgroundColor = 'blue'
      state.selected = !state.selected
      if (state.stompClient && state.stompClient.connected) {
        const msg = { round : 1, fromName: 'final', toName: 'save' }
        state.stompClient.send("/pub/vote/room/"+ roomId, JSON.stringify(msg), {});
      }
      emit('closeFinalVotePopup')
    }

    const handleClose = function () {
      emit('closeFinalVotePopup')
    }

    // watch(
    //   () => state.voteCount,
    //   (voteCount, prevVoteCount) => {
    //     let total = voteCount.reduce((a, b) => a+b)
    //     if(state.userName === state.ownerName && total >= state.participantsList.length ){
    //       if (state.stompClient && state.stompClient.connected) {
    //         let playerPK = ''
    //         if (voteCount[0] > voteCount[1]) {
    //           for (let player of state.participantsList) {
    //             if (player.userId === state.finalVotePlayer) {
    //               playerPK = player.id
    //               break
    //             }
    //           }
    //         }
    //         const msg = { name: `night,${playerPK}` }
    //         state.stompClient.send("/pub/game/end/"+ roomId, JSON.stringify(msg), {});
    //       }
    //     }
    //   }
    // )
    return { state, handleClose, clickKill, clickSave }
  }
}
</script>

<style>
.final-vote {
  text-align: center;
  font-size: 20px;
}

.final-vote > div {
  padding-top: 20px;
}

.kill-button, .save-button {
  height: 40%;
  width: 40%;
}
</style>
