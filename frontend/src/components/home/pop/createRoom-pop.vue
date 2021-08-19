<template>
  <el-dialog title="방 만들기" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="createRoomForm" :label-position="state.form.align" @change="checkValidation" v-loading private="state.popupLoading">
      <el-form-item prop="title" label="방 이름" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off" maxlength="20" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="종류" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.category">
          <el-option v-for="item in state.games" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="private" label="비밀방 설정" :label-width="state.formLabelWidth">
        <el-checkbox v-model="state.form.private"></el-checkbox>
      </el-form-item>
      <el-form-item v-if="state.form.private" prop="password" label="비밀번호" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item prop="description" label="방 설명" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.description" autocomplete="off" maxlength="30" show-word-limit></el-input>
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
import { useRouter } from 'vue-router';
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
    const router = useRouter()
    const createRoomForm = ref(null)

    const state = reactive({
      formLabelWidth: '120px',
      form: {
        title: '',
        category: 'video',
        private: false,
        password: '',
        description: '환영합니다.',
        align: 'left'
      },
      games: [{value: 'video', label: '화상회의'}, {value: 'mafia',label: '마피아'}, {value: 'puzzle', label: '퍼즐',disabled: true},
              {value: 'catch-mind', label: '캐치마인드', disabled: true }, { value: 'word-mafia', label: '단어마피아', disabled: true }],
      rules: {
        title: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 20, message: '최대 20자까지 입력 가능합니다.', trigger: 'change' }
        ],
        category: [
          { required: true, message: '필수 선택 항목입니다.', trigger: 'change' },
        ],
        description: [
          { required: false, message: '설명을 입력해주세요', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
        password: [
          { required: false, message: '비밀번호를 입력해주세요', trigger: 'change' },
          { min: 4, max: 10, message: '최소 4자 최대 10자까지 입력 가능합니다.', trigger: 'change' }
        ],
      },
      btnDisabled: true,
      popupVisible: computed(() => props.open),
    })
    // 비밀방 비밀번호 체크 수정해야함
    const checkValidation = function() {
      createRoomForm.value.validate((valid) => {
        if (valid && state.form.category!=null) {
          state.btnDisabled = false
        } else {
          state.btnDisabled = true
        }
      })
    }

    const clickCreateRoom = function () {
      createRoomForm.value.validate((valid) => {
        if (valid) {
          //console.log(state.form)
          store.dispatch('root/requestCreateRoom', { title: state.form.title, category: state.form.category, private: state.form.private, password: state.form.password, description: state.form.description})
          .then(function (result) {
            //console.log(result)
            emit('closeSignupPopup')
            router.push("/game/"+ state.form.category + "/" + result.data.id)
          })
          .catch(function () {

          })
        } else {
          alert('에러 발생!')
          //alert 혹은 에러 page 이동
        }
      });
    }

    const handleClose = function () {
      state.form.title = ''
      state.form.category = ''
      state.form.private = false
      state.form.password = ''
      state.form.description = ''
      state.btnDisabled = true
      emit('closeRoomPopup')
    }

    return { createRoomForm, state, clickCreateRoom, handleClose, checkValidation}
  }
}
</script>

<style>
</style>
