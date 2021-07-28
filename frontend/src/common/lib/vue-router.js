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

const routes = [
  {
    path: "/",
    name: "Welcome",
    component: WelcomePage,
  },
  {
    path: "/home",
    name: "Home",
    component: HomePage,
    children: [
      {path: "", component: HomeSection},
      {path: "user/:id", component: UserSection},
      {path: "board/list", component: BoardList},
      {path: "notice/list", component: NoticeList},
      {path: "board/:no", component: BoardSection},
      {path: "notice/:no", component: NoticeSection},
    ],
  },
  {
    path: "/game/:no",
    name: "Game",
    component: GamePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
export default router;
