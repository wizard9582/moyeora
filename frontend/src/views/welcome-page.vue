<template>
  <div>
    <Header></Header>
    <welcome-section></welcome-section>
    <login-pop v-if="false"></login-pop>
    <signup-pop v-if="false"></signup-pop>
  </div>
</template>

<script>
import Header from "./main/header.vue";
import WelcomeSection from "./welcome/welcome-section.vue";
import LoginPop from "./welcome/login-pop.vue";
import SignupPop from "./welcome/signup-pop.vue";

export default {
  name: "WelcomePage",
  Components: {
    Header,
    WelcomeSection,
    LoginPop,
    SignupPop,
  },
};
</script>

<style></style>
