<template>
  <el-container>
    <el-aside width="120">
      <side-bar
        @openFilterPopup="onOpenFilterPopup"
        @openRoomPopup="onOpenRoomPopup"
        @openSettingPopup="onOpenSettingPopup"
        @openBoard="onOpenBoard"
        @openNotice="onOpenNotice"
      ></side-bar>
    </el-aside>
    <el-container>
      <main-header></main-header>
      <router-view :key="$route.fullPath"
      @openPwCheck="onOpenPwPopup"
      />
      <el-footer>Copyright © SAMSUNG All Rights Reserved.</el-footer>
    </el-container>
  </el-container>
  <filter-pop :open="state.filterPopupOpen" @closeFilterPopup="onCloseFilterPopup" />
  <create-room-pop :open="state.roomPopupOpen" @closeRoomPopup="onCloseRoomPopup" />
  <room-pw-pop :open="state.pwPopupOpen" @closePwPopup="onClosePwPopup" :type="state.data[0]" :id="state.data[1]"/>
  <setting-pop :open="state.settingPopupOpen" @closeSettingPopup="onCloseSettingPopup" />
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
import { reactive } from 'vue';
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
      loginPopupOpen: false,
      data: []
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
    const onOpenPwPopup = function(data){
      //console.log("data-------->",data)
      state.data = data
      state.pwPopupOpen = true
    }
    const onClosePwPopup = function(){
      state.data = []
      state.pwPopupOpen = false
    }
    const onOpenNotice = function(){
      router.push("/home/notice/list")
    }
    const onOpenBoard = function(){
      router.push("/home/board/list")
    }
    return { state, onOpenSettingPopup, onCloseSettingPopup, onOpenFilterPopup, onCloseFilterPopup, onOpenRoomPopup, onCloseRoomPopup,
            onOpenPwPopup, onClosePwPopup, onOpenNotice, onOpenBoard}
  }
};
</script>

<style>
</style>
