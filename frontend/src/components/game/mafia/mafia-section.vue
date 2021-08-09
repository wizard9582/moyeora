<template>
  <div class="player-list">
    <el-row :gutter="20">
      <el-col :span="state.colSize" id="room">
        <el-row :gutter="20" id="participants">
          <!-- <el-col :span="6" v-for="idx in playerList.length" :key="idx"> -->
          <!-- <el-col :span="6"> -->
            <!-- <div id="participants"></div> -->
            <!-- <MafiaPlayer/> -->
          <!-- </el-col> -->
        </el-row>
      </el-col>
      <el-col v-show="chatVisible" :span="state.chatSize" class="chat">
        <GameChat/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MafiaPlayer from '@/components/game/mafia/mafia-player.vue'
import GameChat from '@/components/game/game-chat.vue'
import { watch, reactive, computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { register, participants } from '@/common/lib/conferenceroom'

export default {
  name: "MafiaSection",
  components: {
    MafiaPlayer,
    GameChat,
  },
  props: {
    openChat: {
      type: Boolean,
    },
  },
  // data () {
  //   return {
  //     playerList: [
  //       {playerId: '1', playerName: 'Player1', job: 'citizen', dead: false},
  //       {playerId: '2', playerName: 'Player2', job: 'citizen', dead: false},
  //       {playerId: '3', playerName: 'Player3', job: 'citizen', dead: false},
  //       {playerId: '4', playerName: 'Player4', job: 'citizen', dead: false},
  //       {playerId: '5', playerName: 'Player5', job: 'citizen', dead: false},
  //       {playerId: '6', playerName: 'Player6', job: 'citizen', dead: false},
  //       {playerId: '7', playerName: 'Player7', job: 'citizen', dead: false},
  //       {playerId: '8', playerName: 'Player8', job: 'doctor', dead: false},
  //       {playerId: '9', playerName: 'Player9', job: 'mafia', dead: false},
  //       {playerId: '10', playerName: 'Player10', job: 'police', dead: false},
  //     ]
  //   }
  // },
  setup(props, {emit}) {
    const route = useRoute()
    const store = useStore()
    const chatVisible = ref(computed(() => props.openChat))

    const state = reactive({
      colSize: 24,
      chatSize: 0,
      roomNum: route.params.no,
      userName: computed(() => store.getters['root/getUserId'])
    })

    const changeSize = () => {
      if (props.openChat) {
        state.colSize = 18
        state.chatSize = 6
      } else {
        state.colSize = 24
        state.chatSize = 0
      }
    }

    const enterRoom = () => {
      let token = localStorage.getItem('jwt')
      store.dispatch('root/requestEnterRoom', { token: token, roomId: state.roomNum })
      .then((result) => {
        console.log('result : ', result)
        store.commit('root/setRoomOwner', result.data.ownerId.userId)
      })
      .catch((error) => {
        console.log(error)
      })
    }

    register(state.roomNum, state.userName)
    enterRoom()

    // props로 넘어온 데이터가 변하면 size를 바꿔준다
    watch(chatVisible, () => {
      changeSize()
    })

    return { state, chatVisible }
  }
}
</script>

<style>
.chat {
  background-color: white;
}

video {
  border-radius: 5px;
}
/* .el-col {
  height: 100%;
} */
</style>
