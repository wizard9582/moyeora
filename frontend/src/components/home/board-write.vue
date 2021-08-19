<template>
	<el-main class="main-write">
    <div class="space"></div>
    <div class="write-wrapper">
      <h4>{{state.form.typeTitle}}</h4>
      <el-form :model="state.form" :rules="state.rules" ref="writeForm" :label-position="state.form.align">
        <el-form-item>
          <h4>제목</h4>
          <el-input v-model="state.form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <h4>내용</h4>
          <el-input v-model="state.form.text" :rows=20 type="textarea"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="write-btn-wrapper">
      <el-button type="primary" icon="el-icon-edit-outline" size="small" @click="clickWrite()">글쓰기</el-button>
      <el-button type="danger" icon="el-icon-delete-solid" size="small" @click="clickList()">취소</el-button>
    </div>
	</el-main>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default {
  name:"BoardWrite",

  setup(){
    const writeForm = ref(null)
    const store = useStore()
    const router = useRouter()

    const type = router.currentRoute.value.params.type
    let userId = store.getters['root/getUserId']

    const state = reactive({
      form: {
        typeTitle: '',
        title: '',
        text: '',
        align: 'left',
      },
      rules: {
        title: [
          { required: true, message: '제목을 입력하세요.', trigger: 'change' },
        ],
        text: [
          { required: true, message: '글 내용을 입력하세요.', trigger: 'change' },
        ],
      },
    })
    state.form.typeTitle = type == 0 ? "게시판 글쓰기" : "공지사항 글쓰기"
    const clickList = function(){
      if(type == 1){
         router.push("/home/notice/list")
      }else{
         router.push("/home/board/list")
      }
    }
    const clickWrite = function(){
      writeForm.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestPostBoard', { title: state.form.title, description: state.form.text, notice: type, userId: userId})
          .then(function (result) {
            //console.log(result)
            if(result.status == 200){
              if(type == 1){
                router.push("/home/notice/list")
              }else{
                router.push("/home/board/list")
              }
            }else{
              alert('글 작성이 실패했습니다!')
            }
          })
          .catch(function () {
            alert('글 작성이 실패했습니다!')
          })
        } else {
          alert('글 내용을 작성해주세요!')
        }
      });
    }

    return { state, writeForm, clickList, clickWrite }
  }
}
</script>

<style>
</style>
