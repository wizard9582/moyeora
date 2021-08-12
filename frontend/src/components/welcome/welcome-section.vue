<template>
  <el-main>
    <div class="space"></div>
    <el-carousel trigger="click" height="500px" :interval="4000">
      <el-carousel-item class="project-desc">
        <h3>MOYEORA<br> 프로젝트 소개 UCC</h3>
          <iframe width="560" height="315" src="https://www.youtube.com/embed/-FMHYjqC4iA" title="YouTube video player"
          frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
          <br>
          <el-button v-if="isLoggedIn()" type="danger" @click="clickStart">바로 시작하기<i class="el-icon-right"></i></el-button>
          <el-button v-if="!isLoggedIn()" type="primary" @click="clickSignup">지금 가입하기 <i class="el-icon-edit-outline"></i></el-button>
      </el-carousel-item>
      <el-carousel-item class="project-desc">
        <h1>MOYEORA</h1>
        <el-row class="sub-content">
          <el-col :span="8" class="sample-image">
            <img src="@/assets/img-vue.png" alt="sample-image">
          </el-col>
          <el-col :span="16">
          </el-col>
        </el-row>
      </el-carousel-item>
      <el-carousel-item class="project-desc">
        <h1>MOYEORA</h1>
        <el-row class="sub-content">
          <el-col :span="16" class="sample-image">
          </el-col>
          <el-col :span="8">
            <img src="@/assets/img-vue.png" alt="sample-image">
          </el-col>
        </el-row>
      </el-carousel-item>
      <el-carousel-item class="project-desc">
        <h1>MOYEORA</h1>
        <el-row class="sub-content">
          <el-col :span="8" class="sample-image">
            <img src="@/assets/img-vue.png" alt="sample-image">
          </el-col>
          <el-col :span="16">
          </el-col>
        </el-row>
      </el-carousel-item>
      <el-carousel-item class="project-desc">
        <h1>MOYEORA</h1>
        <el-row class="sub-content">
          <el-col :span="16" class="sample-image">
            <div class="grid-content"></div>
          </el-col>
          <el-col :span="8">
            <img src="@/assets/img-vue.png" alt="sample-image">
          </el-col>
        </el-row>
      </el-carousel-item>
      <el-carousel-item class="project-desc">
        <h1>MOYEORA</h1>
        <el-row>
          <el-col :span="12">
            프로젝트 설명
          </el-col>
          <el-col :span="6">
            서울 3반 6팀 '모여라'
            <el-divider/>
            팀장 : 최성훈 (Backend)<br>
            팀원 : 노진혁 (Frontend)<br>
            팀원 : 박상욱 (Frontend)<br>
            팀원 : 이재상 (Backend)<br>
            팀원 : 이진영 (Backend)<br>
            <el-divider/>
          </el-col>
          <el-col :span="6"></el-col>
        </el-row >
      </el-carousel-item>
    </el-carousel>
  </el-main>
</template>

<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

export default {
  name:'WelcomeSection',
  setup(props, {emit}){
    const router = useRouter()
    const store = useStore()

    const clickStart = function(){
      router.push("/home/all")
    }

    const isLoggedIn = function() {
      return store.getters['root/isLoggedIn']
    }

    const clickSignup = function() {
      emit('openSignupPopup')
    }

    return { clickStart, isLoggedIn, clickSignup }
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
  border-radius: 4px;
  height: 300px;
}
.project-desc{
  text-align: center;
  background: whitesmoke;
}
</style>
