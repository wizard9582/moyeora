<template>
  <div class="chat-wrapper">
    <div class="chat-header">
        채팅창
    </div>
    <div class="chat-main" style="overflow:auto">
      <ul
        class="list"
        v-infinite-scroll="load"
        infinite-scroll-disabled="disabled">
        <li v-for="i in count" class="list-item" :key="i">{{ i }}</li>
      </ul>
    </div>
    <el-input type="textarea" :rows="2" placeholder="Press Enter for send message." v-model="textarea"></el-input>
  </div>
</template>

<script>
import { ref, computed } from 'vue'

export default {
  name: 'GameChat',

  setup() {
    const count = ref(10);
    const loading = ref(false);
    const noMore = computed(() => count.value >= 20);
    const disabled = computed(() => loading.value || noMore.value);
    const load = () => {
      loading.value = true;
      setTimeout(() => {
        count.value += 2;
        loading.value = false;
      }, 100);
    };
    return { count, disabled, load };
  },
}
</script>

<style>
.chat-wrapper {
  padding: 10px;
}

.chat-header {
  text-align: left;
  font-weight: bold;
}

.chat-main {
  height: 60vh;
}

.list {
  padding: 0;
}

.list .list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  background: #e8f3fe;
  margin: 10px;
  color: #7dbcfc;
}

</style>
