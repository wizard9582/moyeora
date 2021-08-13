<template>
  <el-dialog title="로그인" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="loginForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.id" autocomplete="off" maxlength="10"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickLogin" :disabled="state.btnDisabled">로그인</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name:'LoginPop',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const loginForm = ref(null)

    const state = reactive({
      formLabelWidth: '120px',
      form: {
        id: '',
        password: '',
        align: 'left',
      },
      rules: {
        id: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 16, message: '최대 16자까지 입력 가능합니다.', trigger: 'change' }
        ],
        password: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { min: 8, message: '최소 8 글자를 입력해야 합니다.', trigger: 'change' },
          { max: 16, message: '최대 16 글자까지 입력 가능합니다.', trigger: 'change' },
          { required: true, pattern: /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+])/, message: '비밀번호는 영문, 숫자, 특수문자가 조합되어야합니다.', trigger: 'change' }
        ],
      },
      btnDisabled: true,
      popupVisible: computed(() => props.open),
      popupLoading: false,
    })

    const checkValidation = function() {
      loginForm.value.validate((valid) => {
        if (valid) {
          state.btnDisabled = false
        } else {
          state.btnDisabled = true
        }
      })
    }

    const getUserId = function (token) {
      store.dispatch('root/requestUserInfo', token)
      .then((result) => {
        store.commit('root/setUserId', result.data.userId)
      })
      .catch((err) => {
        alert(err)
      })
    }

    const clickLogin = function () {
      // 스피너 시작
      state.popupLoading = true;
      state.btnDisabled = true;
      setTimeout(() => {
        state.popupLoading = false;
        state.btnDisabled = false;
        // 로그인 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
        loginForm.value.validate((valid) => {
          if (valid) {
            console.log('submit')
            store.dispatch('root/requestLogin', { id: state.form.id, password: state.form.password })
            .then(function (result) {
              console.log("--------->", result)
              store.commit('root/setToken', result.data.accessToken)
              // 해당 유저의 정보를 받아오는 axios
              getUserId(result.data.accessToken)
              emit('closeLoginPopup')
            })
            .catch(function (err) {
              //console.log(err.response.status);

              if(err.response.status == 401){
                alert('비밀번호를 잘못 입력하셨습니다.')
              }
              else if(err.response.status == 500){
                alert('존재하지 않는 회원입니다.')
              }
            })
          } else {
            alert('Validate error!')
          }
        });
      }, 1000);
    }

    //console.log(store)
    const handleClose = function () {
      state.form.id = ''
      state.form.password = ''
      loginForm.value.resetFields()
      emit('closeLoginPopup')
    }

    return { loginForm, state, checkValidation, clickLogin, handleClose }
  }
}
</script>

<style>

</style>
