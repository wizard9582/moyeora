<template>
  <el-main>
    <div class="space"></div>
    <h1>MOYEORA</h1>
    <iframe width="560" height="315" src="https://www.youtube.com/embed/-FMHYjqC4iA" title="YouTube video player"
    frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
    <div class="space">위에 프로젝트 ucc 넣을거에요</div>
    <el-button v-if="isLoggedIn()" type="danger" @click="clickStart">바로 시작하기<i class="el-icon-caret-right el-icon-right"></i></el-button>
    <el-row class="sub-content">
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
    </el-row>
    <el-row class="sub-content">
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
    </el-row>
    <el-row class="sub-content">
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
    </el-row>
  </el-main>
</template>

<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

export default {
    name:'WelcomeSection',
    setup(){
      const router = useRouter()
      const store = useStore()

      const clickStart = function(){
        router.push("/home/all")
      }

      const isLoggedIn = function () {
        return store.getters['root/isLoggedIn']
      }

      return { clickStart, isLoggedIn }
    }
}
</script>

<style>
.space {
  height: 100px;
  width: 100%;
}
.sub-content {
    height: 300px;
}
.sub-content .sample-image .img{
    min-width: 100px;
    width: 30%;
    height: auto;
}
.grid-content {
    background: #e5e9f2;
    border-radius: 4px;
    height: 300px;
}
</style>
