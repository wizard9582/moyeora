<template>
  <el-container>
    <GameHeader
      @openInvitePopup="onOpenInvitePopup"
      @openGameClosePopup="onOpenGameClosePopup"
    />
    <el-main>
      <MafiaSection />
    </el-main>
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

export default {
  name: 'GamePage',
  components: {
    GameHeader,
    InvitePop,
    GameClosePop,
    MafiaSection
  },
  data () {
    return {
      invitePopupOpen: false,
      gameClosePopupOpen: false,
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
    }
  }
}
</script>

<style>

</style>
