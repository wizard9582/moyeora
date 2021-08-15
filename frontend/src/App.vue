<template>
  <router-view />
</template>

<script>
import GamePage from '@/views/game-page.vue';
import HomePage from '@/views/home-page.vue';
import WelcomePage from '@/views/welcome-page.vue';

export default {
  name: "App",
  components: {
    GamePage,
    HomePage,
    WelcomePage,
  },
};
</script>

<style src="./common/css/main.css">

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

body {
  margin: 0;
}
</style>
