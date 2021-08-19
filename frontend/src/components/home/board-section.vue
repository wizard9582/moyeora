<template>
	<el-main>
    <div class="space"></div>
    <h1>게시판</h1>
    <el-descriptions class="board-text" :column="3" border>
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
    <div class="board-text">
      <div class="context-text">
        {{state.description}}
      </div>
      <el-divider></el-divider>
      <el-button type="info" icon="el-icon-tickets" size="small" @click="clickList()">목록으로</el-button>
      <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="clickEdit()">수정</el-button>
      <el-button type="danger" icon="el-icon-delete-solid" size="small" @click="clickDelete()">삭제</el-button>
      <el-divider></el-divider>
    </div>
	</el-main>
</template>

<script>
import { reactive } from '@vue/reactivity'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default {
  name:"BoardSection",

  setup(){
    const router = useRouter()
    const store = useStore()
    const state = reactive({
      id: 0,
      writer: "",
      tag: "",
      date: "",
      title: "",
      description: ""
    })

    state.id = router.currentRoute.value.params.id
    store.dispatch('root/requestBoard')
    .then(function (result){
      //console.log(result)
      let content;
      result.data.forEach(item => {
        if(item.id == state.id){ content = item }
      })
      console.log(content)
      state.writer = content.userId
      state.date = content.date
      state.title = content.title
      state.description = content.description
    })
    .catch(function (err){

    })


    const clickList = function(){
      router.push("/home/board/list")
    }
    const clickEdit = function(){
      store.dispatch('root/requestCheckWriter', {postId: state.id})
      .then(function (result){
        if(result.status == 200){
          router.push( { path:"/home/board/edit/0/" + state.id } )
        }else{
          alert('내가 작성한 글이 아닙니다')
        }
      })
      .catch(function (err){
        alert('내가 작성한 글이 아닙니다')
      })
    }
    const clickDelete = function(){
      store.dispatch('root/requestCheckWriter', {postId: state.id})
      .then(function (result){
        if(result.status == 200){
          store.dispatch('root/requestDeleteBoard', {postId: state.id})
          .then(function(result){
            if(result.status == 200){
              alert('삭제에 성공했습니다.')
              router.push( { path:"/home/board/list"} )
            }else{
              alert('삭제에 실패했습니다.')
            }
          })
        }else{
          alert('내가 작성한 글이 아닙니다')
        }
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
</style>
