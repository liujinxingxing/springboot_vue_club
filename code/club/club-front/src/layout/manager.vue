<!--
管理页面 * author:阳春白雪
-->
<template>
  <Main :collapse="isCollapse" @toggleLeft="toggleLeft">
    <div class="manager">
      <!-- 左侧面板层 -->
      <div
        class="left-position-box left-right-position-box"
        :class="{ 'not-show-left': !isCollapse, shrink: !isCollapse }"
        ref="leftPanel"
      >
        <div class="left-dash-panel dash-panel" ref="leftDashPanel"></div>
        <!-- <div class="arrow-icon" :class="{ shrink: !isCollapse }" @click="toggleLeft"></div> -->
        <div class="left-position-content">
          <!-- 菜单层 -->
          <el-menu
            :default-active="activeIndex"
            class="el-menu-vertical-demo"
            :background-color="this.$store.state.render.skin['--menu-bg-1']"
            text-color="var(--menu-text-color)"
            active-text-color="var(--menu-active-text-color)"
            :collapse-transition="false"
            :unique-opened="true"
            @open="openMenu"
            @select="menuSelect"
            :collapse="!isCollapse"
          >
            <div style="height: 60px;"></div>
            <template v-for="(v, key) in menus">
              <el-menu-item
                :index="JSON.stringify(v)"
                :key="key + v.type"
                v-if="v.type == 'single'"
              >
                <i :class="v.icon"></i>
                <span slot="title">{{ v.title }}</span>
              </el-menu-item>
              <el-submenu :key="key + v.type" :index="JSON.stringify(v)" v-if="v.type == 'child'">
                <template slot="title">
                  <i :class="v.icon"></i>
                  <span slot="title">{{ v.title }}</span>
                </template>
                <el-menu-item
                  v-for="(itm, keys) in v.child"
                  :index="JSON.stringify(itm)"
                  :key="keys"
                >
                  <i :class="itm.icon"></i>
                  <span slot="title">{{ itm.title }}</span>
                </el-menu-item>
              </el-submenu>
            </template>
          </el-menu>
        </div>
      </div>
      <!-- 内容层 -->
      <div class="view-main" :class="{ 'not-show-left-body': isCollapse }">
        <el-tabs
          class="el-tabs-self"
          v-model="currentFrame"
          type="border-card"
          closable
          @tab-remove="removeTab"
          v-if="frame.length > 0"
        >
          <el-tab-pane v-for="(item, index) in frame" :key="index" :name="item.title">
            <span slot="label">
              <i :class="item.icon"></i>
              {{item.title}}
            </span>
            <Users v-if="item.uri == 'users'"></Users>
            <Court v-else-if="item.uri == 'court'"></Court>
            <Appa v-else-if="item.uri == 'appa'"></Appa>
            <Detail v-else-if="item.uri == 'detail'"></Detail>
            <Notice v-else-if="item.uri == 'notice'"></Notice>
            <Discuss v-else-if="item.uri == 'discuss'"></Discuss>
            <User v-else-if="item.uri == 'user'"></User>
            <Home v-else-if="item.uri == 'welcome'"></Home>
            <Order v-else-if="item.uri == 'order'"></Order>
            <Orders v-else-if="item.uri == 'orders'"></Orders>
            <Orderhis v-else-if="item.uri == 'orderhis'"></Orderhis>
            <Log v-else-if="item.uri == 'loginlog'"></Log>
            <Iframer v-else :path="item.uri"></Iframer>
          </el-tab-pane>
        </el-tabs>
        <Home v-else></Home>
      </div>
    </div>
  </Main>
</template>

