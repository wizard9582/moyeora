import { createRouter, createWebHistory } from "vue-router";

import WelcomePage from "@/views/welcome-page.vue";
import HomePage from "@/views/home-page.vue";
import GamePage from "@/views/game-page.vue";

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
  },
  {
    path: "/game",
    name: "Game",
    component: GamePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
export default router;
