<!--
一般客户和VIP客户 首页 * author:阳春白雪
-->
<template>
  <Main>
    <div class="home-main">
      <User v-if="page == 'user'" :head="true" @back="page = ''"></User>
      <Order v-else-if="page == 'order'" :head="true" @back="page = ''"></Order>
      <Home v-else></Home>
    </div>
  </Main>
</template>

<script>
import Main from "layout/index.vue";
import User from "page/user";
import Order from "page/order.vue";
import Home from "page/home";
import { inter } from "api/interface";
import { admin } from "api/admin";
import Qs from "qs";
export default {
  data() {
    return {
      page: ""
    };
  },
  components: { Home, User, Main, Order },
  mounted() {
    this.$bus.$on("open", this.open);
    this.$bus.$on("order", this.order);
    this.getPermission();
  },
  methods: {
    getPermission() {
      this.$fetch(
        inter.auth.hasPermissions,
        Qs.stringify(
          { module: inter.moduleNames, permissions: admin },
          { arrayFormat: "repeat" }
        ),
        { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
      ).then(res => {
        if (res && res.success) {
          this.$store.commit("setPermitteds", res.data);
        } else {
          this.msg(res);
        }
      });
    },
    open(page) {
      this.page = page;
      setTimeout(() => {
        this.$bus.$emit("closeLoad");
      }, 500);
    },
    order(page) {
      this.open(page);
    }
  }
};
</script>

<style lang="less">
.home-main {
  // padding: 18px;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  padding-top: 60px;
  background-color: var(--main-bg) !important;
}
</style>
