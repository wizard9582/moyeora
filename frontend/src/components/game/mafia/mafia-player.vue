<template>
  <div class="player-screen" v-if="!state.playerDead">
    <video src="*" autoplay></video>
  </div>
  <div class="player-screen" v-else>
    <i class="el-icon-close"></i>
    <!-- <video src="*" autoplay></video> -->
  </div>
</template>

<script>
import { reactive, computed } from 'vue'

export default {
  name: 'MafiaPlayer',
  props:{
    playerData:{
      type: Object,
      default: {playerId: '1', playerName: 'Player1', job: 'citizen', dead: false},
    }
  },
  setup(props, {emit}) {
    const state = reactive({
      playerDead: computed(() => props.playerData.dead),
      playerJob: computed(() => props.playerData.job),
    })

    return { state }
  }
}
</script>

<style>
video {
  width: auto;
  height: auto;
}

.player-screen {
 background-color: gainsboro;
 border-radius: 4px;
}
</style>
