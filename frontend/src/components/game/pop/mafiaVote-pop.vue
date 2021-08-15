<template>
  <el-dialog title="제거할 플레이어 선택" v-model="state.popupVisible" width="30%" @close="handleClose">
    <div class="mafia-vote">
      <span>제거할 플레이어를 선택하세요!</span>
      <el-table :data="state.participantsList" highlight-current-row @current-change="clickKillPlayer">
        <el-table-column property="userId" label="Player"></el-table-column>
      </el-table>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="finishVote">선택 완료</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'MafiaVotePop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()

    const state = reactive({
      popupVisible: computed(() => props.open),
      userName: computed(() => store.getters['root/getUserId']),
      ownerName: computed(() => store.getters['root/getRoomOwner']),
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      finalVotePlayer: computed(() => store.getters['root/getFinalVotePlayer']),
      voteCount: computed(() => store.getters['root/getFinalVoteCount']),
      selectedPlayer: null,
    })

    const clickKillPlayer = function (val) {
      state.selectedPlayer = val
      console.log(val)
    }

    const finishVote = function () {
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          roomId: route.params.no,
          fromName: "mafia",
          toName: state.selectedPlayer.userId,
          message: "mafia"
        };
        state.stompClient.send("/pub/vote/room/"+ route.params.no, JSON.stringify(msg), {});
      }
      emit('closeMafiaVotePopup')
    }

    const handleClose = function () {
      emit('closeMafiaVotePopup')
    }

    return { state, handleClose, clickKillPlayer, finishVote }
  }
}
</script>

<style>
.mafia-vote {
  text-align: center;
  font-size: 20px;
}

.mafia-vote > div {
  padding-top: 20px;
}
</style>
