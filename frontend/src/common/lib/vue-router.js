import { createRouter, createWebHistory } from "vue-router";

import WelcomePage from "@/views/welcome-page.vue";
import HomePage from "@/views/home-page.vue";
import GamePage from "@/views/game-page.vue";
import HomeSection from "@/components/home/home-section.vue";
import UserSection from "@/components/home/user-section.vue";
import BoardSection from "@/components/home/board-section.vue";
import NoticeSection from "@/components/home/notice-section.vue";
import BoardList from "@/components/home/board-list.vue";
import NoticeList from "@/components/home/notice-list.vue";
import BoardEdit from "@/components/home/board-edit.vue";
import BoardWrite from "@/components/home/board-write.vue";

const routes = [
  {
    path: "/",
    name: "Welcome",
    component: WelcomePage,
    meta:{ loginRequired: false }
  },
  {
    path: "/home",
    name: "Home",
    component: HomePage,
    children: [
      {path: ":type", component: HomeSection, meta:{ loginRequired: true } },
      {path: "user/:id", component: UserSection , meta:{ loginRequired: true } },
      {path: "board/list", component: BoardList , meta:{ loginRequired: true } },
      {path: "board/:no", component: BoardSection , meta:{ loginRequired: true } },
      {path: "board/edit/:no", component: BoardEdit , meta:{ loginRequired: true } },
      {path: "notice/list", component: NoticeList , meta:{ loginRequired: false } },
      {path: "notice/:no", component: NoticeSection , meta:{ loginRequired: false } },
      {path: "board/write/:type", component: BoardWrite , meta:{ loginRequired: true } },
    ],
  },
  {
    path: "/game/:no",
    name: "Game",
    component: GamePage,
    meta:{ loginRequired: true }
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const isLoggedIn = function(){
  return localStorage.getItem('jwt') ? true : false
}

router.beforeEach((to, from, next) => {
  //console.log(to)

  if(to.meta.loginRequired){
    if(isLoggedIn()){
      //console.log("로그인 통과")
      next()
    }else{
      alert('로그인이 필요합니다!')
      next("/")
    }
  }else{
    //console.log("로그인 불필요")
    next()
  }
})

export default router;
