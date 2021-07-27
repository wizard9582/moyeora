<template>
  <el-dialog title="개인설정" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="settingForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="theme" label="테마" :label-width="state.formLabelWidth">
        <el-radio-group v-model="state.form.theme">
          <el-radio-button label="기본테마"></el-radio-button>
          <el-radio-button label="어둠의자식"></el-radio-button>
          <el-radio-button label="사이버펑크"></el-radio-button>
          <el-radio-button label="레트로감성"></el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="fontSize" label="글씨 크기" :label-width="state.formLabelWidth">
        <el-input-number v-model="state.form.fontSize" :min="8" :max="16"></el-input-number>
      </el-form-item>
      <el-form-item prop="alarm" label="알람설정" :label-width="state.formLabelWidth">
        <el-radio-group v-model="state.form.alarm" size="small">
          <el-radio label="true" border>알람 켜기</el-radio>
          <el-radio label="false" border disabled>알람 끄기</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickAccept" :disabled="state.btnDisabled">저장</el-button>
        <el-button type="danger" @click="handleClose">취소</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name:'SettingPop',
props: {
    open: {
      type: Boolean,
      default: false,
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const settingForm = ref(null)

    const state = reactive({
      formLabelWidth: '120px',
      form: {
        theme: 1,
        fontSize: 10,
        alarm: false,
        align: 'left'
      },
      popupVisible: computed(() => props.open),
    })

    const clickAccept = function () {
      // if (state.checkIdValidation) {
      //   state.popupLoading = true;
      //   state.btnDisabled = true;
      //   setTimeout(() => {
      //     state.popupLoading = false;
      //     state.btnDisabled = false;
      //     // 로그인 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      //     signupForm.value.validate((valid) => {
      //       if (valid) {
      //         console.log('submit')
      //         store.dispatch('root/requestRegister', { id: state.form.id, password: state.form.password, name: state.form.name, department: state.form.department, position: state.form.position })
      //         .then(function () {
      //           emit('closeSignupPopup')
      //           alert('회원 가입이 완료되었습니다.')
      //         })
      //         .catch(function () {
      //           alert('회원 가입에 실패하였습니다.')
      //         })
      //       } else {
      //         alert('Validate error!')
      //       }
      //     });
      //   }, 1000);
      // } else {
      //   alert('이미 존재하는 아이디입니다.')
      // }
    }

    const handleClose = function () {
      state.form.title = ''
      state.form.lock = false
      state.form.password = ''
      state.form.desc = ''
      state.btnDisabled = true
      settingForm.value.resetFields()
      emit('closeSettingPopup')
    }

    return { settingForm, state, clickAccept, handleClose}
  }
}
</script>

<style>

</style>
