<template>
  <el-header class="game-header">
    <div class="basic-info">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="60"/>
      <!-- <div>
        <span class="gameroom-desc">김싸피의 마피아게임</span>
        <span><i class="el-icon-user-solid"></i> 10/10</span>
      </div> -->
    </div>
    <div class="game-info">
      <div class="game-status">
        <span :class="state.statusIcon"></span>
        <span>토론</span>
      </div>
      <span class="game-timer">
        <span class="minute">{{ padMinute(state.minute) }}</span>
        <span>:</span>
        <span class="seconds">{{ padSecond(state.second) }}</span>
        <el-button @click="startTimer(1)">time</el-button>
      </span>
      <button @click="clickPass">넘어가기 {{state.clicked}}/{{state.total}}</button>
    </div>
    <!-- <div>당신은 마피아입니다. ~~~ 하세요.</div> -->
    <div class="button-wrapper">
      <el-button icon="el-icon-message" @click="clickInvite"></el-button>
      <el-button icon="el-icon-question" circle @click="clickOnQuestion" ></el-button>
    </div>
  </el-header>
</template>

<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'GameHeader',

  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()

    const state = reactive({
      statusIcon: 'el-icon-sunny',
      clicked: 2,
      total: 4,
      timer: null,
      minute: 0,
      second: 0
    })

    const padMinute = (val) =>{
      return val.toString().padStart(2,'0')
    }
    const padSecond = (val) =>{
      return val.toString().padStart(2,'0')
    }
    const resetTime = (val)=>{
      state.minute = val
      state.second = 0
    }
    const startTimer = (val)=> {
      resetTime(val)
      state.timer = setInterval(() => countdown(), 1000);
    }
    const countdown = () =>{
      if(state.second == 0 && state.minute != 0){
          state.minute --
          state.second = 59
      }else if(state.second != 0){
        state.second --
      }else{
        clearInterval(state.timer)
        alert('타이머 종료!')
        //위에 alert대신 작동할 로직 넣으면 될듯
      }
    }

    // 초대하기 팝업 열기
    const clickInvite = () => {
      emit('openInvitePopup')
      console.log('invite')
    }

    // 도움말 열기
    const clickOnQuestion = () => {
      console.log('도움말')
    }

    const getRoomInfo = () => {
      store.dispatch('root/requestRoomInfo', {roomId: route.params.no})
      .then((result) => {
        console.log('result : ', result)
      })
      .catch((error) => {
        console.log(error)
      })
    }

    // 넘어가기 클릭
    const clickPass = () => {
      getRoomInfo()
      console.log('PASS')
    }

    return { state, clickPass, clickInvite, clickOnQuestion, padMinute, padSecond, resetTime, startTimer }
  }
}
</script>

<style>
.game-header {
  padding: 10px 20px;
  border-bottom: 2px solid lightgray;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  z-index: 9999;
}

.game-header .basic-info {
  display: flex;
  justify-content: center;
  align-items: center;
}

.gameroom-desc {
  padding: 0px 15px;
}

.game-info {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);

  width: 25%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* padding: 6px 0px; */
  text-align: center;
  border-radius: 6px;
  font-size: 20px;
  background-color: #e8eef3;
}

</style>
