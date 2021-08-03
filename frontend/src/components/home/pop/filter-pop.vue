<template>
  <el-dialog class="gameFilter" title="게임선택" v-model="state.popupVisible" @close="handleClose">
    <ul class="gameFilter-list">
      <li v-for="i in 9" class="gameFilter-list-item" @click="clickGame(i)" :key="i">
        <img :src="require(`@/assets/${imgsrc[i]}`)" alt="yet" style="width: 100%; height: 200px"/>
      </li>
    </ul>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'

export default {
  name:"FilterPop",
  props: {
    open: {
      type: Boolean,
      default: false,
    }
  },

  data(){
    return{
      imgsrc:["img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png","img-vue.png",],
      type:["all","mafia","puzzle","1","2","3","4","5","6","7",],
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const state = reactive({
      formLabelWidth: '120px',
      popupVisible: computed(() => props.open),
    })

    const handleClose = function () {
      emit('closeFilterPopup')
    }

    const clickGame = function(val){
      //url 이동하는 부분 안됨
      this.$router.push({path: "/home/" + this.type[val]})
      this.handleClose()
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
</style>
