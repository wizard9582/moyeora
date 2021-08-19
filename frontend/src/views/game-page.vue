<template>
  <el-container>
    <GameHeader
      @openInvitePopup="onOpenInvitePopup"
      @startNight="onStartNight"
      @startDay="onStartDay"
      v-bind:class="{ 'day': day, 'night':!day }"
    />
    <div class="space"></div>
    <el-main>
      <MafiaSection
        :openChat="gameChatOpen"
        v-bind:class="{ 'day': day, 'night':!day }"
        @closeGameChat="onCloseGameChat"
        @openFinalVotePopup="onOpenFinalVotePopup"
        @openDoctorVotePopup="onOpenDoctorVotePopup"
        @openMafiaVotePopup="onOpenMafiaVotePopup"
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
  <FinalVotePop
    :open="finalVotePopupOpen"
    @closeFinalVotePopup="onCloseFinalVotePopup"
  />
  <DoctorVotePop
    :open="doctorVotePopupOpen"
    @closeDoctorVotePopup="onCloseDoctorVotePopup"
  />
  <MafiaVotePop
    :open="mafiaVotePopupOpen"
    @closeMafiaVotePopup="onCloseMafiaVotePopup"
  />
</template>

<script>
import GameHeader from '@/components/game/game-header.vue'
import MafiaSection from '@/components/game/mafia/mafia-section.vue'
import GameController from '../components/game/game-controller.vue'
import InvitePop from '@/components/game/pop/invite-pop.vue'
import GameClosePop from '@/components/game/pop/gameClose-pop.vue'
import FinalVotePop from '@/components/game/pop/finalVote-pop.vue'
import DoctorVotePop from '@/components/game/pop/doctorVote-pop.vue'
import MafiaVotePop from '@/components/game/pop/mafiaVote-pop.vue'


export default {
  name: 'GamePage',
  components: {
    GameHeader,
    MafiaSection,
    GameController,
    InvitePop,
    GameClosePop,
    FinalVotePop,
    DoctorVotePop,
    MafiaVotePop,
  },
  data () {
    return {
      invitePopupOpen: false,
      gameClosePopupOpen: false,
      finalVotePopupOpen: false,
      doctorVotePopupOpen: false,
      mafiaVotePopupOpen: false,
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
    onOpenFinalVotePopup () {
      this.finalVotePopupOpen = true
    },
    onCloseFinalVotePopup () {
      this.finalVotePopupOpen = false
    },
    onOpenMafiaVotePopup () {
      this.mafiaVotePopupOpen = true
    },
    onCloseMafiaVotePopup () {
      this.mafiaVotePopupOpen = false
    },
    onOpenDoctorVotePopup () {
      this.doctorVotePopupOpen = true
    },
    onCloseDoctorVotePopup () {
      this.doctorVotePopupOpen = false
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
.el-main {
  z-index: 1;
  background-color: rgba(255,255,255,0);
}
.el-footer {
  text-align: center;
  position: fixed;
  left: 0;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 0px;
  min-height: 0px;
  z-index: 50;
}
</style>
