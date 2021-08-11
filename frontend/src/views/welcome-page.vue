<template>
  <el-container>
    <Header
      @openSignupPopup="onOpenSignupPopup"
      @openLoginPopup="onOpenLoginPopup"
    />
    <el-container>
      <welcome-section></welcome-section>
      <el-footer height="200">Copyright © SAMSUNG All Rights Reserved.</el-footer>
    </el-container>
  </el-container>
  <signup-pop
    :open="state.signupPopupOpen"
    @closeSignupPopup="onCloseSignupPopup"
  />
  <login-pop
    :open="state.loginPopupOpen"
    @closeLoginPopup="onCloseLoginPopup"
  />
</template>

<script>
import Header from "@/components/main/header.vue";
import WelcomeSection from "@/components/welcome/welcome-section.vue";
import LoginPop from "@/components/welcome/login-pop.vue";
import SignupPop from "@/components/welcome/signup-pop.vue";
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'


export default {
  name: "WelcomePage",
  components: {
    Header,
    WelcomeSection,
    LoginPop,
    SignupPop,
  },

    setup(){
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      signupPopupOpen: false,
      loginPopupOpen: false
    })

    const onOpenSignupPopup = function(){
      state.signupPopupOpen = true
    }
    const onCloseSignupPopup = function(){
      state.signupPopupOpen = false
    }
    const onOpenLoginPopup = function(){
      state.loginPopupOpen = true
    }
    const onCloseLoginPopup = function(){
      state.loginPopupOpen = false
      if(isLoggedIn()){
        router.push("/home/all")
      }
    }
    const isLoggedIn = function () {
      return store.getters['root/isLoggedIn']
    }
    return { state, onOpenSignupPopup, onCloseSignupPopup, onOpenLoginPopup, onCloseLoginPopup, isLoggedIn }
  }
};
</script>

<style>
.el-footer {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  width: 100%;
}
</style>
