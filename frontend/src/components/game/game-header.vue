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
        <span>{{state.round}}라운드 - {{state.stageTitle}}</span>
        <el-button @click="nextStage(true)" size="mini">DevTrue</el-button>
        <el-button @click="nextStage(false)" size="mini">DevFalse</el-button>
      </div>
      <span class="game-timer" v-bind:class="{ 'danger': state.danger }">
        <span class="minute">{{ padMinute(state.minute) }}</span>
        <span>:</span>
        <span class="seconds">{{ padSecond(state.second) }}</span>
        <el-button @click="startTimer(40)" size="mini">Dev</el-button>
      </span>
      <button @click="clickPass">넘어가기 {{state.clicked}}/{{state.total}}</button>
    </div>
    <!-- <div>당신은 마피아입니다. ~~~ 하세요.</div> -->
    <div class="button-wrapper">
      <el-button icon="el-icon-message" @click="clickInvite">초대하기</el-button>
      <el-tooltip class="item" effect="dark" content="도움말" placement="bottom">
        <el-button icon="el-icon-question" circle @click="clickOnQuestion" ></el-button>
      </el-tooltip>
    </div>
  </el-header>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'GameHeader',

  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()
    const stages = ["대기중","아침회의","투표","최후변론","최종투표","밤","지난 밤"]

    const state = reactive({
      stompClient: computed(() => store.getters['root/getStompClient']),
      statusIcon: 'el-icon-sunny',
      clicked: 2,
      total: 4,
      //타이머
      timer: null,
      minute: 0,
      second: 0,
      danger: false,
      //라운드
      round: 0,
      stage: 0,
      stageTitle: stages[0]
    })

    const padMinute = (val) =>{
      return val.toString().padStart(2,'0')
    }
    const padSecond = (val) =>{
      return val.toString().padStart(2,'0')
    }
    const startTimer = (val)=> {
      console.log("------------->", state.stompClient)
      state.minute = parseInt(val/60)
      state.second = parseInt(val)%60
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
      if(state.minute == 0 && state.second < 20){
        state.danger = (parseInt(state.second) % 2 == 1)? true : false
      }
    }

    const nextStage = (val) =>{
      if(val){
        if(++state.stage == 7){
          state.round++
          state.stage = 1
        }
      }else{
        state.stage += 3
      }
      state.stageTitle = stages[state.stage]
      if(state.stage == 5){
        emit('startNight')
        state.statusIcon = "el-icon-moon"
      }
      if(state.stage == 1){
        emit('startDay')
        state.statusIcon = "el-icon-sunny"
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

    return { state, clickPass, clickInvite, clickOnQuestion, padMinute, padSecond, startTimer, nextStage }
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
.game-status {
  margin-right: 10px;
}

.game-info {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);

  width: 30%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* padding: 6px 0px; */
  text-align: center;
  border-radius: 6px;
  font-size: 20px;
  background-color: #e8eef3;
}

.danger{
  color: red;
  font-weight: bold;
}

</style>
