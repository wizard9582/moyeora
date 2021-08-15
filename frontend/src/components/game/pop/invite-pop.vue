<template>
  <el-dialog title="친구 초대하기" v-model="state.popupVisible" @close="handleClose">
    <el-form :label-position="state.align">
      <el-form-item prop="friend" label="초대할친구" :label-width="state.formLabelWidth">
        <el-input v-model="state.friendName"></el-input>
        <el-card v-if="state.isSetRecommands">
          <div v-for="o in state.recommands" :key="o" @click="selectRecommand(o)">{{o}}</div>
        </el-card>
      </el-form-item>
      <el-form-item prop="link" label="초대 링크" :label-width="state.formLabelWidth">
        <el-input id="invite-link" type="textarea" :rows="2" v-model="state.inviteLink"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="footer">
        <el-button type="primary" @click="invite">초대</el-button>
        <el-button type="primary" @click="copyLink">링크 복사</el-button>
        <el-button type="primary" @click="handleClose">닫기</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, computed, watch } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

export default {
  name: "InvitePop",

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
    const copyLink = () => {
      let link = document.getElementById('invite-link')
      link.select()

      document.execCommand("copy")
    }

    const handleClose = () => {
      state.friendName=""
       state.recommands=[]
      emit('closeInvitePopup')
    }

    const selectRecommand = (value) =>{
      state.isSetRecommands=false;
      state.friendName =value;
    }

    const invite = () =>{
        store.dispatch('root/requestInvite',{toUser: state.friendName, fromUser : state.userName, url : state.inviteLink })
        .then((res) => {
          if(res.data=='fail'){
            alert("존재하지 않는 계정 입니다")
            return;
          }

          alert(state.friendName+"님에게 초대를 완료했습니다.")
        })
        
    }
    return { state, copyLink, handleClose,selectRecommand,invite }
  },
}
</script>

<style>
</style>
