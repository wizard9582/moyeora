<template>
  <el-dialog class="gameFilter" title="게임선택" v-model="state.popupVisible" @close="handleClose">
    <ul class="gameFilter-list">
      <li v-for="i in 12" class="gameFilter-list-item" :key="i">
        <div>
          <el-skeleton-item variant="image" style="width: 100%; height: 200px" />
        </div>
      </li>
    </ul>
  </el-dialog>
</template>

<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'

export default {
  name:"FilterPop",
  props: {
    open: {
      type: Boolean,
      default: false,
    }
  },

  setup(props, { emit }) {
    const store = useStore()

    const state = reactive({
      formLabelWidth: '120px',
      popupVisible: computed(() => props.open),
    })

    const handleClose = function () {
      emit('closeFilterPopup')
    }

    return {state, handleClose}
  }
}
</script>

<style>
.gameFilter-list-item {
  min-width: 200px;
  max-width: 20%;
  min-height: 200px;
  max-height: 20%;
  display: inline-block;
  cursor: pointer;
  margin: 10px;
}
</style>
