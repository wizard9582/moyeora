<template>
  <el-container>
    <main-header></main-header>
    <el-container>
      <!-- <side-bar width="200px"></side-bar> -->
      <el-aside>
        <side-bar></side-bar>
      </el-aside>
      <el-container>
        <router-view></router-view>
        <main-footer></main-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import MainHeader from "../components/main/header.vue";
import MainFooter from "../components/main/footer.vue";
import CreateRoomPop from '../components/home/pop/createRoom-pop.vue';
import FilterPop from '../components/home/pop/filter-pop.vue';
import RoomPwPop from '../components/home/pop/roomPW-pop.vue';
import SideBar from '../components/main/side-bar.vue';

export default {
    name: 'HomePage',
    components: {
      MainHeader,
      MainFooter,
      CreateRoomPop,
      FilterPop,
      RoomPwPop,
      SideBar,
    },
};

</script>

<style>
.el-header{
  line-height: 100px;
}
.el-footer {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 500px;
}

.el-aside {
  width: 100px;
    line-height: 1000px;
}

.el-main {

}
/* 차후 main.css로 통합? */
</style>