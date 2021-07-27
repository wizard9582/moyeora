<template>
  <el-menu class="el-menu-vertical-demo">
    <el-menu-item>
      <i class="el-icon-menu"/>
      <span>Game</span>
    </el-menu-item>
    <el-menu-item>
      <i class="el-icon-plus"/>
      <span>Room</span>
    </el-menu-item>
    <el-menu-item>
      <i class="el-icon-setting"/>
      <span>Setting</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
    name:"SideBar",

}
</script>

<style>
.el-menu-item{
  cursor: pointer;
  margin-left: 0px;
  background: whitesmoke;
}
</style>
