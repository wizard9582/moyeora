<template>
  <el-menu class="el-menu-vertical-demo">
    <el-menu-item @click="clickFilterPop">
      <i class="el-icon-menu"/>
      <span>Game</span>
    </el-menu-item>
    <el-menu-item @click="clickRoomPop">
      <i class="el-icon-plus"/>
      <span>Room</span>
    </el-menu-item>
    <el-menu-item @click="clickSettingPop">
      <i class="el-icon-setting"/>
      <span>Setting</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name:'SideBar',

  setup(props, {emit}){
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      fullscreenLoading: false,
    })

    const clickFilterPop = () => {
      emit('openFilterPopup')
    }
    const clickRoomPop = () => {
      emit('openRoomPopup')
    }
    const clickSettingPop = () => {
      emit('openSettingPopup')
    }

    return { state, clickFilterPop, clickRoomPop, clickSettingPop }
  }
}
</script>

<style>
.el-menu-item{
  cursor: pointer;
  margin-left: 0px;
  background: whitesmoke;
}
</style>
