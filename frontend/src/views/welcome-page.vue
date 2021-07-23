<template>
  <div>
    <div>WelcomePage</div>
    <Header></Header>
    <welcome-section></welcome-section>
    <login-pop></login-pop>
    <signup-pop></signup-pop>
  </div>
</template>

<script>
import Header from "@/components/main/header.vue";
import WelcomeSection from "@/components/welcome/welcome-section.vue";
import LoginPop from "@/components/welcome/login-pop.vue";
import SignupPop from "@/components/welcome/signup-pop.vue";

export default {
  name: "WelcomePage",
  components: {
    Header,
    WelcomeSection,
    LoginPop,
    SignupPop,
  },
};
</script>

<style></style>
