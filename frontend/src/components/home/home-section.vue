<template>
  <el-main>
    <div class="space"></div>
    <el-button class="btn-refresh" type="primary" icon="el-icon-refresh-left" @click="clickRefresh">새로고침</el-button>
    <ul class="gameroom-list">
      <li v-for="item in this.listData" class="gameroom-list-item" :key="item.title">
        <game-room :roomData=item @click="clickRoom(item.title)"></game-room>
      </li>
    </ul>
    <el-pagination layout="prev, pager, next" :page-size="1" @current-change="handleCurrentChange" :total="index"></el-pagination>
  </el-main>
</template>

<script>
import GameRoom from '@/components/home/game-room.vue';
import { useRouter } from 'vue-router';
export default {
    name:"HomeSection",
    components:{
      GameRoom,
    },
    methods:{
      handleCurrentChange(val) {
        console.log(this.index);
        this.page = val;
        this.listData = this.filteredData.slice((this.page-1)*8, this.page*8 );
      },
      clickRefresh() {
        console.log("refresh")
        this.$router.go();
      }
    },
    data(){
      return{
        type: "",
        page: 1,
        index: 0,
        roomData: [
          {title: "싸피아게임0", type:"null", member: 6, lock: true, password: "", state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임1", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임2", type:"null", member: 10, lock: false, state: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임3", type:"mafia", member: 7, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임4", type:"mafia", member: 10, lock: false, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임5", type:"mafia", member: 1, lock: false, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임6", type:"mafia", member: 2, lock: false, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임7", type:"mafia", member: 5, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임8", type:"mafia", member: 10, lock: false, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임9", type:"mafia", member: 3, lock: true, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임10", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임11", type:"mafia", member: 9, lock: true, state: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임12", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임13", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임14", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임15", type:"mafia", member: 10, lock: false, state: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임16", type:"mafia", member: 7, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임17", type:"mafia", member: 10, lock: false, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임18", type:"mafia", member: 1, lock: false, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임19", type:"mafia", member: 2, lock: false, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임20", type:"mafia", member: 5, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임21", type:"mafia", member: 10, lock: false, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임22", type:"mafia", member: 3, lock: true, state: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임23", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임24", type:"mafia", member: 9, lock: true, state: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임25", type:"mafia", member: 6, lock: true, state: "accessable", desc: "너만 오면 고"},
        ],
        filteredData: [],
        listData: [],
      };
    },
    created(){
      this.type = this.$route.params.type;
      //roomData 받아오는 axios 요청

      if(this.type === "all"){
        this.filteredData = this.roomData;
      }else{
        roomData.forEach(item => {
          if(item.type === "this.type"){
            this.filteredData.push(item);
          }
        });
      }
      this.listData = this.filteredData.slice((this.page-1)*8, this.page*8 );
      this.index = parseInt(this.filteredData.length / 8);
      if(this.filteredData.length % 8 != 0){
        this.index ++;
      }
    },
    setup(){
      const router = useRouter()
      const clickRoom = function(room) {
        console.log("click" + room)
      }
      return { clickRoom}
    },
}
</script>
<style>

.btn-refresh{
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

</style>
