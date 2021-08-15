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
        <el-table class="stat-list" ref="myStatTable" :data="myStat" stripe border>
          <el-table-column prop="job" label="직업"></el-table-column>
          <el-table-column prop="win" label="승리" width="150"></el-table-column>
          <el-table-column prop="lose" label="패배" width="150"></el-table-column>
          <el-table-column prop label="승률" width="150"></el-table-column>
        </el-table>
      </div>
      <div class="sub-title">최근 게임</div>
      <el-divider></el-divider>
      <div class="info-item" id="recentGame">
        <el-table
          class="history-list"
          ref="historyTable"
          :data="historyData"
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
        <el-table class="ranking-list" ref="rankingTable" :data="rankingData" stripe border>
          <el-table-column type="index" label="순위"></el-table-column>
          <el-table-column prop="nickName" label="이름" width="150"></el-table-column>
          <el-table-column prop="win" label="승리" width="150"></el-table-column>
          <el-table-column prop="lose" label="패배" width="150"></el-table-column>
          <el-table-column prop label="승률" width="150"></el-table-column>
        </el-table>
        <el-table class="ranking-list" ref="myRankingTable" :data="myRanking" stripe border>
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
          <li v-for="item in friendList" class="friend-list-item" :key="item">
            <el-avatar icon="el-icon-user-solid" :size="100"></el-avatar>
            <div class="sub-title">{{ item }}</div>
          </li>
          <li class="friend-list-item">
            <el-avatar icon="el-icon-plus" :size="100"></el-avatar>
            <div class="sub-title">친구추가</div>
          </li>
        </ul>
      </div>
    </div>
  </el-main>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'

export default {
    name:"UserSection",
    setup(props, {emit}) {
      const store = useStore()
      const state = reactive({
        userName: "",
        userId: "",
        userDepartment: "",
        friendList: [],
        myStat:[
          {
            job: "총합",
            win: 163,
            lose: 54
          },
          {
            job: "마피아",
            win: 15,
            lose: 7
          },
          {
            job: "경찰",
            win: 15,
            lose: 7
          },
          {
            job: "의사",
            win: 15,
            lose: 7
          },
          {
            job: "시민",
            win: 15,
            lose: 7
          }
        ],
        historyData: [{
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'mafia'},
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Lose',
          job: 'mafia'},
          {
            no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'citizen'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Lose',
          job: 'mafia'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'mafia'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Lose',
          job: 'mafia'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'mafia'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'citizen'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Lose',
          job: 'citizen'
          },
          {
          no: 13921,
          tag: 'mafia',
          title: '김철수의 마피아게임',
          date: '2016-05-03',
          result: 'Win',
          job: 'police'
          },],
          rankingData: [
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
            {
              nickName: "김철수",
              win: 123,
              lose: 35,
            },
          ],
          myRanking: [{
              my: "나의 순위",
              nickName: "김싸피",
              win: 11,
              lose: 23,
              },
            ],
      })


      store.dispatch('root/request')
      .then(function (result){
        console.log(result)
        state.tableData = result.data
      })
      .catch(function (err){

      })

      return {state}
    }
}
</script>

<style>
</style>
