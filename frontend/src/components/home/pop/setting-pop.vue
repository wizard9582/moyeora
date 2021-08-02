<template>
  <el-dialog title="개인설정" v-model="state.popupVisible" @close="handleClose">
  <el-form ref="form" :model="form" label-width="120px" :label-position="left">
    <el-form-item label="테마" >
    <el-radio-group v-model="form.theme" :label-width="form.labelWidth">
      <el-radio label="기본테마"></el-radio>
      <el-radio label="어둠의자식"></el-radio>
      <el-radio label="레트로펑크"></el-radio>
      <el-radio label="여름감성"></el-radio>
      <el-radio label="핑크 핫"></el-radio>
    </el-radio-group>
    </el-form-item>
    <el-form-item label="글씨 크기" :label-width="form.labelWidth">
    <el-slider v-model="form.fontSize" :min="8" :max="16"></el-slider>
    </el-form-item>
    <el-form-item label="알람설정" :label-width="form.labelWidth">
    <el-radio-group v-model="form.alarm" size="small">
      <el-radio label="on" border>알람 켜기</el-radio>
      <el-radio label="off" border>알람 끄기</el-radio>
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
  data(){
    return{
      form:{
        theme: "",
        fontSize: 0,
        alarm: "",
        align: 'left',
        labelWidth : 120
      }
    }
  },

  methods:{
    clickAccept() {
      console.log(this.form.theme + " " + this.form.fontSize + " " + this.form.alarm)
      localStorage.setItem('theme', this.form.theme)
      localStorage.setItem('fontSize', this.form.fontSize)
      localStorage.setItem('alarm', this.form.alarm)
      this.handleClose()
    },
  },

  setup(props, { emit }) {
    const store = useStore()
    const settingForm = ref(null)

    const state = reactive({
      popupVisible: computed(() => props.open),
    })

    const handleClose = function () {
      emit('closeSettingPopup')
    }

    return { settingForm, state, handleClose}
  },

  created(){
    let theme = localStorage.getItem('theme')
    let fontSize = localStorage.getItem('fontSize')
    let alarm = localStorage.getItem('alarm')
    if(theme!=null){
      this.form.theme = theme
      this.form.fontSize = parseInt(fontSize)
      this.form.alarm = alarm
    }else{
      this.form.theme = "기본테마"
      this.form.fontSize = 10
      this.form.alarm = "off"
    }
  },
}
</script>

<style>

</style>
