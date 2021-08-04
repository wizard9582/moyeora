<template>
  <el-container>
    <el-aside>
      <side-bar
        @openFilterPopup="onOpenFilterPopup"
        @openRoomPopup="onOpenRoomPopup"
        @openSettingPopup="onOpenSettingPopup"
      ></side-bar>
    </el-aside>
    <el-container>
        <main-header></main-header>
        <router-view :key="$route.fullPath"/>
        <el-footer>Copyright © SAMSUNG All Rights Reserved.</el-footer>
    </el-container>
  </el-container>
  <filter-pop
    :open="state.filterPopupOpen"
    @closeFilterPopup="onCloseFilterPopup"
  />
  <create-room-pop
    :open="state.roomPopupOpen"
    @closeRoomPopup="onCloseRoomPopup"
  />
  <room-pw-pop
    :open="state.pwPopupOpen"
    @closePwPopup="onClosePwPopup"
  />
  <setting-pop
    :open="state.settingPopupOpen"
    @closeSettingPopup="onCloseSettingPopup"
  />
</template>

<script>
import MainHeader from "../components/main/header.vue";
import SideBar from '../components/main/side-bar.vue';
import SettingPop from '../components/home/pop/setting-pop.vue';
import CreateRoomPop from '../components/home/pop/createRoom-pop.vue';
import FilterPop from '../components/home/pop/filter-pop.vue';
import RoomPwPop from '../components/home/pop/roomPW-pop.vue';
import HomeSection from '../components/home/home-section.vue';
import NoticeSection from '../components/home/notice-section.vue';
import BoardSection from '../components/home/board-section.vue';
import UserSection from '../components/home/user-section.vue';
import { reactive} from 'vue';
import { useRouter } from 'vue-router';

export default {
    name: 'HomePage',
    components: {
      MainHeader,
      SideBar,
      SettingPop,
      CreateRoomPop,
      FilterPop,
      RoomPwPop,
      HomeSection,
      NoticeSection,
      BoardSection,
      UserSection
    },
  setup () {
    const router = useRouter()
    const state = reactive({
      settingPopupOpen: false,
      pwPopupOpen: false,
      filterPopupOpen: false,
      roomPopupOpen: false,
    })
    const onOpenSettingPopup = function(){
      state.settingPopupOpen = true
    }
    const onCloseSettingPopup = function(){
      state.settingPopupOpen = false
    }
    const onOpenFilterPopup = function(){
      state.filterPopupOpen = true
    }
    const onCloseFilterPopup = function(choosed){
      state.filterPopupOpen = false
      if(choosed!='no'){
        router.push("/home/" + choosed)
      }
    }
    const onOpenRoomPopup = function(){
      state.roomPopupOpen = true
    }
    const onCloseRoomPopup = function(){
      state.roomPopupOpen = false
    }
    const onOpenPwPopup = function(){
      state.pwPopupOpen = true
    }
    const onClosePwPopup = function(){
      state.pwPopupOpen = false
    }
    return {state, onOpenSettingPopup, onCloseSettingPopup, onOpenFilterPopup, onCloseFilterPopup,
            onOpenRoomPopup, onCloseRoomPopup, onOpenPwPopup, onClosePwPopup}
  }
};
</script>

<style>
@import "https://unpkg.com/element-plus/lib/theme-chalk/index.css";
.space{
  height: 100px;
  width: 100%;
}
.el-header{
  position: fixed;
  top: 0;
	left: 0;
  width: 100%;
  height: 70px;
  /* border: solid; */
  background: white;
  opacity: 0.9;
}
.el-aside {
  position: fixed;
  top: 70px;
	left: 0;
  width: 120px;
  height: 100%;
  /* border: solid; */
  background: whitesmoke;
}
.el-main {
  width: 100% - 100px;
  margin-left: 100px;
  background-color: #e8eef3;
  color: #333;
  text-align: center;
}

body > .el-container {
  margin-bottom: 40px;
}
.el-footer {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  width: 100%;
  height: 200px;
  line-height: 150px;
}
/* 차후 main.css로 통합? */
</style>
