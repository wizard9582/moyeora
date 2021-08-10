<template>
	<el-main>
    <div class="space"></div>
    <h1>공지사항</h1>
    <el-descriptions class="board-text" :title="state.id" :column="3" border>
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
        {{state.description}}
      </div>
        <el-button type="info" icon="el-icon-tickets" size="small" @click="clickList()">목록으로</el-button>
        <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="clickEdit()">수정</el-button>
        <el-button type="danger" icon="el-icon-delete-solid" size="small" @click="clickDelete()">삭제</el-button>
    </div>
	</el-main>
</template>

<script>
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default {
  name:"NoticeSection",

  setup(){
    const router = useRouter()
    const store = useStore()
    const state = {
      id: 0,
      writer: "",
      tag: "",
      date: "",
      title: "",
      description: ""
    }

    state.id = router.currentRoute.value.params.id

    const clickList = function(){
      router.push("/home/notice/list")
    }
    const clickEdit = function(){
      router.push( { path:"/home/board/edit/1/" + state.id } )
    }
    const clickDelete = function(){
      store.dispatch('root/requestCheckWriter', {postId: state.id})
      .then(function (result){
        console.log(result)
        //삭제 요청
      })
      .catch(function (err){
        alert('내가 작성한 글이 아닙니다')
      })
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
