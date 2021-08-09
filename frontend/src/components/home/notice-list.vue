<template>
    <el-main>
      <div class="space"></div>
      <h1>공지사항</h1>
       <el-table class="board-list" ref="filterTable"
      :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
      @row-click="clickBoard"
      :default-sort = "{prop: 'date', order: 'descending'}" border stripe>
        <el-table-column prop="no" label="글번호" width="150"></el-table-column>
        <el-table-column prop="tag" label="태그" width="100"
        :filters="[{ text: '자유', value: 'free' }, { text: 'mafia', value: 'mafia' },]"
        :filter-method="filterTag" filter-placement="bottom-end">
          <template #default="scope">
            <el-tag :type="scope.row.tag === '자유' ? 'primary' : 'success'" disable-transitions>{{scope.row.tag}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="제목"></el-table-column>
        <el-table-column prop="date" label="작성일" width="180"></el-table-column>
        <el-table-column prop="name" label="글쓴이" width="180"></el-table-column>
      </el-table>
      <div class="board-under-wrapper">
        <div class="select-wrapper">
          <el-pagination :page-size="10" :pager-count="5" layout="prev, pager, next" :total="1000"></el-pagination>
        </div>
        <div class="search-wrapper">
          <el-input v-model="search" size="mini" placeholder="Type to search" clearable/>
        </div>
      </div>
    </el-main>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
    name:"BoardList",
    setup(){
      const router = useRouter()

      const clickBoard = function(row){
        //console.log(row)
        router.push(row.no.toString())
      }

      return { clickBoard }
    },
    data() {
      return {
        search: '',
        tableData: [{
          no: 1,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 2,
          tag: '자유',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 3,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 4,
          tag: '자유',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 5,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 6,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 7,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 8,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 9,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },{
          no: 10,
          tag: 'mafia',
          title: 'Test용 글 1번',
          date: '2016-05-03',
          name: 'Tom',
        },]
      }
    },
    methods:{
      filterTag(value, row) {
        return row.tag === value;
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      }
    }
}
</script>

<style>
.board-list{
  margin-left: 15%;
  width: 70%;
  margin-right: 15%;
}
.board-under-wrapper{
  padding: 50px;
}
.board-under-wrapper .select-wrapper{
  display: inline-block;
  min-width: 300px;
  max-width: 70%;
  margin-left: 5%;
}
.board-under-wrapper .search-wrapper{
  display: inline-block;
  min-width: 100px;
  max-width: 20%;
  margin-left: 5%;
}
</style>
