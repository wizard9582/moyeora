<template>
  <el-row :gutter="20">
    <el-col class="main-content">
      <h1>Main Content</h1>
    </el-col>
    <el-col class="sub-content">
      <el-row :gutter="20">
        <el-col :span="8" class="sample-image">
          <img src="@/assets/img-vue.png" alt="sample-image">
        </el-col>
        <el-col :span="16">
          <div class="grid-content"></div>
        </el-col>
      </el-row>
    </el-col>
    <el-col class="sub-content">
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="grid-content"></div>
        </el-col>
        <el-col :span="8" class="sample-image">
          <img src="@/assets/img-vue.png" alt="sample-image">
        </el-col>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>
export default {
    name:'WelcomeSection',
}
</script>

<style>
  .el-row {
    margin-bottom: 20px;
  }

  .el-col {
    margin-bottom: 10px;
  }

  .el-row .main-content {
    height: 1000px;
    background: #d3dce6;
    border-radius: 4px;
    text-align: center;
  }

  .sub-content {
    height: 300px;
  }

  .sample-image {
    height: 300px;
    width: 300px;
    text-align: center;
    border: 1px solid black;
    border-radius: 4px;
  }

  .grid-content {
    background: #e5e9f2;
    border-radius: 4px;
    height: 300px;
  }
</style>
