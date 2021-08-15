<template>
  <el-main>
    <div class="space"></div>
    <el-button class="btn-refresh" type="primary" :icon="state.refresh" :round="true" @click="clickRefresh" @mouseover="mouseOver" @mouseleave="mouseOut">새로고침</el-button>
    <div v-if="state.noRoom">
      현재 생성된 방이 없습니다!
    </div>
    <ul class="gameroom-list">
      <li v-for="item in state.listData" class="gameroom-list-item" :key="item.id">
        <game-room :roomData="item" @click="clickRoom(item)"></game-room>
      </li>
    </ul>
    <el-pagination layout="prev, pager, next" :page-size="1" @current-change="handleCurrentChange" :total="state.index"></el-pagination>
  </el-main>
</template>

<script>
import GameRoom from '@/components/home/game-room.vue';
import { reactive } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'
export default {
    name:"HomeSection",
    components:{
      GameRoom,
    },

    setup(props, {emit}){
      const store = useStore()
      const router = useRouter()

      const state = reactive({
        refresh:"el-icon-refresh-left",
        noRoom: false,
        roomData: [],
        filteredData: [],
        listData: [],
        type: "",
        page: 1,
        index:  0
      })
      const clickRoom = function(room) {
        let route = room.type + "/" + room.id
        if(room.state == "accessable"){
          if(room.lock){
            emit('openPwCheck', ["1234", route])
          }else{
            router.push("/game/" + route)
          }
        }
      }
      const handleCurrentChange = function(val) {
        state.page = val;
        state.listData = state.filteredData.slice((state.page-1)*8, state.page*8 );
      }
      const mouseOver = () => {
        state.refresh = "el-icon-loading"
      }
      const mouseOut = () => {
        state.refresh = "el-icon-refresh-left"
      }

      const clickRefresh = function() {
        console.log("refresh")
        //router.go()
        state.roomData = []
        state.type = router.currentRoute.value.params.type

          // console.log("current ->", router.currentRoute)
          // console.log("current ->", router.currentRoute.value.params.type)
          // console.log("route ->", router.getRoutes())
          // console.log("optios ->", router.options)

          store.dispatch('root/requestRoomList')
          .then(function (result) {
            console.log(result.data)
            result.data.forEach(item =>{
              //console.log(roomData)
              //console.log("requestRoomList : "+item.id + " / "+item.count);
              let conference = {id: 0, title: "", type:"", member: 0, lock: true, password: "", state: "", desc: ""}
              conference.id = item.id
              conference.title = item.title
              conference.type = item.category
              conference.lock = item.private
              conference.desc = item.description
              //참가자 수 확인과 룸 상태 변경은 나중에 구현
              conference.member = item.count;
              conference.state = item.state;
              //console.log(conference)
              state.roomData.push(conference)
            })

            if(state.type === "all"){
              state.filteredData = state.roomData;
            }else{
              state.roomData.forEach(item => {
                if(item.type === state.type){
                  state.filteredData.push(item);
                }
              })
            }
            //console.log("filteredData",filteredData)

            state.listData = state.filteredData.slice((state.page-1)*8, state.page*8 );
            state.index = parseInt(state.filteredData.length / 8);

            if(state.filteredData.length % 8 != 0){
              state.index = state.index + 1;
            }
            if(state.filteredData.length == 0){
              state.noRoom = true;
            }
          })
          .catch(function (err) {
            alert(err)
          })
      }
      clickRefresh()
      return { state, clickRoom , handleCurrentChange, clickRefresh, mouseOver, mouseOut }
    },
}
</script>
<style>
.btn-refresh {
  position: fixed;
  top: 80px;
  right: 50px;
}
.gameroom-list {
  max-height: calc(100% - 35px);
}
@media (min-width: 701px) and (max-width: 1269px) {
  .gameroom-list {
    min-width: 700px;
  }
}
@media (min-width: 1270px) {
  .gameroom-list {
    min-width: 1021px;
  }
}
.gameroom-list .gameroom-list-item {
  min-width: 320px;
  max-width: 25%;
  display: inline-block;
  cursor: pointer;
}

.gameroom-list-item {
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
.gameroom-list-item:hover {
  -webkit-transform:scale(1.1);
  -moz-transform:scale(1.1);
  -ms-transform:scale(1.1);
  -o-transform:scale(1.1);
  transform:scale(1.1);
  z-index: 5000;
}
</style>
