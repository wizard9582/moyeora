<template>
  <div class="chat-wrapper">
    <div class="chat-header">채팅창</div>
    <div class="chat-main" style="overflow:auto">
      <ul class="list" v-infinite-scroll="load" infinite-scroll-disabled="disabled">
        <li v-for="i in recvList" class="list-item" :key="i">
          {{ i.fromName }} >> {{ i.toName }} :
          <br />
          {{ i.message }}
        </li>
      </ul>
    </div>
    <el-select v-model="toName" clearable>
      <el-option label="모두" value="모두"></el-option>
      <el-option
        v-for="player in state.participantsList"
        :key="player.userId"
        :label="player.userId"
        :value="player.userId"
      ></el-option>
    </el-select>
    <!-- <el-button @click="openFinalVote">확인</el-button> -->
    <!-- <el-input placeholder="모두에게" v-model="toName"></el-input> -->
    <el-input
      type="textarea"
      :rows="2"
      placeholder="Press Enter for send message."
      v-model="message"
      @keyup="sendMessage"
    ></el-input>
  </div>
</template>

<script>
import { ref, computed, reactive, h } from 'vue'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { leaveRoom, register } from '@/common/lib/conferenceroom'
import { ElMessage } from 'element-plus'


let scope = '';

export default {
  name: 'GameChat',

  setup(props, {emit}) {
    const count = ref(10);
    const loading = ref(false);
    const noMore = computed(() => count.value >= 20);
    const disabled = computed(() => loading.value || noMore.value);
    const load = () => {
      loading.value = true;
      setTimeout(() => {
        count.value += 2;
        loading.value = false;
      }, 100);
    };
    const route = useRoute();
    const roomId = route.params.no;

    const router = useRouter();
    const store = useStore();

    const state = reactive({
      participantsList: computed(() => store.getters['root/getParticipantsList']),
      ownerId: computed(()=> store.getters['root/getRoomOwner']),
      voteCountList: computed(() => store.getters['root/getVoteCount']),
      finalVoteCount: computed(() => store.getters['root/getFinalVoteCount']),
      mylife: computed(() => store.getters['root/getMylife']), // 현재 내가 죽었는지 살았는지에 대한 정보
      gameStarted: computed(() => store.getters['root/getGameStarted']),
      didMafiaKillPlayer: false,
    })

    const voteIndex = ref(0)

    const openFinalVote = () => {
      emit('openFinalVotePop')
    }

    const openDoctorVote = () => {
      emit('openDoctorVotePop')
    }

    const openMafiaVote = () => {
      emit('openMafiaVotePop')
    }

    // 타이머 시간 조정

    const morningTime = 48;
    const judgeTime = 36;
    const nightTime = 27;

    return { count, disabled, load, roomId, router, store, state, openFinalVote, openDoctorVote, openMafiaVote, morningTime, judgeTime, nightTime };
  },
  data() {
    return {
      userName: "",
      //roomId : roomNum,
      message: "",
      recvList: [],
      toName: "",
      //participantsList: [],
    }
  },
  methods: {
    sendMessage (e) {
      if(e.keyCode === 13 && this.userName !== '' && this.message !== ''){
        console.log('----------toName---------- ', this.toName)
        if(this.toName == '' || this.toName == '모두')
          this.sendToRoom()
        else
          this.sendToPerson()
        this.message = ''
        this.toName = ''
      }
    },

    sendToRoom() {
      console.log("Send message To Room "+ this.roomId +" :" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          roomId: this.roomId,
          fromName: this.userName,
          toName: this.toName,
          message: this.message
        };
        this.stompClient.send("/pub/chat/room/"+ this.roomId, JSON.stringify(msg), {});
      }
    },

    sendToPerson() {
      console.log("Send message To Perseon "+ this.toName +" : " + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          roomId: this.roomId,
          fromName: this.userName,
          toName: this.toName,
          message: this.message
        };
        this.stompClient.send("/pub/chat/room/"+ this.roomId + "/" + this.toName, JSON.stringify(msg), {});
        this.recvList.push(msg)
      }
    },

    sendToLeave() {
      console.log("Send message To Leave ");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          roomId: this.roomId,
          fromName: this.userName,
        };
        this.stompClient.send("/pub/leave/"+ this.roomId, JSON.stringify(msg), {});
      }
    },

    gameTimerStart() {
      console.log("Send message To Start game timer ");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          round: 1,
          desc: "start",
          second : 60
        };
        this.stompClient.send("/pub/game/start/"+ this.roomId, JSON.stringify(msg), {});
      }
    },

    waitSecond(room, name) {
        console.log('기다리고')
        let random = Math.floor(Math.random() * (1001 - 0)) + 0 - 500;
        setTimeout(function () {
              register(room, name);
        }, 2000-random); // 랜덤 숫자 더하기 (-0.5~0.5)
    },

    //현재 게임이 끝났는지 판단해주는 API, 주의. 방장만 호출해줄 것!
    getGameStatus() {
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
              roomId: this.roomId,
          };
        this.stompClient.send("/pub/game/end/"+ this.roomId, JSON.stringify(msg), {});
      }
    },

    connect() {
      //const store = useStore();
      this.userName = computed(() => this.store.getters['root/getUserId']);

      const serverURL = "/websocket"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      // socketConnect()
      // stompClient = Stomp.over(socket);

      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);

          this.store.commit('root/setStompClient', this.stompClient)

          // 환영 메세지
          if (this.stompClient && this.stompClient.connected) {
            const msg = {
              name: this.userName,
            };
            this.stompClient.send("/pub/hello/room/"+ this.roomId, JSON.stringify(msg), {});
          }

          // 구독 결과
          this.stompClient.subscribe('/sub/greetings/room/'+this.roomId, function (chat) {
            console.log("새로운 참가자 등장!!", chat.body);
            scope.store.dispatch('root/requestRoomInfo', { roomId: scope.roomId })
            .then((result) => {
              console.log('새로운 목록: ',result.data.member)
              scope.store.commit('root/setParticipantsList', result.data.member)

              // // voteCount 세팅하기
              // // voteCount 구조 = [{ userId : [0, pk] }]
              // let votePlayerList = new Object
              // for (let player of result.data.member) {
              //   let greetingUserId = player.userId
              //   votePlayerList[greetingUserId] = [0, player.id]
              // }
              // console.log('votePlayerList', votePlayerList)
              // scope.store.commit('root/setVoteCount', votePlayerList)
            })
          });

          this.stompClient.subscribe('/sub/bye/room/'+this.roomId, function (chat) {
            console.log("참가자 퇴장!!", chat.body);
            //this.participantsList.push(chat.body);
            scope.store.dispatch('root/requestRoomInfo', { roomId: scope.roomId })
            .then((result) => {
              console.log('새로운 목록: ',result.data.member)
              scope.store.commit('root/setParticipantsList', result.data.member)
            })
          });
          this.stompClient.subscribe("/sub/chat/room/"+this.roomId, chat => {
            let mess = JSON.parse(chat.body)
            mess.toName = '모두'
            scope.recvList.push(mess)
          });
          this.stompClient.subscribe('/sub/chat/room/'+this.roomId+'/'+this.userName, function (chat) {
            scope.recvList.push(JSON.parse(chat.body));
          });
          // for문을 사용하여 참가자 아이디 별로 구독할지, 전체 구독으로 사용자에 따른
          this.stompClient.subscribe('/sub/vote/room/'+this.roomId, function (chat) {
            let voteResult = JSON.parse(chat.body)
            console.log("투표 받았다", voteResult)

            if (voteResult.fromName === 'doctor') {
              scope.store.commit('root/setDoctorSelectPlayer', voteResult.toName)
            } else if (voteResult.fromName === 'mafia') {
              let currentMafiaSelectPlayer = scope.store.getters['root/getMafiaSelectPlayer']
              if (!currentMafiaSelectPlayer) {
                scope.store.commit('root/setMafiaSelectPlayer', voteResult.toName)
              } else {
                if (currentMafiaSelectPlayer !== voteResult.toName) {
                  // 마피아 간의 의견 충돌이 나면 mafia로 처리하며 투표를 무효화한다.
                  scope.store.commit('root/setMafiaSelectPlayer', 'mafia')
                }
              }
            } else if(voteResult.fromName === 'final'){
                if(voteResult.toName == 'kill')
                  scope.store.commit('root/voteFinalVote', 'kill')
                else
                  scope.store.commit('root/voteFinalVote', 'save')
            } else {
              scope.store.commit('root/voteTo', voteResult['toName'])

              // 받은 투표 수를 화면에 표시
              const voteSpace = document.querySelector(`#${voteResult['toName']}`).children[1].children[1]
              console.log(document.querySelector(`#${voteResult['toName']}`).children[1])
              let voteImg = document.createElement('img');
              let leftIndex = 100 + 25*scope.state.voteCountList[voteResult['toName']][0]
              voteImg.src = require('@/assets/favicon-white.png');

              voteImg.style.position = 'absolute'
              voteImg.style.top = '-8px'
              voteImg.style.width = '40px'
              voteImg.style.height = '40px'
              voteImg.style.left = `${leftIndex}px`

              voteImg.id = `vote-${voteResult['toName']}-${leftIndex}`
              voteSpace.appendChild(voteImg)
	            // let voteImg = document.createElement('img');
              // voteImg.src = require('@/assets/favicon.png');
              // voteImg.style = 'height:50%; opacity:1; position:absolute;'
            }
          });

          this.stompClient.subscribe('/sub/leave/'+this.roomId, function (chat) {
            scope.disconnectSocket()
            console.log("나가라")
            scope.router.push("/home/" + 'all')
            leaveRoom()
          });

          ////////////// 게임 로직 ///////////////
          var myRole = "";
          // 직업 분배 결과
          this.stompClient.subscribe('/sub/game/start/'+this.roomId+"/"+this.userName, function (chat) {
            scope.store.commit('root/setMylife', true);
            // 직업 연동 아직 안됨
            console.log("타이머 0 (직업 분배 결과) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            let msg = "당신의 직업은 "+ rchat.role +"입니다.";
            if(rchat.same){
              msg += "\n마피아 목록: "
              msg += rchat.same.substr(1,rchat.same.length-2)
            }
            if(rchat.same){
              msg += "\n마피아 목록: "
              msg += rchat.same.substr(1,rchat.same.length-2)
              ElMessage({
                message:  h('p', null, [
                  h('strong', null, '당신의 직업은 '),
                  h('i', { style: ['color: tomato', 'font-weight: bold'] }, rchat.role),
                  h('strong', null, ' 입니다.\n'),
                  h('strong', null, '같은 마피아는  '),
                  h('i', { style: ['color: tomato', 'font-weight: bold'] }, rchat.same.substr(1,rchat.same.length-2)),
                  h('strong', null, ' 입니다.'),
                ]),
                duration: 10000,
              })
            }else{
              ElMessage({
                message: h('p', null, [
                  h('strong', null, '당신의 직업은 '),
                  h('i', { style: ['color: tomato', 'font-weight: bold'] }, rchat.role),
                  h('strong', null, ' 입니다.'),
                ]),
                duration: 10000,
              })
            }
            // ElMessage({
            //   dangerouslyUseHTMLString: true,
            //   message: '<strong>당신의 직업은 <i>' + msg + '</i>입니다</strong>',
            // });
            // alert(msg)
            myRole = rchat.role;
            scope.store.commit('root/setMyJob', myRole)
            // 경찰이라면 참가자들의 직업을 미리 받아옴 => 경찰이 아닌 사람도 죽었을 때 직업을 밝히기 위해 받아오도록 바꿈.
            //if(myRole == "police"){
              scope.store.dispatch('root/requestByPolice', { roomId: scope.roomId })
              .then((result) => {
                //console.log("경찰이 얻어온 직업들 : ", result.data)
                scope.store.commit('root/setMafiaRoles', result.data);
                console.log('참가자들의 직업 뷰엑스 확인, 모닝x : ',scope.store.getters['root/getMafiaRoles'])
              })
              .catch((error) => {
                console.log(error)
              })
            //}
          });
          // 메세지를 받을 때마다 게임 승리여부 판단과 라운드 체크를 해주어야 합니다.
          this.stompClient.subscribe('/sub/game/morning/'+this.roomId, function (chat) {
            console.log("타이머 1 (아침, 투표) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            scope.store.commit('root/setGameRound', {round: rchat.round, second: rchat.second-1})

            // 아침 시작
            if(rchat.desc === 'morning'){
              ElMessage({message: h('strong', null, '아침이 되었습니다.')})
              // 투표 및 선택된 사람 초기화
              scope.store.commit('root/newRoundStart');
            }

            if(rchat.desc === 'vote') {
              ElMessage({message: h('strong', null, '투표가 시작되었습니다.')})
              scope.store.commit('root/startVote');
            }

            if(rchat.desc=='end'){
              // 투표 리프레시
              scope.store.commit('root/endVote');
              document.getElementById('participants').style.pointerEvents = 'auto'

              // 투표 완료 후 처리
              let maxUser = ['', 0, 0] // 유저 아이디, 총 득표수, 유저 PK
              let tiebreaker = false // 동점자 유무
              let totalResult = scope.state.voteCountList
              for (let id in totalResult) {
                if (totalResult[id][0] > maxUser[1]) {
                  maxUser = [id, totalResult[id][0], totalResult[id][1]]
                  tiebreaker = false
                } else if (totalResult[id][0] === maxUser[1]) {
                  tiebreaker = true
                }
              }

              let msg = {}
              if (tiebreaker) { // 동점자가 있는 경우
                msg = { round: 3, second: scope.nightTime }
                scope.store.commit('root/skipStage', {value: true})
                ElMessage({
                  message: h('strong', null, '아침 투표에서 동점으로 인해 아무도 죽지 않았습니다.'),
                  type: 'warning',
                  duration: 10000,
                })
                // scope.recvList.push({message:`동점으로 인해 아무도 죽지 않았습니다.`})
                // 방장만 메시지를 보낸다!
                if (scope.userName === scope.state.ownerId) {
                  scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {});
                }
              } else { // 동점자가 없는 경우
                msg = { round: 2, second : scope.judgeTime };
                scope.store.commit('root/skipStage', {value: false})
                scope.store.commit('root/setFinalVotePlayer', maxUser[0])
                 if (scope.userName === scope.state.ownerId) {
                   scope.stompClient.send("/pub/game/judge/"+ scope.roomId, JSON.stringify(msg), {});
                 }
              }
              console.log(tiebreaker, msg)
            }

            // 투표들 지우기
            for (let player of scope.state.participantsList) {
              let playerVoteSpace = document.querySelector(`#vote-space-${player.userId}`)
              console.log('투표 진행 상황 지우기!!! : ', playerVoteSpace)
              while (playerVoteSpace.hasChildNodes) {
                playerVoteSpace.removeChild(playerVoteSpace.firstChild)
              }
            }
          });

          this.stompClient.subscribe('/sub/game/judge/'+this.roomId, function (chat) {
            console.log("타이머 2 (변론, 투표) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            scope.store.commit('root/setGameRound', {round: rchat.round, second: rchat.second-1})

            let judgePerson = scope.store.getters['root/getFinalVotePlayer']
            if(rchat.desc == 'judge'){
              ElMessage({message: h('strong', null, judgePerson+'님, 최후의 변론 시간입니다.')})
            }

            // 최종 투표 팝업 오픈
            if(rchat.desc === 'finalvote') {
              ElMessage({message: h('strong', null, judgePerson+'님의 운명을 결정할 투표가 시작되었습니다.')})
              scope.store.commit('root/startVote');
              scope.store.commit('root/setFinalVoteCount')
              if (scope.state.mylife) {
                scope.openFinalVote()
              }
              for (let player of scope.state.participantsList) {
                try{
                  // 투표들 지우기
                  for (let player of scope.state.participantsList) {
                    let playerVoteSpace = document.querySelector(`#vote-space-${player.userId}`)
                    console.log('투표 진행 상황 지우기!!! : ', playerVoteSpace)
                    while (playerVoteSpace.hasChildNodes) {
                      playerVoteSpace.removeChild(playerVoteSpace.firstChild)
                    }
                  }
                  // console.log('확인: ',document.querySelector(`#${player.userId}`))
                  // const voteSpan = document.querySelector(`#${player.userId}`).children[2]
                  // voteSpan.innerText = '';
                }catch{
                  // console.log('유령')
                }

              }
            }
            if(rchat.desc === 'end'){
              scope.store.commit('root/endVote');
              let msg = {}
              let playerPK = ''
              console.log("최종 투표 사람1 : ",scope.store.getters['root/getFinalVotePlayer'])
              console.log("최종 투표 사람2 : ",computed(() => scope.store.getters['root/getFinalVotePlayer']))
              let finalVotePlayer = scope.store.getters['root/getFinalVotePlayer'];
              if (scope.state.finalVoteCount[0] > scope.state.finalVoteCount[1]) {
                // 죽이기로 결정
                for (let player of scope.state.participantsList) {
                  if (player.userId === finalVotePlayer) {
                    playerPK = player.id
                    /// player.death = true;
                    break
                  }
                }
                scope.store.commit('root/setDeath',finalVotePlayer);
                try{
                  let img = document.querySelector(`#${finalVotePlayer}`).children[0].children[0];
                  img.style = 'height:50%;opacity:1;position:absolute;'
                }catch{
                  console.log('유령 도장')
                }
                // 죽은 사람 직업 찾기
                let roles = scope.store.getters['root/getMafiaRoles'];
                let deadRole = '';
                for (let player of roles) {
                  if (player.userId === finalVotePlayer) {
                    deadRole = player.role;
                    break
                  }
                }
                if(deadRole == 'mafia') deadRole = "마피아 "
                else if(deadRole == 'police') deadRole = "무고한 경찰 "
                else if(deadRole == 'doctor') deadRole = "무고한 의사 "
                else deadRole = "무고한 시민 "

                ElMessage({
                  message: h('p', null, [
                    h('strong', null, '최종투표에서 '),
                    h('i', { style: ['color: tomato', 'font-weight: bold'] }, deadRole+finalVotePlayer),
                    h('strong', null, ' 가 죽었습니다.'),
                  ]),
                  type: 'danger',
                  duration: 10000,
                })
                // scope.recvList.push({message:`최종투표에서 ${finalVotePlayer}가 죽었습니다.`})
                console.log('죽은사람 처리 확인: ', scope.store.getters['root/getParticipantsList']);
                // 만약 죽은게 나라면...ㅠㅠㅠㅠㅠ
                if(finalVotePlayer == scope.userName){
                  scope.store.commit('root/setMylife', false);
                }
                if(scope.userName == scope.state.ownerId){
                  msg = { name: `judge,${playerPK}` }
                  scope.stompClient.send("/pub/game/end/"+ scope.roomId, JSON.stringify(msg), {});
                }
              }else{
                // 살리기로 결정
                ElMessage({
                  message: h('p', null, [
                    h('strong', null, '최종투표에서 '),
                    h('i', { style: ['color: tomato', 'font-weight: bold'] }, finalVotePlayer),
                    h('strong', null, ' 가 구사일생 했습니다.'),
                  ]),
                  type: 'success',
                  duration: 10000,
                })
                // scope.recvList.push({message:`최종투표에서 ${finalVotePlayer}가 구사일생 했습니다.`})
                if(scope.userName == scope.state.ownerId){
                  msg = {round : 1, second : scope.nightTime}
                  scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {});
                }
              }

            }
          });

          this.stompClient.subscribe('/sub/game/night/'+this.roomId, function (chat) {
            console.log("타이머 3 (저녁, 결과) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            scope.store.commit('root/setGameRound', {round: rchat.round, second: rchat.second-1})
            if (rchat.desc === 'night') {
              ElMessage({message: h('strong', null, '밤이 되었습니다.'),duration: 10000,})
              // 살아있으면
              if(scope.state.mylife){
                leaveRoom()
                console.log("내 직업",myRole)
                if(myRole=='mafia' && scope.state.mylife){
                  // 마피아인 경우 : 마피아끼리 모임
                  scope.waitSecond(scope.roomId+"/mafia",scope.userName)
                  scope.openMafiaVote()
                }else if(myRole == 'doctor' && scope.state.mylife){
                  // 의사인 경우 : 투표창 열림
                  scope.openDoctorVote()
                }
                else if(myRole == 'police'){
                  // 경찰인 경우 : 투표창 열림

                }
              }else{
                // 죽은사람들은 원래 기본세션 그대로 연결해놓는다.

              }
            } else if (rchat.desc === 'result') {
              /////////////////////////////////////////// 세션 합치기
              if(myRole=='mafia'){
                // 마피아인 경우 : 마피아끼리 모임
                leaveRoom()
              }
              if(scope.state.mylife)
                scope.waitSecond(scope.roomId,scope.userName)

              /////////////////////////////////////////// 밤 사이에 일어난 결과 발표
              let doctorSelectPlayer = scope.store.getters['root/getDoctorSelectPlayer']
              let mafiaSelectPlayer = scope.store.getters['root/getMafiaSelectPlayer']
              if (doctorSelectPlayer === mafiaSelectPlayer || !mafiaSelectPlayer || mafiaSelectPlayer === 'mafia') {
                // 마피아가 제거하지 못하는 경우
                // let testMessage = new Object

                ElMessage({
                  message: h('strong', null, '밤 사이 아무일도 일어나지 않았습니다.'),
                  type: 'warning',
                  duration: 10000,
                })
                // scope.recvList.push({message:'밤 사이 아무일도 일어나지 않았습니다.'})
                scope.state.didMafiaKillPlayer = false
              } else {
                // 마피아가 제거하는 경우
                console.log('마피아가 죽인 사람: ',mafiaSelectPlayer);
                scope.store.commit('root/setDeath',mafiaSelectPlayer);
                try{
                  let img = document.querySelector(`#${mafiaSelectPlayer}`).children[0].children[0];
                  img.style = 'height:50%;opacity:1;position:absolute;'
                }catch{
                  console.log('도장 유령')
                }
                // 그게 나라면...!!!ㅠㅠㅠㅠㅠㅠ
                if(mafiaSelectPlayer == scope.userName){
                  scope.store.commit('root/setMylife', false);
                }
                let roles = scope.store.getters['root/getMafiaRoles'];
                let deadRole = '';
                for (let player of roles) {
                  if (player.userId === mafiaSelectPlayer) {
                    deadRole = player.role;
                    break
                  }
                }
                if(deadRole == 'mafia') deadRole = "마피아 "
                else if(deadRole == 'police') deadRole = "무고한 경찰 "
                else if(deadRole == 'doctor') deadRole = "무고한 의사 "
                else deadRole = "무고한 시민 "

                ElMessage({
                  message: h('p', null, [
                    h('strong', null, '마피아가 '),
                    h('i', { style: ['color: black', 'font-weight: bold'] }, deadRole),
                    h('i', { style: ['color: tomato', 'font-weight: bold'] }, mafiaSelectPlayer),
                    h('strong', null, ' 를 죽였습니다.'),
                  ]),
                  type: 'danger',
                  duration: 10000,
                })
                // scope.recvList.push({message:`마피아가 무고한 ${deadRole}, ${mafiaSelectPlayer}를 죽였습니다.`})
                scope.state.didMafiaKillPlayer = true
              }

            } else if(rchat.desc=='end'){
                for (let player of scope.state.participantsList) {
                  if (player.death){
                    try{
                      let img = document.querySelector(`#${player.userId}`).children[0].children[0];
                      img.style = 'height:50%;opacity:1;position:absolute;'
                    }catch{
                      console.log('도장유령')
                    }
                  }
                }

              // 죽은 사람이 있다면
                if (scope.state.didMafiaKillPlayer) {
                  let playerPK = ''
                  let deadPlayer = scope.store.getters['root/getMafiaSelectPlayer']
                  for (let player of scope.state.participantsList) {
                    if (player.userId === deadPlayer) {
                      playerPK = player.id
                      player.death = true;
                      break
                    }
                  }
                  if((scope.userName == scope.state.ownerId)){
                    const msg = {
                      name: `night,${playerPK}`,
                    }
                    scope.stompClient.send("/pub/game/end/"+ scope.roomId, JSON.stringify(msg), {});
                  }
                }else{ // 죽은 사람이 없다면
                 if((scope.userName == scope.state.ownerId)){
                    const msg = {
                      round : 1,
                      second : scope.morningTime,
                    }
                    scope.stompClient.send("/pub/game/morning/"+ scope.roomId, JSON.stringify(msg), {});
                 }
                }
            }
          });

          //게임 종료 판단하기
          this.stompClient.subscribe('/sub/game/end/'+this.roomId, function (res) {
              console.log("게임 진행 상황 : "+res.body);
              const resMessage = res.body.split(',')
              if(resMessage[1]=='on'){
                //게임 계속 진행
                if (resMessage[0] === 'judge') {
                  const msg = { round : 1, second : scope.nightTime }
                   if (scope.userName === scope.state.ownerId) {
                    scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {})
                   }
                } else if (resMessage[0] === 'night') {
                  const msg = { round : 1, second : scope.morningTime }
                   if (scope.userName === scope.state.ownerId) {
                    scope.stompClient.send("/pub/game/morning/"+ scope.roomId, JSON.stringify(msg), {})
                   }
                }
              }else if(resMessage[1]=='citizen'){
                //시민 이김
                console.log('시민 이겻을 때 state : ',scope.state.gameStarted)
                //if (scope.state.gameStarted) {
                  ElMessage({
                    message: h('strong', null, '시민 승!!'),
                    type: 'success',
                    duration: 10000,
                  })
                  // alert('시민 승!!!')
                  scope.store.commit('root/resetDeath');
                  scope.store.commit('root/endGame');
                  scope.store.commit('root/setMafiaRoles', null);
                  scope.store.commit('root/setMylife', true);

                  // 도장 제거
                  for (let player of scope.state.participantsList) {
                    try{
                      let img = document.querySelector(`#${player.userId}`).children[0].children[0];
                      img.style = 'height:50%;opacity:0;position:absolute;'
                    }catch{
                      console.log('도장유령')
                    }
                  }
                //}
              }else{
                //if (scope.state.gameStarted) {
                  //마피아 이김
                  ElMessage({
                    message: h('strong', null, '마피아 승!!'),
                    type: 'success',
                    duration: 10000,
                  })
                  // alert('마피아 승!!!')
                  scope.store.commit('root/resetDeath');
                  scope.store.commit('root/endGame');
                  scope.store.commit('root/setMafiaRoles', null);
                  scope.store.commit('root/setMylife', true);

                  // 도장 제거
                  for (let player of scope.state.participantsList) {
                    try{
                      let img = document.querySelector(`#${player.userId}`).children[0].children[0];
                      img.style = 'height:50%;opacity:0;position:absolute;'
                    }catch{
                      console.log('도장유령')
                    }
                  }
                //}
              }
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );
    },

    disconnectSocket() {
      if (this.stompClient !== null) {
        this.stompClient.disconnect();
      }
      this.store.commit('root/setStompClient', null);
      console.log("Disconnected");
    },

  },
  created() {
    this.connect()
    scope = this;
  },
}
</script>

<style>
.chat-wrapper {
  padding: 10px;
}

.chat-header {
  text-align: left;
  font-weight: bold;
}

.chat-main {
  height: 60vh;
}

.list {
  padding: 0;
}

.list .list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: #e8f3fe;
  margin: 10px;
  color: #7dbcfc;
}
</style>
