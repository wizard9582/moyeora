<template>
  <div class="anchor">
    <div class="anchor-item">
      <a href="#myInfo">내 정보</a>
    </div>
    <div class="anchor-item">
      <a href="#statistic">내 전적</a>
    </div>
    <div class="anchor-item">
      <a href="#recentGame">최근 게임</a>
    </div>
    <div class="anchor-item">
      <a href="#ranking">랭킹</a>
    </div>
    <div class="anchor-item">
      <a href="#friend">친구</a>
    </div>
  </div>
  <el-main>
    <div class="space"></div>
    <div class="info">
      <div class="sub-title">내 정보</div>
      <el-divider></el-divider>
      <div class="info-item" id="myInfo">
        <el-avatar id="img-profile" icon="el-icon-user-solid" shape="square" :size="100"></el-avatar>
        <!-- <el-avatar id="img-profile" shape="square" :size="100" :src="require('@/assets/img-vue.png')"></el-avatar> -->
      </div>
      <div class="sub-title">내 전적</div>
      <el-divider></el-divider>
      <div class="info-item" id="statistic">
        <el-table class="stat-list" ref="myStatTable" :data="state.myStat" stripe border>
          <el-table-column prop="job" label="직업"></el-table-column>
          <el-table-column prop="win" label="승리" width="150"></el-table-column>
          <el-table-column prop="lose" label="패배" width="150"></el-table-column>
          <el-table-column prop="rate" label="승률" width="150"></el-table-column>
        </el-table>
      </div>
      <div class="sub-title">최근 게임</div>
      <el-divider></el-divider>
      <div class="info-item" id="recentGame">
        <el-table
          class="history-list"
          ref="historyTable"
          :data="state.historyData"
          :default-sort="{prop: 'date', order: 'descending'}"
          stripe
        >
          <el-table-column prop="no" label="방번호" width="100"></el-table-column>
          <el-table-column prop="tag" label="게임" width="100">
            <template #default="scope">
              <el-tag
                :type="scope.row.tag === '자유' ? 'primary' : 'danger'"
                disable-transitions
              >{{scope.row.tag}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="방제목"></el-table-column>
          <el-table-column prop="date" label="날짜" width="150"></el-table-column>
          <el-table-column prop="result" label="결과" width="100">
            <template #default="scope">
              <el-tag
                :type="scope.row.result === 'Win' ? 'primary' : 'danger'"
                disable-transitions
              >{{scope.row.result}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="job" label="내 직업" width="100">
            <template #default="scope">
              <el-tag
                :type="scope.row.job === 'police' ? 'primary' : (scope.row.job === 'citizen' ? 'default' : 'danger')"
                disable-transitions
              >{{scope.row.job}}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="sub-title">랭킹</div>
      <el-divider></el-divider>
      <div class="info-item" id="ranking">
        <el-table class="ranking-list" ref="rankingTable" :data="state.rankingData" stripe border>
          <el-table-column type="index" label="순위"></el-table-column>
          <el-table-column prop="nickName" label="이름" width="150"></el-table-column>
          <el-table-column prop="win" label="승리" width="150"></el-table-column>
          <el-table-column prop="lose" label="패배" width="150"></el-table-column>
          <el-table-column prop label="승률" width="150"></el-table-column>
        </el-table>
        <el-table class="ranking-list" ref="myRankingTable" :data="state.myRanking" stripe border>
          <el-table-column prop="my" label="순위"></el-table-column>
          <el-table-column prop="nickName" label="이름" width="150"></el-table-column>
          <el-table-column prop="win" label="승리" width="150"></el-table-column>
          <el-table-column prop="lose" label="패배" width="150"></el-table-column>
          <el-table-column prop label="승률" width="150"></el-table-column>
        </el-table>
      </div>
      <div class="sub-title">친구</div>
      <el-divider></el-divider>
      <div class="info-item" id="friend">
        <ul class="friend-list">
          <li v-for="item in state.friendList" class="friend-list-item" :key="item">
            <el-avatar icon="el-icon-user-solid" :size="100"></el-avatar>
            <div class="sub-title">{{ item }}
              <i class="el-icon-close" @click="clickDelete(item)"></i>
            </div>
          </li>
          <li class="friend-list-item">
            <el-avatar icon="el-icon-plus" :size="100" @click="state.friendPopOpen = true"></el-avatar>
            <div class="sub-title">친구추가</div>
          </li>
        </ul>
      </div>
    </div>
    <friend-pop :open="state.friendPopOpen" @closeFriendPopup="state.friendPopOpen = false"
    ></friend-pop>
  </el-main>
</template>

<script>
import FriendPop from '@/components/home/pop/friend-pop.vue';
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'

export default {
    name:"UserSection",
    components:{
      FriendPop,
    },
    setup(props, {emit}) {
      const store = useStore()
      const state = reactive({
        friendPopOpen: false,
        userName: "",
        userId: "",
        userDepartment: "",
        friendList: [],
        myStat:[],
        historyData: [],
        rankingData: [],
      })

      const getmyStat = () => {
        store.dispatch('root/requestMyStat', { token: localStorage.getItem('jwt') })
        .then((result)=>{
          let total = {job:"종합", win:result.data.twin, lose:result.data.tlose, rate: result.data.twin/(result.data.twin+result.data.tlose).toFixed(2)}
          let citizen = {job:"시민", win:result.data.cwin, lose:result.data.close, rate:result.data.crate}
          let doctor = {job:"의사", win:result.data.dwin, lose:result.data.dlose, rate:result.data.drate}
          let police = {job:"경찰", win:result.data.pwin, lose:result.data.plose, rate:result.data.prate}
          let mafia = {job:"마피아", win:result.data.mwin, lose:result.data.mlose, rate:result.data.mrate}
          state.myStat.push(total,citizen,doctor,police,mafia)
        })
        .catch((err)=>{

        })
      }
      const getHistoryData = () => {

      }
      const getRankingData = () => {

      }
      const getFriendList = () => {
        store.dispatch('root/requestFriendList', { token: localStorage.getItem('jwt') })
        .then((result)=>{
          //console.log(result)
          state.friendList = result.data
        })
        .catch((err)=>{

        })
      }

      const clickDelete = (id) =>{
        store.dispatch('root/requestDeleteFriend', { toUser: id })
        .then((result)=>{
          getFriendList()
        })
        .catch((err)=>{

        })
      }

      getmyStat()
      getHistoryData()
      getRankingData()
      getFriendList()
      return {state, clickDelete}
    }
}
</script>

<style>
</style>
