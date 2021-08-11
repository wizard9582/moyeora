<template>
  <el-main>
    <div class="space"></div>
    <h1>MOYEORA</h1>
    <iframe width="560" height="315" src="https://www.youtube.com/embed/-FMHYjqC4iA" title="YouTube video player"
    frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
    <div class="space">프로젝트 설명</div>
    <el-row class="sub-content">
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
    </el-row>
    <el-row class="sub-content">
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
    </el-row>
    <el-row class="sub-content">
      <el-col :span="8" class="sample-image">
        <img src="@/assets/img-vue.png" alt="sample-image">
      </el-col>
      <el-col :span="16">
        <div class="grid-content"></div>
      </el-col>
    </el-row>
  </el-main>
</template>

<script>
export default {
    name:'WelcomeSection',
}
</script>

<style>
.space {
  height: 100px;
  width: 100%;
}
.sub-content {
    height: 300px;
}
.sub-content .sample-image .img{
    min-width: 100px;
    width: 30%;
    height: auto;
}
.grid-content {
    background: #e5e9f2;
    border-radius: 4px;
    height: 300px;
}
</style>
