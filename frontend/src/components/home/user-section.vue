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
        {{state.userId}}
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
      <div class="sub-title">최근 회의</div>
      <el-divider></el-divider>
      <div class="info-item" id="recentGame">
        <el-table
          class="history-list"
          ref="historyTable"
          :data="state.historyData"
          :default-sort="{prop: 'date', order: 'descending'}"
          stripe
        >
          <el-table-column prop="conference.id" label="방번호" width="100"></el-table-column>
          <el-table-column prop="conference.conferenceCategory" label="타입" width="100">
            <template #default="scope">
              <el-tag
                :type="scope.row.conference.conferenceCategory === 'video' ? 'primary' : 'danger'"
                disable-transitions
              >{{scope.row.conference.conferenceCategory}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="conference.title" label="방제목"></el-table-column>
          <el-table-column prop="conference.ownerId.name" label="방장" width="150"></el-table-column>
          <el-table-column prop="conference.callStartTime" label="시작시간" width="200"></el-table-column>
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
          //console.log(result.data)
          let total = {job:"종합", win:result.data.twin + "승", lose:result.data.tlose + "패", rate:result.data.twinRate + "%"}
          let citizen = {job:"시민", win:result.data.cwin + "승", lose:result.data.close + "패", rate:result.data.crate + "%"}
          let doctor = {job:"의사", win:result.data.dwin + "승", lose:result.data.dlose + "패", rate:result.data.drate + "%"}
          let police = {job:"경찰", win:result.data.pwin + "승", lose:result.data.plose + "패", rate:result.data.prate + "%"}
          let mafia = {job:"마피아", win:result.data.mwin + "승", lose:result.data.mlose + "패", rate:result.data.mrate + "%"}
          state.myStat.push(total,citizen,doctor,police,mafia)
        })
        .catch((err)=>{

        })
        store.dispatch('root/requestUserInfo', { token: localStorage.getItem('jwt') })
        .then((result)=>{
          state.userId = result.data.userId
        })
        .catch((err)=>{

        })
      }
      const getHistoryData = () => {
        store.dispatch('root/requestMyHistory', { token: localStorage.getItem('jwt') })
        .then((result)=>{
          //console.log(result.data.slice(19))
          state.historyData = result.data.slice(result.data.length-10)
          state.historyData.forEach(item => {
            let arrangeDate = item.conference.callStartTime.substring(0,10)
            arrangeDate += " "
            arrangeDate += item.conference.callStartTime.substring(11,19)
            item.conference.callStartTime = arrangeDate
          })
          //console.log(state.historyData)
        })
        .catch((err)=>{

        })
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
