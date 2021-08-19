<template>
  <el-main>
    <div class="space"></div>
      <el-divider/>
      <h3>MOYEORA<br> 프로젝트 소개 UCC</h3>
      <video playsinline autoplay muted loop poster='@/assets/0-0.png'>
        <source :src='require("@/assets/MOYEORA-ucc.mp4")' type='video/mp4'>
      </video>
      <br>
      <el-button v-if="isLoggedIn()" type="danger" @click="clickStart">바로 시작하기<i class="el-icon-right"></i></el-button>
      <el-button v-if="!isLoggedIn()" type="primary" @click="clickSignup">지금 가입하기 <i class="el-icon-edit-outline"></i></el-button>
      <div style="background-color: var(--color-default)">
      <el-divider/>
      <el-row class="sub-content">
        <el-col :span="14" class="sample-image">
          <div class="grid-content">
            <strong style="line-height:50px; font-size:45px; color:var(--color-dark);">Makes You Re-active</strong>
            <br><br><br>
            <p style="line-height:35px; font-size:20px;">
              코로나로 축 쳐진 당신을 다시 깨울 사이트<br>
              Meeting - 멀어진 우리 지금 당장 만나<br>
              Yours - 테마,기록,친구 당신만의 사이트<br>
              Reactive - 즐겁게 떠들고 웃으며 다시 활동적으로!<br>
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <span class="demonstration"><strong>MOYEORA</strong></span>
          <br>
          <img src="@/assets/0-0.png" alt="sample-image" style="height:300px; width:auto;">
        </el-col>
        <el-col :span="2" class="sample-image">
          <div class="grid-content"></div>
        </el-col>
      </el-row>
      <el-divider/>
      <el-divider/>
    </div>
      <el-row class="sub-content">
        <el-col :span="2" class="sample-image">
          <div class="grid-content"></div>
        </el-col>
        <el-col :span="8" class="sample-image">
          <span class="demonstration"><strong>즐거운 플레이</strong></span>
          <br>
          <el-carousel height="300px">
              <el-carousel-item>
                <img src="@/assets/1-1.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/1-2.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/1-3.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/1-4.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/1-5.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/1-6.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
          </el-carousel>
        </el-col>
        <el-col :span="14">
          <div class="grid-content">
            <strong style="line-height:50px; font-size:45px; color:var(--color-dark);">코로나로 잃었던 즐거움</strong>
            <br><br><br>
            <p style="line-height:35px; font-size:20px;">
              매일 치솟는 확진자, 우리 거리는 100만 광년<br>
              같이 있는 것처럼 생생한 영상과 목소리로<br>
              함께 즐기는 영상 회의와 게임들<br>
              모여라에선 사회자로 빠지는 친구도 없다구!<br>
              그리운 친구들 어색한 팀원들 모여라로 모여라!<br>
            </p>
          </div>
        </el-col>
      </el-row>
      <el-divider/>
    <div style="background-color: var(--color-default)">
      <el-divider/>
      <el-divider/>
      <el-row class="sub-content">
        <el-col :span="14" class="sample-image">
          <div class="grid-content">
            <strong style="line-height:50px; font-size:45px; color:var(--color-dark);">직관적이고 편리한 사이트</strong>
            <br><br><br>
            <p style="line-height:35px; font-size:20px;">
              설명이 필요 없는 귀여운 아이콘과 버튼들<br>
              방 설명? 친구 초대 아이디? 우리가 다 완성해줄게!<br>
              게임이 종료되면 자동으로 기록되는 전적<br>
              회의 기록으로 리마인드까지 꼼꼼히<br>
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <span class="demonstration"><strong>편리한 사이트</strong></span>
          <br>
          <el-carousel height="300px">
            <el-carousel-item>
              <img src="@/assets/3-1.png" alt="sample-image" style="height:100%; width:auto;">
            </el-carousel-item>
            <el-carousel-item>
              <img src="@/assets/3-2.png" alt="sample-image" style="height:100%; width:auto;">
            </el-carousel-item>
            <el-carousel-item>
              <img src="@/assets/3-3.png" alt="sample-image" style="height:100%; width:auto;">
            </el-carousel-item>
            <el-carousel-item>
              <img src="@/assets/3-4.png" alt="sample-image" style="height:100%; width:auto;">
            </el-carousel-item>
          </el-carousel>
        </el-col>
        <el-col :span="2" class="sample-image">
          <div class="grid-content"></div>
        </el-col>
      </el-row>
      <el-divider/>
      <el-divider/>
    </div>
      <el-divider/>
        <el-row class="sub-content">
          <el-col :span="2" class="sample-image">
            <div class="grid-content">
            </div>
          </el-col>
          <el-col :span="8">
            <span class="demonstration"><strong>다양한 테마</strong></span>
            <br>
            <el-carousel height="300px">
              <el-carousel-item>
                <img src="@/assets/2-1.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/2-2.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/2-3.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/2-4.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
              <el-carousel-item>
                <img src="@/assets/2-5.png" alt="sample-image" style="height:100%; width:auto;">
              </el-carousel-item>
            </el-carousel>
          </el-col>
          <el-col :span="14" class="sample-image">
            <div class="grid-content">
              <strong style="line-height:50px; font-size:45px; color:var(--color-dark);">취향에 맞춰 고를수 있는 다양한 테마</strong>
              <br><br><br>
              <p style="line-height:35px; font-size:20px;">
                설정 버튼 하나만 누르면 즉시 반응하는 다채로운 테마!<br>
                심플한 기본 테마, 잠자기 전 봐도 편안한 다크테마<br>
                무더위를 날려줄 시원한 여름 테마<br>
                감성이 메마른 프로그래머의 가슴을 적셔줄 핑크 테마<br>
                개성적인 사람을 위한 정신나간 레트로 테마까지<br>
              </p>
            </div>
          </el-col>
        </el-row>
        <el-divider/>
      <div style="background-color: var(--color-default)">
        <el-divider/>
        <h1>MOYEORA</h1>
        <el-divider/>
        <el-row style="line-height:35px; font-size:20px; color:var(--color-dark);">
          <el-col :span="6"></el-col>
          <el-col :span="12">
            서울 3반 6팀 '모여라 MOYEORA'
            <el-divider/>
            1대 팀장 : 최성훈 (Backend)<br>
            2대 팀장 : 박상욱 (Frontend)<br>
            팀원 : 노진혁 (Frontend)<br>
            팀원 : 이재상 (Backend)<br>
            팀원 : 이진영 (Backend)<br>
            <el-divider/>
            Special thanks to<br><br>서울 3반<br>SSAFY 5th<br>...and <strong>You</strong> <br><br><br>
          </el-col>
          <el-col :span="6"></el-col>
        </el-row >
        <el-button v-if="isLoggedIn()" type="danger" @click="clickStart">바로 시작하기<i class="el-icon-right"></i></el-button>
        <el-button v-if="!isLoggedIn()" type="primary" @click="clickSignup">지금 가입하기 <i class="el-icon-edit-outline"></i></el-button>
      </div>
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
    const url = '@/assets/MOYEORA-ucc.mp4'
    const type = 'video/mp4'

    const clickStart = function(){
      router.push("/home/all")
    }

    const isLoggedIn = function() {
      return store.getters['root/isLoggedIn']
    }

    const clickSignup = function() {
      emit('openSignupPopup')
    }

    return { clickStart, isLoggedIn, clickSignup, url, type }
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
