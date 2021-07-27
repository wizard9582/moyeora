<template>
  <el-container>
    <Header
      @openSignupPopup="onOpenSignupPopup"
      @openLoginPopup="onOpenLoginPopup"
    />
    <el-main>
      <welcome-section></welcome-section>
    </el-main>
    <el-footer>Copyright © SAMSUNG All Rights Reserved.</el-footer>
  </el-container>
  <signup-pop
    :open="signupPopupOpen"
    @closeSignupPopup="onCloseSignupPopup"
  />
  <login-pop
    :open="loginPopupOpen"
    @closeLoginPopup="onCloseLoginPopup"
  />
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
  data () {
    return {
      signupPopupOpen: false,
      loginPopupOpen: false,
    }
  },
  methods: {
    onOpenSignupPopup () {
      this.signupPopupOpen = true
    },
    onCloseSignupPopup () {
      this.signupPopupOpen = false
    },
    onOpenLoginPopup () {
      this.loginPopupOpen = true
    },
    onCloseLoginPopup () {
      this.loginPopupOpen = false
    }
  }
};
</script>

<style>

.el-footer {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  width: 100%;
  height: 200px;
}

</style>
