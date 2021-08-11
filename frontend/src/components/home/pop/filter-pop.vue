<template>
  <el-dialog class="gameFilter" title="게임선택" v-model="state.popupVisible" @close="handleClose">
    <ul class="gameFilter-list">
      <li v-for="i in 9" class="gameFilter-list-item" @click="clickGame(i)" :key="i">
        <img :src="require(`@/assets/${state.imgsrc[i]}`)" class="img-game" alt="yet" style="width: 100%; height: 200px"/>
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
      imgsrc: ["img-vue.png","img-logo-chat.png","img-logo-mafia.png","img-logo-puzzle.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png",],
      type: ["zero","all","mafia","puzzle","1","2","3","4","5","6",],
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
.gameFilter-list-item {
  min-width: 200px;
  max-width: 20%;
  min-height: 200px;
  max-height: 20%;
  display: inline-block;
  cursor: pointer;
  margin: 10px;
}
.img-game{
  border: 1px solid black;
  padding: 0px;

}
</style>
