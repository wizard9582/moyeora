<template>
  <el-header class="game-header">
    <div class="basic-info">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="60"/>
      <!-- <div>
        <span class="gameroom-desc">김싸피의 마피아게임</span>
        <span><i class="el-icon-user-solid"></i> 10/10</span>
      </div> -->
    </div>
    <div class="job-info" v-if="state.roomType=='mafia'">
      <span>당신은 {{state.myJob}} 입니다.</span>
    </div>
    <div class="game-info" v-if="state.roomType=='mafia'">
      <div class="game-status">
        <span :class="state.statusIcon"></span>
        <span>{{state.round}}라운드 - {{state.stageTitle}}</span>
        <!-- <el-button @click="nextStage(true)" size="mini">DevTrue</el-button>
        <el-button @click="nextStage(false)" size="mini">DevFalse</el-button> -->
      </div>
      <span class="game-timer" v-bind:class="{ 'danger': state.danger }">
        <span class="minute">{{ padMinute(state.minute) }}</span>
        <span>:</span>
        <span class="seconds">{{ padSecond(state.second) }}</span>
        <!-- <el-button @click="startTimer(10)" size="mini">Dev</el-button> -->
      </span>
      <button @click="clickPass">넘어가기 {{state.clicked}}/{{state.total}}</button>
    </div>
    <!-- <div>당신은 마피아입니다. ~~~ 하세요.</div> -->
    <div class="button-wrapper">
      <el-button @click="state.detectOpen = true">dev.Police</el-button>
      <el-button icon="el-icon-message" @click="clickInvite">초대하기</el-button>
      <el-tooltip class="item" effect="dark" content="도움말" placement="bottom">
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
    <span>
      수사할 시민을 클릭하세요. <br>
      <el-tag class="user-tag" :key="user" v-for="user in state.participantsList" @click="detectChoose(user)"> {{user.userId}} </el-tag>
    </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="state.detectOpen = false">확인</el-button>
      </span>
    </template>
  </el-dialog>
  <!-- 도움말 다이얼로그 -->
  <el-dialog title="도움말" v-model="state.questionOpen" width="30%" :before-close="handleClose">
    <el-image style="width: 100%; height: auto" :src="require('@/assets/img-mafia-rule.png')" :fit="contain"></el-image>
  </el-dialog>
</template>

<script>
import { reactive, computed, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

export default {
  name: 'GameHeader',

  setup(props, {emit}) {
    const store = useStore()
    const route = useRoute()
    const stages = ["대기중","아침회의","투표","최후변론","최종투표","밤","지난 밤"]
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
      roomType: route.params.type,
      detectOpen: false,
      questionOpen: false,
      stompClient: computed(() => store.getters['root/getStompClient']),
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      myJob: computed(() => store.getters['root/getMyJob']),
      jobList: computed(() => store.getters['root/getMafiaRoles']),
      gameTime: computed(() => store.getters['root/getGameTime']),
      gameRound: computed(() => store.getters['root/getGameRound']),
      statusIcon: 'el-icon-sunny',
      clicked: 2,
      total: 4,
      //타이머
      timer: null,
      minute: 0,
      second: 0,
      danger: false,
      //라운드
      round: 1,
      stage: 1,
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
      console.log('state.timer : ', state.timer)
    }

    const countdown = () =>{
      if(state.second == 0 && state.minute != 0){
          state.minute --
          state.second = 59
      }else if(state.second != 0){
        state.second --
      }else{
        clearInterval(state.timer)
        nextStage(true)
      }

      //  타이머에 색 입히기
      if(state.minute == 0 && state.second < 20){
        state.danger = (parseInt(state.second) % 2 == 1)? true : false
      }
    }

    const nextStage = (val) =>{

      if(state.stage == 2){
        //아침투표 끝, 로직 구현
      }else if(state.stage == 4){
        //최종투표 끝, 로직 구현
      }

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
      //console.log("stage---------->", queue)
      queue.dequeue()
      if(queue._arr.length > 0){
        let nextTime = queue.peek()
        startTimer(nextTime)
      }
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
      console.log('PASS')
    }

    const detectChoose = (user) => {
      //console.log(user)
      //console.log("-------->직업:", state.jobList)
      //직업확인하는 로직, 태그 후처리
      let answer = ""
      state.jobList.forEach(target => {
        if(target.userId == user.userId){
          answer = target.role
        }
      });

      alert("선택하신 대상의 직업은 " + answer + "입니다.")
      state.detectOpen = false
    }

    watch(
      () => state.gameTime,
      (gameTime, prevGameTime) => {
        //console.log("---------->",queue)
        //console.log(queue.length)
        if(gameTime!=0){
          if(queue._arr.length == 0){
            //console.log("new timer", queue)
            startTimer(gameTime)
          }
          queue.enqueue(gameTime)
        }
      }
    )

    return { state, clickPass, clickInvite, clickOnQuestion, padMinute, padSecond, startTimer, nextStage, detectChoose }
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

.job-info {
  position: absolute;
  left: 20%;
  transform: translateX(-50%);

  width: 15%;
  display: flex;
  text-align: center;
  border-radius: 6px;
  font-size: 15px;
  background-color: #e8eef3;
}

.danger{
  color: red;
  font-weight: bold;
}

.user-tag{
  min-width: 30px;
  max-width: 30%;
  display: inline-block;
  cursor: pointer;
  margin: 5px;
}
</style>