<script>
import Users from "page/users";
import Court from "page/court";
import Appa from "page/appa";
import User from "page/user";
import Notice from "page/notice";
import Discuss from "page/discuss";
import Home from "page/home";
import Main from "layout/index.vue";
import Iframer from "page/iframer";
import Orders from "page/orders.vue";
import Orderhis from "page/orderhis.vue";
import Order from "page/order.vue";
import Detail from "page/detail.vue";
import Log from "page/log.vue";
import { inter } from "api/interface";
import { admin } from "api/admin";
import Qs from "qs";
export default {
  data() {
    return {
      activeIndex: "",
      isCollapse: false,
      currentActive: "",
      menus: [],
      currentFrame: "",
      frame: []
    };
  },
  components: {
    Users,
    Notice,
    Discuss,
    Home,
    User,
    Iframer,
    Court,
    Appa,
    Order,
    Detail,
    Log,
    Orders,
    Orderhis,
    Main
  },
  created() {
    // 带路由参数
    this.activeIndex = this.$route.params.id;
  },
  mounted() {
    this.$bus.$on("open", this.open);
    this.$bus.$on("close", this.close);
    this.getPermission();
  },
  methods: {
    open(page, prop = undefined, data = undefined) {
      for (var i = 0; i < this.$store.state.render.hidden.length; i++) {
        if (this.$store.state.render.hidden[i].uri == page) {
          if (this.$store.state.render.hidden[i].fouces) {
            this.removeTab(this.$store.state.render.hidden[i].title);
            setTimeout(() => {
              this.addTab(this.$store.state.render.hidden[i], prop, data);
              this.$bus.$emit("closeLoad");
            }, 500);
            return;
          }
          this.addTab(this.$store.state.render.hidden[i], prop, data);
          return;
        }
      }
    },
    close(page) {
      this.removeTab(page || this.currentFrame);
    },
    removeTab(targetName) {
      let tabs = this.frame;
      let activeName = this.currentFrame;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.title === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.title;
            }
          }
        });
      }
      this.currentFrame = activeName;
      this.frame = tabs.filter(tab => tab.title !== targetName);
    },
    addTab(menu, prop = undefined, data = undefined) {
      var title = menu.title;
      for (var i = 0; i < this.frame.length; i++) {
        if (this.frame[i].title === title) {
          this.currentFrame = title;
          return;
        }
      }
      this.frame.push({
        title: title,
        icon: menu.icon,
        uri: menu.uri
      });
      this.currentFrame = title;
      if (prop != undefined || data != undefined) {
        setTimeout(() => {
          this.$bus.$emit("setData", menu.uri, prop, data);
        }, 600);
      }
    },
    toggleLeft() {
      this.isCollapse = !this.isCollapse;
      // this.$refs.view.resize();
    },
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
          this.calcRate(res.data);
        } else {
          this.msg(res);
        }
      });
    },
    calcRate(permitty) {
      for (var i = 0; i < this.$store.state.render.menus.length; i++) {
        var menu = this.$store.state.render.menus[i];
        if (permitty["page:club:" + menu.uri]) {
          if (menu.type == "single") {
            this.menus.push(menu);
          } else if (menu.type == "child") {
            var temp = {
              type: menu.type,
              title: menu.title,
              uri: menu.uri,
              icon: menu.icon,
              groupName: menu.groupName,
              child: []
            };
            for (var j = 0; j < menu.child.length; j++) {
              var child = menu.child[j];
              if (permitty["page:club:" + child.uri]) {
                if (child.type == "single") {
                  temp.child.push(child);
                }
              }
            }
            this.menus.push(temp);
          }
        }
      }
      for (var i = 0; i < this.$store.state.render.extends.length; i++) {
        this.menus.push(this.$store.state.render.extends[i]);
      }
      this.currentActive = JSON.stringify(this.menus[0]);
    },

    openMenu(key, keyPath) {
      let p = JSON.parse(this.currentActive);
      let j = JSON.parse(key);
      if (j.groupName != p.groupName && j.type == "child") {
        this.currentActive = JSON.stringify(j.child[0]);
      }
    },
    menuSelect(key, keyPath) {
      if (this.currentActive != key) {
        this.currentActive = key;
      }
      this.addTab(JSON.parse(key));
    }
  }
};
</script>

<style lang="less">
.manager {
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding-top: 60px;
  .view-main:not(.not-show-left-body) {
    padding-left: 65px !important;
  }
  .view-main {
    padding-left: 220px;
    height: 100%;
    .el-tabs-self > .el-tabs__header .el-tabs__item.is-active {
      background-color: var(--main-bg);
    }
    .el-tabs-self > .el-tabs__header {
      background-color: var(--tabs-header);
    }
    .el-tabs-self {
      .el-tabs__content {
        background-color: var(--main-bg);
      }
    }
    .el-tabs--border-card {
      height: 100%;
      .el-tabs__content {
        height: calc(~"100% - 40px");

        
        .el-tab-pane {
          height: 100%;
          overflow-y: auto;
        }
      }
    }
    .el-tabs--border-card > .el-tabs__content {
      padding: 0px !important;
    }
  }
  .el-menu {
    min-height: 100%;
    border-right: solid 0px #e6e6e6 !important;
    .el-menu-item-group__title,
    .el-menu-item,
    .el-submenu__title {
      font-size: 14px !important;
      height: 50px !important;
      line-height: 50px !important;
    }
    .el-menu-item.is-active {
      background-color: var(--menu-bg-2) !important;
      i{
        color: var(--menu-active-text-color);
      }
    }

    .el-menu-item i {
      font-size: 18px;
      margin-right: 6px;
      color: var(--menu-text-color);
    }
  }

  .el-menu::-webkit-scrollbar {
    width: 0px;
  }
  .el-menu:not(.el-menu--collapse) {
    width: 100%;
  }
  .el-menu--collapse {
    width: 65px !important;
    float: right;
  }
}

.left-right-position-box {
  .dash-panel {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    z-index: -99;
  }
  .arrow-icon {
    cursor: pointer;
    width: 12px;
    height: 40px;
    position: absolute;
    top: 50%;
    margin-top: -20px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
  }
  &.left-position-box {
    .arrow-icon {
      display: none;
      right: -12px;
      margin-top: -20px;
      background-image: url("../assets/icons/expand_arrow.png");
      &:hover {
        background-image: url("../assets/icons/expand_arrow_active.png");
      }
      &.shrink {
        background-image: url("../assets/icons/shrink_arrow.png");
        &:hover {
          background-image: url("../assets/icons/shrink_arrow_active.png");
        }
      }
    }
    &:hover {
      .arrow-icon {
        display: block;
      }
    }
  }
  &.right-position-box {
    .arrow-icon {
      display: none;
      left: -12px;
      background-image: url("../assets/icons/expand_arrow_r.png");
      &:hover {
        background-image: url("../assets/icons/expand_arrow_active_r.png");
      }
      &.shrink {
        background-image: url("../assets/icons/shrink_arrow_r.png");
        &:hover {
          background-image: url("../assets/icons/shrink_arrow_active_r.png");
        }
      }
    }
    &:hover {
      .arrow-icon {
        display: block;
      }
    }
  }
}
.left-position-box {
  width: 220px;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;

  // transition: left 0.2s linear;
  &.not-show-left {
    left: -155px;
  }
  .left-position-content {
    width: 100%;
    height: 100%;
    border-right: 0px solid #e6e6e6;
    position: relative;
  }
}
.el-tooltip__popper.is-dark {
    background: var(--menu-bg-1) !important;
    .popper__arrow{
      display: none;
    }
}
</style>
