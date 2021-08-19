<template>
  <el-header class="game-header" :height="'70px'">
    <div class="basic-info">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="60"/>
    </div>
    <div class="game-info" v-if="state.roomType=='mafia'">
      <div class="game-status">
        <span :class="state.statusIcon"></span>
        <span>{{state.round}}라운드 - {{state.stageTitle}}</span>
      </div>
      <span class="game-timer" v-bind:class="{ 'danger': state.danger }">
        <span class="minute">{{ padMinute(state.minute) }}</span>
        <span>:</span>
        <span class="seconds">{{ padSecond(state.second) }}</span>
      </span>
      <el-button v-if="state.roomType=='mafia'" class="job-info" @click="openJob">내 직업확인</el-button>
    </div>
    <div class="button-wrapper">
      <el-button icon="el-icon-message" @click="clickInvite" :disabled="state.gameStarted">초대하기</el-button>
      <el-tooltip v-if="state.roomType=='mafia'" class="item" effect="dark" content="도움말" placement="bottom">
        <el-button icon="el-icon-question" circle @click="clickOnQuestion" ></el-button>
      </el-tooltip>
    </div>
  </el-header>
  <!-- 경찰 조사 다이얼로그 -->
  <el-dialog
    title="수사하기"
    v-model="state.detectOpen"
    width="30%"
    :before-close="handleClose">
    <span>수사할 플레이어를 선택하세요!</span>
    <el-table
      :data="state.participantsList"
      highlight-current-row
      @current-change="clickDetectPlayer"
      :row-style = "tableRowClassName"
    >
      <el-table-column property="userId" label="Player"></el-table-column>
    </el-table>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="detectChoose">확인하기</el-button>
      </span>
    </template>
  </el-dialog>
  <!-- 도움말 다이얼로그 -->
  <el-dialog title="도움말" v-model="state.questionOpen" width="40%" :before-close="handleClose">
    <el-image style="width: 100%; height: auto" :src="require('@/assets/img-mafia-rule.png')" :fit="contain"></el-image>
  </el-dialog>
</template>

