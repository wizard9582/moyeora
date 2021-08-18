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
        if(room.state == "accessable"){
          if(room.lock){
            emit('openPwCheck', [room.type, room.id])
          }else{
            router.push("/game/" + room.type + "/" + room.id)
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
        //console.log("refresh")
        state.roomData = []
        state.type = router.currentRoute.value.params.type

          store.dispatch('root/requestRoomList')
          .then(function (result) {
            result.data.forEach(item =>{
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
              let data = []
              state.roomData.forEach(item => {
                if(item.type === state.type){
                  data.push(item);
                }
              })
              state.filteredData = data
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
      setInterval(function(){clickRefresh()},10000);
      return { state, clickRoom , handleCurrentChange, clickRefresh, mouseOver, mouseOut }
    },
}
</script>
<style>
</style>
