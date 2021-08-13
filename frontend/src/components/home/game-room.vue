<template>
  <el-card :body-style="{ padding: '0px' }" shadow="hover">
    <div class="image-wrapper">
      <el-skeleton style="width: 100%">
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 150px" />
        </template>
      </el-skeleton>
    </div>
    <div class="title-wrapper">
      <span class="title">{{ roomData.title }}&nbsp;&nbsp;</span>
      <i v-if="roomData.lock" class="el-icon-lock"></i>&nbsp;
      <el-tag v-if="roomData.state==='accessable'" size="small" class="accessable">접속가능</el-tag>
      <el-tag v-else-if="roomData.state==='full'" size="small" class="full">가득참</el-tag>
      <el-tag v-else-if="roomData.state==='playing'" size="small" class="playing">게임중</el-tag>
      <el-tag v-else size="small" class="empty">빈 방</el-tag>&nbsp;
      <el-tag size="small">{{ roomData.member }}/10</el-tag>
      <div class="bottom">
        <span>{{ roomData.desc }}</span>
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
.el-card .title-wrapper .full{
  background: blue;
  color: white;
}
.el-card .title-wrapper .playing{
  background: tomato;
  color: white;
}
.el-card .title-wrapper .empty{
  background: gray;
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
      roomData:{
        type: Object,
        default: {title: "빈 방", member: 0, lock: true, state: "empty", desc: "빈 방입니다."},
      }
    },
  setup () {},
}
</script>
