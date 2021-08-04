<template>
  <el-dialog title="방 만들기" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="createRoomForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="title" label="방제" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="게임" :label-width="state.formLabelWidth">
        <el-select v-model="state.form.type" placeholder="Select">
          <el-option v-for="item in state.games" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="lock" label="비밀방 설정" :label-width="state.formLabelWidth">
        <el-checkbox v-model="state.form.lock"></el-checkbox>
      </el-form-item>
      <el-form-item v-if="state.form.lock" prop="password" label="비밀번호" :label-width="state.formLabelWidth">
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
        type: '',
        lock: false,
        password: '',
        desc: '',
        align: 'left'
      },
      games: [{value: 'video',label: '화상회의'}, {value: 'mafia',label: '마피아'}, {value: 'puzzle',label: '퍼즐',disabled: true},
              {value: 'catch-mind', label: '캐치마인드', disabled: true }, { value: 'word-mafia', label: '단어마피아', disabled: true }],
      rules: {
        title: [
          { required: true, message: '필수 입력 항목입니다.', trigger: 'change' },
          { max: 20, message: '최대 20자까지 입력 가능합니다.', trigger: 'change' }
        ],
        type: [
          { required: true, message: '필수 선택 항목입니다.', trigger: 'change' },
        ],
        desc: [
          { required: false, message: '설명을 입력해주세요', trigger: 'change' },
          { max: 30, message: '최대 30자까지 입력 가능합니다.', trigger: 'change' }
        ],
        password: [
          { required: false, message: '비밀번호를 입력해주세요', trigger: 'change' },
          { min: 4, max: 10, message: '최대 10자까지 입력 가능합니다.', trigger: 'change' }
        ],
      },
      btnDisabled: true,
      popupVisible: computed(() => props.open),
    })
    // 비밀방 비밀번호 체크 수정해야함
    const checkValidation = function() {
      createRoomForm.value.validate((valid) => {
        if (valid && state.form.type!=null) {
          state.btnDisabled = false
        } else {
          state.btnDisabled = true
        }
      })
    }

    const clickCreateRoom = function () {
      createRoomForm.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestCreateRoom', { title: state.form.title, type: state.form.type, lock: state.form.lock, password: state.form.password, desc: state.form.desc})
          .then(function () {
            //생성된 방으로 이동하는 router push
            //emit('closeSignupPopup')
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
      state.form.type = ''
      state.form.lock = false
      state.form.password = ''
      state.form.desc = ''
      state.btnDisabled = true
      emit('closeRoomPopup')
    }

    return { createRoomForm, state, clickCreateRoom, handleClose, checkValidation}
  }
}
</script>

<style>
</style>
