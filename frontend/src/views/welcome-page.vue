<template>
  <el-container>
    <Header
      @openSignupPopup="onOpenSignupPopup"
      @openLoginPopup="onOpenLoginPopup"
    />
    <el-main>
      <welcome-section></welcome-section>
    </el-main>
    <el-footer>Footer</el-footer>
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
/* @media (max-width: 700px) {
  .main-container {
    height: calc(100% - 275px) !important;
  }
}

#app, .main-wrapper {
  height: 100%;
  display: block !important;
}

.el-row.main-header {
  width: 100%;
  display: block;
  max-width: 100%;
  margin-left: 0 !important;
  margin-right: 0 !important;
  border-bottom: 1px solid #dcdfe6;
}

.el-row.main-sidebar {
  height: 100%;
  display: inline-block;
}

.main-container {
  height: calc(100% - 195px);
}

.el-row.main-footer {
  z-index: -1;
  margin-left: 0 !important;
  margin-right: 0 !important;
  border-top: 1px solid #dcdfe6;
} */
</style>
