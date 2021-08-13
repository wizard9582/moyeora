<template>
  <el-dialog title="보호할 플레이어 선택" v-model="state.popupVisible" width="30%" @close="handleClose">
    <div class="doctor-vote">
      <span>의사의 보호를 받을 플레이어를 선택하세요!</span>
      <el-table :data="state.participantsList" highlight-current-row @current-change="clickSavePlayer">
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
  name: 'DoctorVotePop',

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

    const clickSavePlayer = function (val) {
      state.selectedPlayer = val
      console.log(val)
    }

    const finishVote = function () {
      if (state.stompClient && state.stompClient.connected) {
        const msg = {
          roomId: route.params.no,
          fromName: "doctor",
          toName: state.selectedPlayer,
          message: "doctor"
        };
        state.stompClient.send("/pub/vote/room/"+ route.params.no, JSON.stringify(msg), {});
      }
      emit('closeDoctorVotePopup')
    }

    const handleClose = function () {
      emit('closeDoctorVotePopup')
    }

    return { state, handleClose, clickSavePlayer, finishVote }
  }
}
</script>

<style>
.doctor-vote {
  text-align: center;
  font-size: 20px;
}

.doctor-vote > div {
  padding-top: 20px;
}
</style>
