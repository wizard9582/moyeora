<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-skeleton style="width: 100%">
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 150px" />
        </template>
      </el-skeleton>
      </div>
    <div class="title-wrapper">
      <span class="title">{{ roomdata.title }}&nbsp;&nbsp;</span>
      <i v-if="roomdata.lock" class="el-icon-lock"></i>&nbsp;
      <el-tag size="small">{{ roomdata.member }}/10</el-tag>&nbsp;
      <el-tag v-if="roomdata.roomstate==='accessable'" size="small" class="accessable">접속가능</el-tag>
      <el-tag v-if="roomdata.roomstate==='wating'" size="small" class="wating">대기중</el-tag>
      <el-tag v-if="roomdata.roomstate==='playing'" size="small" class="playing">게임중</el-tag>
      <div class="bottom">
        <span>{{ roomdata.desc }}</span>
      </div>
    </div>
  </el-card>
</template>
<style>
.el-card {
  margin: 0 8px;
  margin-bottom: 20px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 160px;
}
.el-card .title-wrapper {
  text-align: left;
  padding: 14px;
}
.el-card .title-wrapper .accessable{
  background: green;
  color: white;
}
.el-card .title-wrapper .wating{
  background: gray;
  color: white;
}
.el-card .title-wrapper .playing{
  background: tomato;
  color: white;
}
.el-card .title-wrapper .title {
  font-weight: bold;
}
.el-card .bottom {
  margin-top: 5px;
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
}
</style>
<script>
export default {
  name: "GameRoom",
    props:{
      roomdata:{
        type: Object,
        default: {title: "싸피아게임0", member: 6, lock: true, roomstate: "accessable", desc: "너만 오면 고"},
      }
    },
    // props:{
    //   title: {
    //     type: String,
    //     default: "싸피아게임"
    //   },
    //   member: {
    //     type: Number,
    //     default: 6
    //   },
    //   lock: {
    //     type: Boolean,
    //     default: true
    //   },
    //   roomstate: {
    //     type: String,
    //     default: "wating"
    //   },
    //   desc: {
    //     type: String,
    //     default: "너만 오면 고"
    //   }
    // },

  setup () {},
}
</script>
