import Vue from "vue";
import App from "./App.vue";

// 路由
import router from "./router";
import store from "./store";

// 全局样式
import "./styles/reset.css";
import "./styles/common.less";

// 字体
import "font-awesome/css/font-awesome.css";
import "./font/iconfont/iconfont.css";
import "./font/iconfont/iconfont.js";

// 全局函数
import util from "script/util";
Vue.use(util);


// ElementUI
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI, {
  size: "small"
});

Vue.config.productionTip = false;

// 事件总线
Vue.prototype.$bus = new Vue();

// 滚动加载
import InfiniteScroll from "vue-infinite-scroll";
Vue.use(InfiniteScroll);
Vue.directive("loadmore", {
  bind(el, binding) {
    const selectWrap = el.querySelector(".el-table__body-wrapper");
    selectWrap.addEventListener("scroll", function () {
      let sign = 20;
      const scrollDistance =
        this.scrollHeight - this.scrollTop - this.clientHeight;
      if (scrollDistance <= sign) {
        binding.value();
      }
    });
  }
});

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");

export default Vue;