<script>
import { reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'GameHeader',

  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()
    const stages = ["아침회의","투표","최후변론","최종투표","밤","지난 밤"]
    class Queue {
      constructor() {
        this._arr = [];
      }
      enqueue(item) {
        this._arr.push(item);
      }
      dequeue() {
        return this._arr.shift();
      }
      peek() {
        return this._arr[0];
      }
    }
    const queue = new Queue();

    const state = reactive({
      gameStarted: false,
      roomType: route.params.type,
      detectOpen: false,
      questionOpen: false,
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      myJob: computed(() => store.getters['root/getMyJob']),
      jobList: computed(() => store.getters['root/getMafiaRoles']),
      gameTime: computed(() => store.getters['root/getGameTime']),
      gameRefresh: computed(() => store.getters['root/getGameRefresh']),
      mylife: computed(() => store.getters['root/getMylife']),
      statusIcon: 'el-icon-sunny',
      selectedPlayer: null,
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
    if(state.roomType=="mafia"){
      state.questionOpen = true
      document.body.className = 'morning'
    }else{
      document.body.className = 'white'
    }
    const skyloop = require('@/assets/sounds/skyloop.mp3')
    let audio = new Audio(skyloop)
    if(state.roomType == 'mafia'){
      audio.play();
    }

    const padMinute = (val) =>{
      return val.toString().padStart(2,'0')
    }

    const padSecond = (val) =>{
      return val.toString().padStart(2,'0')
    }

    const startTimer = (val)=> {
      state.gameStarted = true
      //console.log("------------->", state.stompClient)
      nextStage(val)
      state.minute = parseInt(val/60)
      state.second = parseInt(val)%60
      state.timer = setInterval(() => countdown(), 1000);
      //console.log('state.timer : ', state.timer)
    }

    const countdown = () =>{
      if(state.second <= 0 && state.minute == 0){
        clearInterval(state.timer)
        state.second == 0
        queue.dequeue()
        if(queue._arr.length > 0){
          let nextTime = queue.peek()
          startTimer(nextTime)
        }
      }else if(state.second == 0 && state.minute != 0){
          state.minute --
          state.second = 59
      }else{
        state.second --
      }
      //  타이머에 색 입히기
      if(state.minute == 0 && state.second < 20){
        state.danger = (parseInt(state.second) % 2 == 1)? true : false
      }
    }

    const nextStage = (val) =>{
      audio.pause()
      const morningSound = require('@/assets/sounds/morning.mp3')
      const nightSound = require('@/assets/sounds/night.mp3')
      const judgeSound = require('@/assets/sounds/judge.mp3')
      const doctorSound = require('@/assets/sounds/doctor.mp3')
      const policeSound = require('@/assets/sounds/police.mp3')
      const mafiaSound = require('@/assets/sounds/mafia.mp3')

      if(val == 31){
        state.round ++
        state.stage = 0
        audio = new Audio(morningSound)
        audio.volume = 0.2
        audio.play()
      }else if(val == 15){
        state.stage = 1
      }else if(val == 23){
        state.stage = 2
      }else if(val == 11){
        audio = new Audio(judgeSound)
        audio.play()
        state.stage = 3
      }else if(val == 17){
        state.stage = 4
        audio = new Audio(nightSound)
        audio.volume = 0.2
        audio.play()
        let gun = new Audio(mafiaSound)
        let police = new Audio(policeSound)
        setTimeout(()=>{gun.play()}, 5000)
        setTimeout(()=>{police.play()}, 8000)
      }else if(val == 8){
        state.stage = 5
        let amb = new Audio(doctorSound)
        setTimeout(()=>{amb.play()}, 1000)
      }
      state.stageTitle = stages[state.stage]
      if(state.stage == 4){
        emit('startNight')
        state.statusIcon = "el-icon-moon"
        document.body.className = 'night'
        if(state.myJob == '경찰' && state.mylife){
          state.detectOpen = true
        }
      }
      if(state.stage == 0){
        emit('startDay')
        state.statusIcon = "el-icon-sunny"
      }
      //console.log("stage---------->", queue)
    }

    // 초대하기 팝업 열기
    const clickInvite = () => {
      emit('openInvitePopup')
      console.log('invite')
    }

    // 도움말 열기
    const clickOnQuestion = () => {
      state.questionOpen = true
    }

    const getRoomInfo = () => {
      store.dispatch('root/requestRoomInfo', {roomId: route.params.no})
      .then((result) => {
        //console.log('result : ', result)
      })
      .catch((error) => {
        //console.log(error)
      })
    }

    // 넘어가기 클릭
    const clickPass = () => {
      getRoomInfo()
      //console.log('PASS')
    }

    const openJob = () =>{
        var job = new Audio('http://soundbible.com/mp3/Air Plane Ding-SoundBible.com-496729130.mp3')
        job.volume = 0.3
        job.play()
        ElMessage({
          dangerouslyUseHTMLString: true,
          message: '<strong>당신의 직업은 <i><strong>' + state.myJob + '</strong></i> 입니다</strong>',
          duration: 10000,
        });
    }

    const clickDetectPlayer = function (val) {
      state.selectedPlayer = val
    }

    const detectChoose = () => {
      // console.log("-------->직업:", state.jobList)
      //직업확인하는 로직, 태그 후처리
      let answer = ""
      state.jobList.forEach(target => {
        if(target.userId === state.selectedPlayer.userId){
          answer = target.role
        }
      });
      ElMessage({
        dangerouslyUseHTMLString: true,
        message: '<strong>선택하신 대상의 직업은 <i><strong>' + answer + '</strong></i> 입니다</strong>',
        duration: 10000,
      });
      state.detectOpen = false
    }

    watch(() => state.gameTime,
      (gameTime, prevGameTime) => {
        //console.log("---------->",queue)
        //console.log("&&&&*&*&*&*&&*&*&*&&*8---->",gameTime)
        if(!(gameTime <= 0)){
          if(queue._arr.length == 0){
            //console.log("new timer", queue)
            //console.log("round------------->",state.gameRound)
            startTimer(gameTime)
          }
          queue.enqueue(gameTime)
        }
      }
    )

    watch(()=> state.gameRefresh, ()=>{
      state.gameStarted = false,
      state.round = 0
      state.stage = 0
      state.stageTitle = stages[0]
      emit('startDay')
      state.statusIcon = "el-icon-sunny"
      document.body.className = 'morning'
    })

    return { state, clickPass, clickInvite, clickOnQuestion, padMinute, padSecond, startTimer, nextStage, clickDetectPlayer, detectChoose, openJob }
  }
}
</script>

<style>
</style>
