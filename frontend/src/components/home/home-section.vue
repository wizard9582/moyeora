<template>
  <el-main>
    <div class="space"></div>
    <ul class="gameroom-list">
      <li v-for="i in 12" class="gameroom-list-item" :key="i">
        <game-room :roomdata="roomdata[i]"></game-room>
      </li>
    </ul>
  </el-main>
</template>

<script>
import GameRoom from '@/components/home/game-room.vue';
export default {
    name:"HomeSection",
    components:{
      GameRoom,
    },
    data(){
      return{
        //객체배열 순회가 1번부터 돔
        roomdata: [
          {title: "싸피아게임0", member: 6, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임1", member: 6, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임2", member: 10, lock: false, roomstate: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임3", member: 7, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임4", member: 10, lock: false, roomstate: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임5", member: 1, lock: false, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임6", member: 2, lock: false, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임7", member: 5, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임8", member: 10, lock: false, roomstate: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임9", member: 3, lock: true, roomstate: "wating", desc: "너만 오면 고"},
          {title: "싸피아게임10", member: 6, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
          {title: "싸피아게임11", member: 9, lock: true, roomstate: "playing", desc: "너만 오면 고"},
          {title: "싸피아게임12", member: 6, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
        ],
      };
    },
}
</script>
<style>
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
