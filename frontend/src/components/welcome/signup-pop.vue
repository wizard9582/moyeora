<template>
  <el-dialog title="회원 가입" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="signupForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="department" label="소속" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.department" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="position" label="직책" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.position" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.id" autocomplete="off" style="width: 71%; padding-right: 10px;"></el-input>
        <el-button type="primary" @click="checkID">중복 확인</el-button>
      </el-form-item>
      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="passwordConfirm" label="비밀번호 확인" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.passwordConfirm" autocomplete="off" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickRegister" :disabled="state.btnDisabled">가입하기</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name:'SignupPop',

  props: {
    open: {
      type: Boolean,
      default: false,
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const signupForm = ref(null)

    const state = reactive({
      formLabelWidth: '120px',
      form: {
        id: '',
        password: '',
        passwordConfirm: '',
        name: '',
        department: '',
        position: '',
        align: 'left'
      },
      rules: {
        name: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
        department: [
          { required: false, message: 'Please input department', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
        position: [
          { required: false, message: 'Please input position', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
        id: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' }
        ],
        password: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { min: 8, message: '최소 9 글자를 입력해야 합니다.', trigger: 'change' },
          { max: 16, message: '최대 16 글자까지 입력 가능합니다.', trigger: 'change' },
          { required: true, pattern: /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+])/,
            message: '비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.',
            trigger: 'change'
          }
        ],
        passwordConfirm: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { min: 8, message: '최소 9 글자를 입력해야 합니다.', trigger: 'change' },
          { max: 16, message: '최대 16 글자까지 입력 가능합니다.', trigger: 'change' },
          { required: true, pattern: /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+])/,
            message: '비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.',
            trigger: 'change'
          },
          { validator(rule, value, callback){
              return state.form.password === value
            }, message:'입력한 비밀번호와 일치하지 않습니다.', trigger: 'change' }
        ],
      },
      btnDisabled: true,
      popupVisible: computed(() => props.open),
      popupLoading: false,
      checkIdValidation: false,
    })

    const checkValidation = function() {
      signupForm.value.validate((valid) => {
        if (valid && state.checkIdValidation) {
          state.btnDisabled = false
        } else {
          state.btnDisabled = true
        }
      })
    }

    const checkID = function () {
      store.dispatch('root/requestCheckID', {id: state.form.id})
      .then(function () {
        alert('사용 가능한 아이디입니다.')
        state.checkIdValidation = true
      })
      .catch(function () {
        alert('이미 존재하는 아이디입니다.')
        state.checkIdValidation = false
      })
    }

    const clickRegister = function () {
      if (state.checkIdValidation) {
        state.popupLoading = true;
        state.btnDisabled = true;
        setTimeout(() => {
          state.popupLoading = false;
          state.btnDisabled = false;
          // 로그인 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
          signupForm.value.validate((valid) => {
            if (valid) {
              console.log('submit')
              store.dispatch('root/requestRegister', { id: state.form.id, password: state.form.password, name: state.form.name, department: state.form.department, position: state.form.position })
              .then(function () {
                emit('closeSignupPopup')
                alert('회원 가입이 완료되었습니다.')
              })
              .catch(function () {
                alert('회원 가입에 실패하였습니다.')
              })
            } else {
              alert('Validate error!')
            }
          });
        }, 1000);
      } else {
        alert('이미 존재하는 아이디입니다.')
      }
    }

    const handleClose = function () {
      state.form.id = ''
      state.form.password = ''
      state.form.passwordConfirm = ''
      state.form.name = ''
      state.form.department = ''
      state.form.position = ''
      state.btnDisabled = true
      state.checkIdValidation = false
      signupForm.value.resetFields()
      emit('closeSignupPopup')
    }

    return { signupForm, state, clickRegister, handleClose, checkValidation, checkID }
  }
}
</script>

<style>

</style>
