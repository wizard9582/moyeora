<template>
  <div class="player-list">
    <el-row :gutter="20">
      <el-col :span="state.colSize" id="room">
        <el-row :gutter="20" id="participants">
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
