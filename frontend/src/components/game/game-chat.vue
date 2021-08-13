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
import { participants, leaveRoom, register } from '@/common/lib/conferenceroom'
// import { stompClient, socket } from '@/common/lib/webSocket'

let scope = '';

export default {
  name: 'GameChat',

  setup() {
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
    })

    // const participants = ref(participants)
    // console.log('participants[ssafy1] : ', participants['ssafy1'])

    return { count, disabled, load, roomId, participants, router, store, state };
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
            scope.store.commit('root/voteTo', voteResult['toName'])
            // 받은 투표 수를 화면에 표시
            // console.log(document.querySelector(`#${voteResult['toName']}`))
            // console.log(document.querySelector(`#${voteResult['toName']}`).children[2])
            const voteSpan = document.querySelector(`#${voteResult['toName']}`).children[2]
            voteSpan.innerText = scope.state.voteCountList[voteResult['toName']][0]

            // 투표 완료 여부 체크하기
            let total = 0
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
              total += totalResult[id][0]
            }

            // 투표가 완료되었다면 처리
            if (total === scope.state.participantsList.length) {
              let msg = {}
              if (tiebreaker) { // 동점자가 있는 경우
                msg = { name: '' };
              } else { // 동점자가 없는 경우
                msg = { name: maxUser[2] };
              }
              console.log(tiebreaker, msg)
              scope.stompClient.send("/pub/game/end/"+ scope.roomId, JSON.stringify(msg), {});
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
              const msg = {
                round: 0,
                desc: "morning",
                second : 20,
              };
              scope.stompClient.send("/pub/game/judge/"+ scope.roomId, JSON.stringify(msg), {});
            }
          });

          this.stompClient.subscribe('/sub/game/judge/'+this.roomId, function (chat) {
            console.log("타이머 2 (변론, 투표) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
             if((scope.userName == scope.state.ownerId) && rchat.desc=='end'){
            const msg = {
              round: 0,
              desc: "morning",
              second : 20,
            };
            scope.stompClient.send("/pub/game/night/"+ scope.roomId, JSON.stringify(msg), {});}
          });

          this.stompClient.subscribe('/sub/game/night/'+this.roomId, function (chat) {
            console.log("타이머 3 (저녁, 결과) : ", JSON.parse(chat.body))
            let rchat = JSON.parse(chat.body);
            leaveRoom()
            if(true){
              // 살아있으면
              console.log("내 직업",myRole)
              if(myRole=='mafia'){
                  // 마피아인 경우 : 마피아끼리 모임
                  scope.waitSecond(scope.roomId+"/mafia",scope.userName)
                }else if(myRole == 'doctor'){
                  // 의사인 경우 : 투표창 열림

                }
                else if(myRole == 'police'){
                  // 경찰아인 경우 : 투표창 열림

                }
            }
            // 죽었으면 leaveRoom에서 끝남

              if(rchat.desc=='end'){
                if(myRole=='mafia'){
                  // 마피아인 경우 : 마피아끼리 모임
                  leaveRoom()
                }
                scope.waitSecond(scope.roomId,scope.userName)
                if((scope.userName == scope.state.ownerId)){
                   const msg = {
                    round: 0,
                    desc: "morning",
                    second : 20,
                    //scope.stompClient.send("/pub/game/morning/"+ scope.roomId, JSON.stringify(msg), {});
                    }
                }
              }
          });

          //게임 종료 판단하기
          this.stompClient.subscribe('/sub/game/end/'+this.roomId, function (res) {
              console.log("게임 진행 상황 : "+res.body);

              if(res.body=='on'){
                //게임 계속 진행


              }else if(res.body=='citizen'){
                //시민 이김



              }else{
                //마피아 이김



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
