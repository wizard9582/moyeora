<template>
  <el-dialog title="방 나가기" v-model="state.popupVisible" width="30%" @close="handleClose">
    <span>현재 방에서 나가시겠습니까?</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="danger">나가기</el-button>
        <el-button @click="handleClose">취소</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'

export default {
  name: 'GameClosePop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, {emit}) {
    const state = reactive({
      popupVisible: computed(() => props.open),
    })

    const handleClose = function () {
      emit('closeGameClosePopup')
    }

    return { state, handleClose }
  }
}
</script>

<style>

</style>
