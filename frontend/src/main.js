import { createApp } from "vue";
import App from "./App.vue";
import router from "./common/lib/vue-router";
import ElementPlus from "./common/lib/element-plus";
import store from "./store";
import VueAxios from "./common/lib/axios";
import axios from "./common/lib/axios";

import "element-plus/lib/theme-chalk/index.css";

const app = createApp(App);
app.use(ElementPlus);
app.use(VueAxios, axios);
app.use(store);
app.use(router);

app.mount("#app");
