<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png" />
    <div id="container">
      <div id="wrapper">
        <div id="join" class="animate join">
          <h1>Join a Room</h1>
          <!-- <form @submit="register" accept-charset="UTF-8">  -->
          <form @submit.prevent="register" accept-charset="UTF-8">
            <p>
              <input type="text" name="name" value id="name" placeholder="Username" required />
            </p>
            <p>
              <input type="text" name="room" value id="roomName" placeholder="Room" required />
            </p>
            <p class="submit">
              <input type="submit" name="commit" value="Join!" />
            </p>
          </form>
        </div>
        <div id="room" style="display: none;">
          <h2 id="room-header"></h2>
          <div id="participants"></div>
          <input type="button" id="button-leave" @mouseup="leaveRoom" value="Leave room" />
        </div>
      </div>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App" />
  </div>
</template>


<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";
import adapter from 'webrtc-adapter';
import {register, leaveRoom} from "@/assets/js/conferenceroom"

export default {
  name: "Home",
  components: {
    HelloWorld,
  },
  methods:{
    register,
    leaveRoom
  },
}
</script>