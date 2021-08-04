<template>
  <div class="chat-wrapper">
    <div class="chat-header">
        채팅창
    </div>
    <div class="chat-main" style="overflow:auto">
      <ul
        class="list"
        v-infinite-scroll="load"
        infinite-scroll-disabled="disabled">
        <li v-for="i in recvList" class="list-item" :key="i">{{ i.fromName }} >> {{ i.toName }} : <br> {{ i.message }}</li>
      </ul>
    </div>
    <el-input placeholder="모두에게" v-model="toName"></el-input>
    <el-input type="textarea" :rows="2" placeholder="Press Enter for send message." v-model="message" @keyup="sendMessage"></el-input>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

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

    return { count, disabled, load, roomId };
  },
  data() {
    return {
      userName: "",
      //roomId : roomNum,
      message: "",
      recvList: [],
      toName: "",
    }
  },
  methods: {
    sendMessage (e) {
      if(e.keyCode === 13 && this.userName !== '' && this.message !== ''){
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

    connect() {
      const store = useStore();
      store.dispatch('root/requestUserInfo')
        .then(function (result) {
            scope.userName = result.data.userId;
        })
        .catch(function (err) {
          alert(err)
        })

      const serverURL = "https://localhost:8443/websocket"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);

          this.stompClient.subscribe("/sub/chat/room/"+this.roomId, chat => {
            let mess = JSON.parse(chat.body)
            mess.toName = '모두'
            scope.recvList.push(mess)
          });
          this.stompClient.subscribe('/sub/chat/room/'+this.roomId+'/'+this.userName, function (chat) {
            scope.recvList.push(JSON.parse(chat.body));
          });
          // for문을 사용하여 참가자 아이디 별로 구독할지, 전체 구독으로 사용자에 따른 
          this.stompClient.subscribe('/sub/vote/room/'+this.roomId+'/'+this.userName, function (chat) {
            console.log("투표 받았다")
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
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
