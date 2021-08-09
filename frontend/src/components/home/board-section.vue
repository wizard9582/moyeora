<template>
	<el-main>
    <div class="space"></div>
    <h1>게시판</h1>
    <el-descriptions class="board-text" :title="state.no" :column="3" border>
      <template #extra>
        <el-button type="info" icon="el-icon-tickets" size="small" @click="clickList()">목록으로</el-button>
        <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="clickEdit()">수정</el-button>
        <el-button type="danger" icon="el-icon-delete-solid" size="small" @click="clickDelete()">삭제</el-button>
      </template>
      <el-descriptions-item label="글쓴이">{{state.writer}}</el-descriptions-item>
      <el-descriptions-item label="태그">
        <el-tag size="small">{{state.tag}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="작성일">{{state.date}}</el-descriptions-item>
      <el-descriptions-item label="제목">{{state.title}}</el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <div class="board-text">
      <div class="context-text">
        {{state.context}}
      </div>
        <el-button type="info" icon="el-icon-tickets" size="small" @click="clickList()">목록으로</el-button>
        <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="clickEdit()">수정</el-button>
        <el-button type="danger" icon="el-icon-delete-solid" size="small" @click="clickDelete()">삭제</el-button>
    </div>
	</el-main>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name:"BoardSection",

  setup(){
    const state = {
      no:0,
      writer: "",
      tag: "",
      date: "",
      title: "",
      context: ""
    }
    const router = useRouter()
    const clickList = function(){
      router.push("list")
    }
    const clickEdit = function(){

    }
    const clickDelete = function(){

    }
    return {state, clickList, clickEdit, clickDelete}
  }
}
</script>

<style>
.board-text{
  margin-left: 15%;
  width: 70%;
  margin-right: 15%;
}
.board-text .context-text{
  text-align: left;
  margin: 5%;
  padding: 5%;
  background: white;
  min-height: 500px;
}
</style>
