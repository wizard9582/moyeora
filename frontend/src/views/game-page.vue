<template>
  <el-container>
    <GameHeader
      @openInvitePopup="onOpenInvitePopup"
      @openGameClosePopup="onOpenGameClosePopup"
      @startNight="onStartNight"
      @startDay="onStartDay"
      v-bind:class="{ 'day': day, 'night':!day }"
    />
    <el-main>
      <MafiaSection
        :openChat="gameChatOpen"
        @closeGameChat="onCloseGameChat"
        v-bind:class="{ 'day': day, 'night':!day }"
      />
    </el-main>
    <el-footer height="80px">
      <GameController
        @openGameChat="onOpenGameChat"
        @closeGameChat="onCloseGameChat"
        @openGameClosePopup="onOpenGameClosePopup"
      />
    </el-footer>
  </el-container>
  <InvitePop
    :open="invitePopupOpen"
    @closeInvitePopup="onCloseInvitePopup"
  />
  <GameClosePop
    :open="gameClosePopupOpen"
    @closeGameClosePopup="onCloseGameClosePopup"
  />
</template>

<script>
import GameHeader from '@/components/game/game-header.vue'
import InvitePop from '@/components/game/pop/invite-pop.vue'
import GameClosePop from '@/components/game/pop/gameClose-pop.vue'
import MafiaSection from '@/components/game/mafia/mafia-section.vue'
import GameController from '../components/game/game-controller.vue'


export default {
  name: 'GamePage',
  components: {
    GameHeader,
    InvitePop,
    GameClosePop,
    MafiaSection,
    GameController
  },
  data () {
    return {
      invitePopupOpen: false,
      gameClosePopupOpen: false,
      gameChatOpen: false,
      day: true,
    }
  },
  methods: {
    onOpenInvitePopup () {
      this.invitePopupOpen = true
    },
    onCloseInvitePopup () {
      this.invitePopupOpen = false
    },
    onOpenGameClosePopup () {
      this.gameClosePopupOpen = true
    },
    onCloseGameClosePopup () {
      this.gameClosePopupOpen = false
    },
    onOpenGameChat () {
      this.gameChatOpen = true
    },
    onCloseGameChat () {
      this.gameChatOpen = false
    },
    onStartNight(){
      this.day = false
    },
    onStartDay(){
      this.day = true
    },
  }
}
</script>

<style scoped>
.el-header{
  position: fixed;
  top: 0;
	left: 0;
  width: 100%;
  height: 70px;
  /* border: solid; */
}
.el-main {
  color: #333;
  text-align: center;
  width: 100%;
  height: 100%;
}
.day{
  background: white;
}
.night{
  background: black;
}
.el-footer {
  background-color: gray;
  color: #333;
  text-align: center;
  position: fixed;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
