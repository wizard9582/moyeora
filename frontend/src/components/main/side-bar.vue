<template>
  <el-menu :default-active="state.activeIndex" class="el-menu-vertical-demo">
    <el-menu-item @click="clickFilterPop" index="1">
      <i class="el-icon-menu"/>
      <span>Game</span>
    </el-menu-item>
    <el-menu-item @click="clickRoomPop">
      <i class="el-icon-plus"/>
      <span>Room</span>
    </el-menu-item>
    <el-menu-item @click="clickNotice" index="3">
      <i class="el-icon-message-solid"/>
      <span>Notice</span>
    </el-menu-item>
    <el-menu-item @click="clickBoard" index="4">
      <i class="el-icon-tickets"/>
      <span>Board</span>
    </el-menu-item>
    <el-menu-item @click="clickSettingPop">
      <i class="el-icon-setting"/>
      <span>Setting</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import { reactive } from 'vue'

export default {
  name:'SideBar',

  setup(props, {emit}){
    const state = reactive({
      fullscreenLoading: false,
      activeIndex: 1
    })

    const clickFilterPop = () => {
      state.activeIndex = 1
      emit('openFilterPopup')
    }
    const clickRoomPop = () => {
      emit('openRoomPopup')
    }
    const clickNotice= () => {
      state.activeIndex = 3
      emit('openNotice')
    }
    const clickBoard = () => {
      state.activeIndex = 4
      emit('openBoard')
    }
    const clickSettingPop = () => {
      emit('openSettingPopup')
    }

    return { state, clickFilterPop, clickRoomPop, clickNotice, clickBoard, clickSettingPop }
  }
}
</script>

<style>
.el-menu-item{
  cursor: pointer;
  margin-left: 0px;
  background: whitesmoke;
  -webkit-transform:scale(1);
  -moz-transform:scale(1);
  -ms-transform:scale(1);
  -o-transform:scale(1);
  transform:scale(1);
  -webkit-transition:.3s;
  -moz-transition:.3s;
  -ms-transition:.3s;
  -o-transition:.3s;
  transition:.3s;
}
.el-menu-item:hover {
  -webkit-transform:scale(1.2);
  -moz-transform:scale(1.2);
  -ms-transform:scale(1.2);
  -o-transform:scale(1.2);
  transform:scale(1.2);
  z-index: 5000;
}
</style>
