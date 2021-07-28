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
        <el-slider v-model="state.form.fontSize" :min="8" :max="16"></el-slider>
        <!-- <el-input-number v-model="state.form.fontSize" :min="8" :max="16"></el-input-number> -->
      </el-form-item>
      <el-form-item prop="alarm" label="알람설정" :label-width="state.formLabelWidth">
        <el-radio-group v-model="state.form.alarm" size="small">
          <el-radio label="on" border>알람 켜기</el-radio>
          <el-radio label="off" border>알람 끄기</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickAccept">저장</el-button>
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
        theme: "기본테마",
        fontSize: 10,
        alarm: "off",
        align: 'left'
      },
      popupVisible: computed(() => props.open),
    })

    const clickAccept = function () {
    }

    const handleClose = function () {
      emit('closeSettingPopup')
    }

    return { settingForm, state, clickAccept, handleClose}
  }
}
</script>

<style>

</style>
