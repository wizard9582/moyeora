<template>
  <el-dialog title="친구 초대하기" v-model="state.popupVisible" @close="handleClose">
    <el-form :label-position="state.align">
      <el-form-item prop="friend" label="초대할친구" :label-width="state.formLabelWidth">
        <el-input></el-input>
      </el-form-item>
      <el-form-item prop="link" label="초대 링크" :label-width="state.formLabelWidth">
        <textarea id="invite-link">https://localhost:8080/game/123/</textarea>
        <el-button type="primary" @click="copyLink">링크 복사</el-button>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="footer">
        <el-button type="primary" @click="handleClose">닫기</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'

export default {
  name: "InvitePop",

  props: {
    open: {
      type: Boolean,
      default: false
    },
  },

  setup(props, { emit }) {
    const state = reactive({
      formLabelWidth: '120px',
      inviteLink: '',
      align: 'left',
      popupVisible: computed(() => props.open),
    })

    // 링크를 클립보드에 복사해주는 함수
    // 우선은 textarea로 설정 input text 로도 가능
    const copyLink = () => {
      let link = document.getElementById('invite-link')
      link.select()

      document.execCommand("copy")
    }

    const handleClose = () => {
      emit('closeInvitePopup')
    }

    return { state, copyLink, handleClose }
  }
}
</script>

<style>

</style>
