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

  setup(){
    let theme = localStorage.getItem('theme')
    let fontSize = localStorage.getItem('fontSize')
    if(theme == null){
      theme = "main"
    }else if(theme == "기본테마"){
      theme = "main"
    }else if(theme == "어둠의자식"){
      theme = "dark"
    }else if(theme == "레트로펑크"){
      theme = "punk"
    }else if(theme == "여름감성"){
      theme = "summer"
    }else if(theme == "핑크 핫"){
      theme = "pink"
    }

    const style = document.documentElement.style;
    style.setProperty('--color-light', 'var(--'+theme+'-light)');
    style.setProperty('--color-default', 'var(--'+theme+'-default)');
    style.setProperty('--color-dark', 'var(--'+theme+'-dark)');
    style.setProperty('--color-button', 'var(--'+theme+'-button)');
    style.setProperty('--color-font', 'var(--'+theme+'-font)');
    style.setProperty('--font-size', fontSize);
  }
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
