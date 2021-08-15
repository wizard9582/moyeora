<template>
  <el-dialog title="개인설정" v-model="state.popupVisible" @close="handleClose">
  <el-form ref="form" :model="state.form" label-width="120px" :label-position="left">
    <el-form-item label="테마" >
    <el-radio-group v-model="state.form.theme" :label-width="state.form.labelWidth">
      <el-radio label="기본테마"></el-radio>
      <el-radio label="어둠의자식"></el-radio>
      <el-radio label="레트로펑크"></el-radio>
      <el-radio label="여름감성"></el-radio>
      <el-radio label="핑크 핫"></el-radio>
    </el-radio-group>
    </el-form-item>
    <el-form-item label="글씨 크기" :label-width="state.form.labelWidth">
    <el-slider v-model="state.form.fontSize" :min="8" :max="16"></el-slider>
    </el-form-item>
    <el-form-item label="초대받기" :label-width="state.form.labelWidth">
    <el-radio-group v-model="state.form.alarm" size="small">
      <el-radio label="on" border>초대 받기</el-radio>
      <el-radio label="off" border>초대 받지 않기</el-radio>
    </el-radio-group>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" @click="clickAccept">저장</el-button>
    <el-button type="danger" @click="handleClose">취소</el-button>
    </el-form-item>
  </el-form>
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
      form:{
        theme: "",
        fontSize: 0,
        alarm: "",
        align: 'left',
        labelWidth : 120
      },
      popupVisible: computed(() => props.open),
    })

    const handleClose = function () {
      emit('closeSettingPopup')
    }

    const clickAccept = function() {
      //console.log(this.form.theme + " " + this.form.fontSize + " " + this.form.alarm)
      localStorage.setItem('theme', state.form.theme)
      localStorage.setItem('fontSize', state.form.fontSize)
      localStorage.setItem('alarm', state.form.alarm)
      handleClose()
    }

    let theme = localStorage.getItem('theme')
    let fontSize = localStorage.getItem('fontSize')
    let alarm = localStorage.getItem('alarm')
    if(theme!=null){
      state.form.theme = theme
      state.form.fontSize = parseInt(fontSize)
      state.form.alarm = alarm
    }else{
      state.form.theme = "기본테마"
      state.form.fontSize = 10
      state.form.alarm = "off"
    }

    return { settingForm, state, handleClose, clickAccept}
  },
}
</script>

<style>

</style>
