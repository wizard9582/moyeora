<template>
  <el-dialog title="친구 추가하기" v-model="state.popupVisible" @close="handleClose">
    <el-form :label-position="state.align">
      <el-form-item prop="friend" label="요청할친구" :label-width="state.formLabelWidth">
        <el-input v-model="state.friendName"></el-input>
        <el-card v-if="state.isSetRecommands">
          <div v-for="o in state.recommands" :key="o" @click="selectRecommand(o)">{{o}}</div>
        </el-card>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="footer">
        <el-button type="primary" @click="addFriend">친구요청</el-button>
        <el-button type="danger" @click="handleClose">닫기</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, watch } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

export default {
  name: "FriendPop",

  props: {
    open: {
      type: Boolean,
      default: false
    },
  },

  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()

    const state = reactive({
      formLabelWidth: '120px',
      inviteLink: window.location.href,
      align: 'left',
      popupVisible: computed(() => props.open),
      friendName: '',
      recommands:[],
      isSetRecommands : false,
      userName: computed(() => store.getters['root/getUserId']),
    })
    watch(
      () =>state.friendName,
      (friendName)=>{

        if(friendName.length==0){
          state.recommands = []
          state.isSetRecommands=false;
          return;
        }

        store.dispatch('root/requestRecommandFriendList',{friendName: friendName})
        .then((res) => {
          if(res.data.length==0){
            state.isSetRecommands=false;
            return;
          }
          state.recommands=res.data;
          state.isSetRecommands=true;
        })
      }
    )
    // 링크를 클립보드에 복사해주는 함수
    // 우선은 textarea로 설정 input text 로도 가능

    const handleClose = () => {
      state.friendName=""
      state.recommands=[]
      emit('closeFriendPopup')
    }

    const selectRecommand = (value) =>{
      state.isSetRecommands=false;
      state.friendName =value;
    }

    const addFriend = () =>{
        store.dispatch('root/requestInvite',{toUser: state.friendName, fromUser : state.userName })
        .then((res) => {
          if(res.data=='fail'){
            alert("존재하지 않는 계정 입니다")
            return;
          }

          alert(state.friendName+"님에게 친구요청을 보냈습니다.")
        })

    }
    return { state, handleClose, selectRecommand, addFriend}
  },
}
</script>

<style>
</style>
