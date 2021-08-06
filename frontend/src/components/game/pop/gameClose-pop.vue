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
import { useRouter } from 'vue-router'
import { leaveRoom } from '@/common/lib/conferenceroom'

export default {
  name: 'GameClosePop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  setup(props, {emit}) {
    const router = useRouter()
    const state = reactive({
      popupVisible: computed(() => props.open),
    })

    const leaveGame = function () {
      router.push("/home/" + 'all')
      leaveRoom()
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
