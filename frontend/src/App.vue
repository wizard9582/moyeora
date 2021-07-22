<template>
  <div>
    <router-view />
  </div>
</template>

<script>
import WelcomePage from "./views/welcome-page.vue";
import HomePage from "./views/home-page.vue";
import GamePage from "./views/game-page.vue";

export default {
  name: "App",
  Components: {
    WelcomePage,
    HomePage,
    GamePage,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
