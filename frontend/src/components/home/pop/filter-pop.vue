<template>
  <el-dialog class="gameFilter" title="게임선택" v-model="state.popupVisible" @close="handleClose">
    <ul class="gameFilter-list">
      <li v-for="i in 9" class="gameFilter-list-item" @click="clickGame(i)" :key="i">
        {{state.desc[i]}}
        <img :src="require(`@/assets/${state.imgsrc[i]}`)" class="img-game" alt="yet"/>
        <el-divider/>
      </li>
    </ul>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'

export default {
  name:"FilterPop",
  props: {
    open: {
    }
  },

  setup(props, { emit }) {
    const state = reactive({
      formLabelWidth: '120px',
      popupVisible: computed(() => props.open),
      imgsrc: ["img-vue.png","img-all.png","img-logo-chat.png","img-logo-mafia.png","img-logo-puzzle.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png",],
      type: ["zero","all","video","mafia","puzzle","1","2","3","4","5",],
      desc: ["index맞추기","ALL","화상회의","마피아","퍼즐","개발중","개발중","개발중","개발중","개발중",],
    })

    const handleClose = function () {
      emit('closeFilterPopup','no')
    }
    const clickGame = function (val) {
      //let type = state.type[val]
      emit('closeFilterPopup', state.type[val])
    }
    return {state, handleClose, clickGame}
  }
}
</script>

<style>
</style>
