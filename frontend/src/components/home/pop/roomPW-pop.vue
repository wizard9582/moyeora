<template>
  <el-dialog width="30%" title="비밀번호 확인" v-model="state.popupVisible" @close="handleClose">
    <div class="pwPopup" >
      <el-input label="PW:" class="pwInput" placeholder="비밀번호를 입력하세요." v-model="state.input" clearable></el-input>
      <el-button type="primary" @click="checkPW">제출</el-button>
      <el-button type="danger" @click="handleClose">취소</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'
import { useRouter } from 'vue-router';

export default {
  name:"RoomPWPop",
  props:{
    open: {
      type: Boolean,
      default: false,
    },
    pw:"",
    id:"",
    },
  setup(props, { emit }) {
    const router = useRouter()

    const state = reactive({
      input: "",
      popupVisible: computed(() => props.open),
    })
    const checkPW = ()=>{
      if(state.input === props.pw){
        emit('closePwPopup')
        router.push("/game/" + props.id)
      }else{
        alert("잘못입력하셨습니다!")
        emit('closePwPopup')
      }
    }

    const handleClose = function () {
      emit('closePwPopup')
    }
    return { state, checkPW, handleClose }
  }
}
</script>

<style>
</style>
