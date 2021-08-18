<template>
  <el-main>
    <div class="space"></div>
    <h1>게시판</h1>
    <el-table
      class="board-list"
      ref="filterTable"
      :data="state.tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
      @row-click="clickBoard"
      :default-sort="{prop: 'date', order: 'descending'}"
      border
      stripe
    >
      <el-table-column type="index" label="글번호" width="150"></el-table-column>
      <el-table-column
        prop="tag"
        label="태그"
        width="100"
        :filters="[{ text: '자유', value: 'free' }, { text: 'mafia', value: 'mafia' },]"
        :filter-method="filterTag"
        filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag
            :type="scope.row.tag === '자유' ? 'primary' : 'success'"
            disable-transitions
          >{{scope.row.tag}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="제목"></el-table-column>
      <el-table-column prop="date" label="작성일" width="180"></el-table-column>
      <el-table-column prop="userId" label="글쓴이" width="180"></el-table-column>
    </el-table>
    <div class="board-under-wrapper">
      <div class="search-wrapper">
        <el-input v-model="state.search" size="mini" placeholder="Type to search" clearable />
      </div>
      <div class="select-wrapper">
        <el-pagination :page-size="10" :pager-count="5" layout="prev, pager, next" :total="1000"></el-pagination>
      </div>
      <el-button
        class="btn-write"
        type="success"
        icon="el-icon-edit-outline"
        size="small"
        @click="clickWrite()"
      >글쓰기</el-button>
    </div>
  </el-main>
</template>

<script>
import { reactive } from '@vue/reactivity'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default {
    name:"BoardList",
    setup(){
      const router = useRouter()
      const store = useStore()

      const state = reactive({
        tableData : [],
      })

      store.dispatch('root/requestBoard')
      .then(function (result){
        console.log("result---->",result)
        state.tableData = result.data
      })
      .catch(function (err){

      })

      const clickBoard = function(row){
        //console.log(row)
        router.push("/home/board/" + row.id)
      }

      const filterTag = function(value, row) {
        return row.tag === value;
      }

      const filterHandler = function(value, row, column) {
        let property = column['property'];
        return row[property] === value;
      }

      const clickWrite = function() {
        router.push( { path:"/home/board/write/0" } )
      }

      return { state, clickBoard, filterTag, filterHandler, clickWrite }
    }
}
</script>

<style>
</style>
