<template>
  <el-dialog title="방 만들기" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="createRoomForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="title" label="방제" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="lock" label="비밀방 설정" :label-width="state.formLabelWidth">
        <el-checkbox v-model="state.form.lock"></el-checkbox>
      </el-form-item>
      <el-form-item v-if="state.pwRequired" prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="desc" label="방 설명" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.desc" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickCreateRoom" :disabled="state.btnDisabled">방만들기</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name:'CreateRoomPop',

  props: {
    open: {
      type: Boolean,
      default: false,
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const createRoomForm = ref(null)

    const state = reactive({
      formLabelWidth: '120px',
      form: {
        title: '',
        lock: false,
        password: '',
        desc: '',
        align: 'left'
      },
      rules: {
        title: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 15, message: '최대 15자까지 입력 가능합니다.', trigger: 'change' }
        ],
        desc: [
          { required: false, message: '설명을 입력해주세요', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
      },
      btnDisabled: true,
      pwRequired: false,
      popupVisible: computed(() => props.open),
    })
    // 비밀방 비밀번호 체크 수정해야함
    const checkValidation = function() {
      createRoomForm.value.validate((valid) => {
        if (valid && (!form.lock)) {
          state.btnDisabled = false
        } else {
          state.btnDisabled = true
        }
      })
    }

    const clickCreateRoom = function () {
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
      createRoomForm.value.resetFields()
      emit('onCloseRoomPopup')
    }

    return { createRoomForm, state, clickCreateRoom, handleClose, checkValidation}
  }
}
</script>

<style>

</style>
