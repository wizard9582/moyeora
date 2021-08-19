<template>
  <el-card :body-style="{ padding: '0px' }" shadow="hover">
    <div class="image-wrapper" style="width: 100%; height: 150px; background-color: white;">
      <img :src="require(`@/assets/img-${roomData.type}.png`)" class="img-game" alt="yet" style="width: auto; height: 150px; border: 0px"/>
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
</style>
<script>
export default {
  name: "GameRoom",
    props:{
      roomData:{
        type: Object,
        default: {title: "빈 방", member: 0, lock: true, state: "empty", desc: "빈 방입니다.", type: "video"},
      }
    },
  setup () {},
}
</script>
