<!--
初始化配置 用于页面加载或刷新时仅仅加载一次配置文件 并能跳转到所需路由 * author:阳春白雪
-->
<template>
  <div />
</template>

<script>
import { inter } from "api/interface";
export default {
  data() {
    return {
      result: 0,
      page:undefined,
    };
  },
  mounted() {
    this.page = this.loadRender();
    this.getUserInfo();
  },
  methods: {
    endsWith(str, appoint) {
      str = str.toLowerCase(); //不区分大小写：全部转为小写后进行判断
      var start = str.length - appoint.length; //相差长度=字符串长度-特定字符长度
      var char = str.substr(start, appoint.length); //将相差长度作为开始下标，特定字符长度为截取长度
      if (char == appoint) {
        //两者相同，则代表验证通过
        return true;
      }
      return false;
    },
    init(page) {
      if (this.result > 1) {
        if (this.$store.state.user.level > 2) {
          this.$router.push({ name: page || "manager" });
          return;
        }
        if (this.$store.state.user.level > 0 && page != 'login') {
          this.$router.push({ name: "club" });
          return;
        }
        this.$router.push({ name: "login" });
      }
    },
    getUserInfo() {
      this.$fetch(inter.auth.getCurrentUser).then(res => {
        if (res && res.success) {
          this.result++;
          this.$store.commit("setUser", res.data);
          this.init(this.page);
        }
      });
    },
    loadRender() {
      var page = undefined;
      if(this.endsWith(window.location.hash,"/club")){
        page = 'club';
      }
      if(this.endsWith(window.location.hash,"/manager")){
        page = 'manager';
      }
      if(this.endsWith(window.location.hash,"/login")){
        page = 'login';
      }
      this.$getch(
        "lang/zh/club.json",
        "get",
        {},
        { Pragma: "no-cache", "Cache-Control": "no-cache" }
      )
        .then(res => {
          this.$store.commit("setRender", res);
          if (res.skin) {
            let root = document.querySelectorAll("body")[0];
            for (var key in res.skin) {
              root.style.setProperty(key, res.skin[key]);
            }
          }
          this.$nextTick(() => {
            this.result++;
            this.init(page);
          });
        })
        .catch(err => {});
        return page;
    }
  }
};
</script>

<style lang="less">
</style>
