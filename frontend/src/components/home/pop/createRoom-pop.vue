<template>
  <el-dialog title="방 만들기" v-model="state.popupVisible" @close="handleClose">
    <el-form :model="state.form" :rules="state.rules" ref="createRoomForm" :label-position="state.form.align" @change="checkValidation" v-loading.lock="state.popupLoading">
      <el-form-item prop="title" label="방제" :label-width="state.formLabelWidth">
        <el-input v-model="state.form.title" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="게임" :label-width="state.formLabelWidth">
        <el-select v-model="value" placeholder="Select">
          <el-option v-for="item in games" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
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

data() {
      return {
        games: [{
          value: 'video',
          label: '화상회의'
        }, {
          value: 'mafia',
          label: '마피아',
        }, {
          value: 'puzzle',
          label: '퍼즐',
          disabled: true
        }, {
          value: 'catch-mind',
          label: '캐치마인드',
          disabled: true
        }, {
          value: 'word-mafia',
          label: '단어마피아',
          disabled: true
        }],
        value: ''
      }
    },

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

    }

    const handleClose = function () {
      state.form.title = ''
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
