<template>
  <el-header>
    <a href="/home" class="logo">
      <!-- <img :src="require('@/assets/img-logo.png')" alt="IceBreaking logo" width="70" height="50"/> -->
      <img :src="require('@/assets/img-vue.png')" alt="IceBreaking logo" width="50" height="50"/>
    </a>
    <ul class="nav">
      <li>
        <el-badge :value="6">
          <el-button icon="el-icon-bell" circle v-on:click="clickOnBell" ></el-button>
        </el-badge>
      </li>
      <li>
        <el-button icon="el-icon-user" circle v-on:click="clickOnUser" ></el-button>
      </li>
    </ul>
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
.logo {
  float: left;
  margin-top: 10px;
}
.nav{
  float: right;
  list-style: none;
  margin-right: 5px;
}
.nav li{
  float: left;
  margin: 5px;
}
</style>
