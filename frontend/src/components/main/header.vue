<template>
  <el-header>
    <a href="/home">
      <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="80" height="80"/>
    </a>
    <div class="menu-wrapper">
      <el-button type="primary" v-on:click="clickOnBell">
        <img :src="require('@/assets/img-bell.png')" alt="IceBreaking bell" width="50" height="50"/>
      </el-button>
      <el-button type="primary" v-on:click="clickOnUser">
        <img :src="require('@/assets/img-user.png')" alt="IceBreaking user" width="50" height="50"/>
      </el-button>
    </div>
  </el-header>
</template>

<script>
export default {
  name:'Header',
  setup(props, {emit}){
    const clickOnBell = () => {
      console.log('bell')
    }
    const clickOnUser = () => {
      console.log('user')
    }
    return {clickOnBell, clickOnUser}
  }
}
</script>

<style>
.el-row {
  margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.main-header {
  position: fixed;
  top: 0;
}
.logo-wrapper {
  margin: 0;
  float: left;
}
.menu-wrapper {
  display: inline-block;
  vertical-align: top;
  position: relative;
}
</style>
