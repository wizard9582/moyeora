<template>
  <div class="player-list">
    <el-row :gutter="20">
      <el-col :span="6" v-for="idx in playerList.length" :key="idx">
        <MafiaPlayer/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MafiaPlayer from '@/components/game/mafia/mafia-player.vue'

export default {
  name: "MafiaSection",
  components: {
    MafiaPlayer,
  },
  data () {
    return {
      playerList: [
        {playerId: '1', playerName: 'Player1', job: 'citizen', dead: false},
        {playerId: '2', playerName: 'Player2', job: 'citizen', dead: false},
        {playerId: '3', playerName: 'Player3', job: 'citizen', dead: false},
        {playerId: '4', playerName: 'Player4', job: 'citizen', dead: false},
        {playerId: '5', playerName: 'Player5', job: 'citizen', dead: false},
        {playerId: '6', playerName: 'Player6', job: 'citizen', dead: false},
        {playerId: '7', playerName: 'Player7', job: 'citizen', dead: false},
        {playerId: '8', playerName: 'Player8', job: 'doctor', dead: false},
        {playerId: '9', playerName: 'Player9', job: 'mafia', dead: false},
        {playerId: '10', playerName: 'Player10', job: 'police', dead: false},
      ]
    }
  }
}
</script>

<style>

</style>
