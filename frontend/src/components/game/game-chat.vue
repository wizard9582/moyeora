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
    <el-select v-model="toName" clearable placeholder="Select">
      <el-option
        v-for="player in state.participantsList"
        :key="player.userId"
        :label="player.userId"
        :value="player.userId"
      ></el-option>
    </el-select>
    <el-button @click="openDoctorVote">확인</el-button>
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
import { ref, computed, reactive } from 'vue'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { leaveRoom, register } from '@/common/lib/conferenceroom'


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
      ownerId : computed(()=> store.getters['root/getRoomOwner']),
      voteCountList: computed(() => store.getters['root/getVoteCount']),
      finalVoteCount: computed(() => store.getters['root/getFinalVoteCount']),
      didMafiaKillPlayer: false,
    })

    const openFinalVote = () => {
      emit('openFinalVotePop')
    }

    const openDoctorVote = () => {
      emit('openDoctorVotePop')
    }

    const openMafiaVote = () => {
      emit('openMafiaVotePop')
    }

    return { count, disabled, load, roomId, router, store, state, openFinalVote, openDoctorVote, openMafiaVote };
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
        if(this.toName == '')
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
        }, 3000-random); // 랜덤 숫자 더하기 (-0.5~0.5)
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

              // voteCount 세팅하기
              // voteCount 구조 = [{ userId : [0, pk] }]
              let votePlayerList = new Object
              for (let player of result.data.member) {
                let greetingUserId = player.userId
                votePlayerList[greetingUserId] = [0, player.id]
              }
              console.log('votePlayerList', votePlayerList)
              scope.store.commit('root/setVoteCount', votePlayerList)
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
              let currentMafiaSelectPlayer = computed(() => scope.store.getters['root/getMafiaSelectPlayer'])
              if (!currentMafiaSelectPlayer) {
                scope.store.commit('root/setMafiaSelectPlayer', voteResult.toName)
              } else {
                if (currentMafiaSelectPlayer !== voteResult.toName) {
                  // 마피아 간의 의견 충돌이 나면 mafia로 처리하며 투표를 무효화한다.
                  scope.store.commit('root/setMafiaSelectPlayer', 'mafia')
                }
              }
            } else {
              scope.store.commit('root/voteTo', voteResult['toName'])
              // 받은 투표 수를 화면에 표시
              const voteSpan = document.querySelector(`#${voteResult['toName']}`).children[2]
              voteSpan.innerText = scope.state.voteCountList[voteResult['toName']][0]
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
            // 직업 연동 아직 안됨
            console.log("타이머 0 (직업 분배 결과) : ", chat.body)
            alert("당신의 직업은 "+ chat.body +"입니다.")
            myRole = chat.body;
          });
          // 메세지를 받을 때마다 게임 승리여부 판단과 라운드 체크를 해주어야 합니다.
          this.stompClient.subscribe('/sub/game/morning/'+this.roomId, function (chat) {
            console.log("타이머 1 (아침, 투표) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            scope.store.commit('root/setGameRound', {round: rchat.round, second: rchat.second})
            if((scope.userName == scope.state.ownerId) && rchat.desc=='end'){
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
                msg = { round: 1 }
                // 방장만 메시지를 보낸다!
                if (scope.userName === scope.state.ownerId) {
                  scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {});
                }
              } else { // 동점자가 없는 경우
                msg = { round: 1 };
                scope.store.commit('root/setFinalVotePlayer', maxUser[1])
                scope.stompClient.send("/pub/game/judge/"+ scope.roomId, JSON.stringify(msg), {});
              }
              console.log(tiebreaker, msg)
            }
          });

          this.stompClient.subscribe('/sub/game/judge/'+this.roomId, function (chat) {
            console.log("타이머 2 (변론, 투표) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            // 최종 투표 팝업 오픈
            if(rchat.desc === 'finalvote') {
              scope.store.commit('root/setFinalVoteCount')
              scope.openFinalVote()
            }
            if((scope.userName == scope.state.ownerId) && rchat.desc === 'end'){
              let msg = {}
              let playerPK = ''
              if (voteCount[0] > voteCount[1]) {
                let finalVotePlayer = computed(() => scope.store.getters['root/getFinalVotePlayer'])
                for (let player of scope.state.participantsList) {
                  if (player.userId === finalVotePlayer) {
                    playerPK = player.id
                    break
                  }
                }
              }
              msg = { name: `judge,${playerPK}` }
              scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {});
            }
          });

          this.stompClient.subscribe('/sub/game/night/'+this.roomId, function (chat) {
            console.log("타이머 3 (저녁, 결과) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            if (rchat.desc === 'night') {
              leaveRoom()
              if(true){
                // 살아있으면
                console.log("내 직업",myRole)
                if(myRole=='mafia'){
                  // 마피아인 경우 : 마피아끼리 모임
                  scope.waitSecond(scope.roomId+"/mafia",scope.userName)
                  scope.openMafiaVote()
                }else if(myRole == 'doctor'){
                  // 의사인 경우 : 투표창 열림
                  scope.openDoctorVote()
                }
                else if(myRole == 'police'){
                  // 경찰인 경우 : 투표창 열림

                }
              }
            } else if (rchat.desc === 'result') {
              let doctorSelectPlayer = computed(() => scope.store.getters['root/getDoctorSelectPlayer'])
              let mafiaSelectPlayer = computed(() => scope.store.getters['root/getMafiaSelectPlayer'])
              if (doctorSelectPlayer === mafiaSelectPlayer || !mafiaSelectPlayer || mafiaSelectPlayer === 'mafia') {
                // 마피아가 제거하지 못하는 경우
                // let testMessage = new Object
                scope.recvList.push({message:'아무도 안죽었다'})
                scope.state.didMafiaKillPlayer = false
              } else {
                // 마피아가 제거하는 경우
                scope.recvList.push({message:`${mafiaSelectPlayer}가 죽었다.`})
                scope.state.didMafiaKillPlayer = true
              }

            } else if(rchat.desc=='end'){
              // 죽었으면 leaveRoom에서 끝남
              if(myRole=='mafia'){
                // 마피아인 경우 : 마피아끼리 모임
                leaveRoom()
              }
              scope.waitSecond(scope.roomId,scope.userName)
              if((scope.userName == scope.state.ownerId)){
                if (scope.state.didMafiaKillPlayer) {
                  let playerPK = ''
                  let deadPlayer = computed(() => scope.store.getters['root/getMafiaSelectPlayer'])
                  for (let player of scope.state.participantsList) {
                    if (player.userId === deadPlayer) {
                      playerPK = player.id
                      break
                    }
                  }
                  const msg = {
                    name: `night,${playerPK}`,
                  }
                  scope.stompClient.send("/pub/game/end/"+ scope.roomId, JSON.stringify(msg), {});
                }
              }
            }
          });

          //게임 종료 판단하기
          this.stompClient.subscribe('/sub/game/end/'+this.roomId, function (res) {
              console.log("게임 진행 상황 : "+res.body);
              const resMessage = res.split(',')
              if(resMessage[1]=='on'){
                //게임 계속 진행
                if (resMessage[0] === 'judge') {
                  const msg = { roomId: scope.roomId }
                  scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {})
                } else if (resMessage[0] === 'night') {
                  const msg = { roomId: scope.roomId }
                  scope.stompClient.send("/pub/game/morning/"+ scope.roomId, JSON.stringify(msg), {})
                }
              }else if(resMessage[1]=='citizen'){
                //시민 이김
                alert('시민 승!!!')
              }else{
                //마피아 이김
                alert('마피아 승!!!')
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
