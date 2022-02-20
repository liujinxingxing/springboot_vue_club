<!--
 首页导航栏  * author:阳春白雪
-->
<template>
  <div class="container-main">
    <div class="nvg-toolbar" :class="{'nvg-toolbar-bg':collapse == undefined}">
      <div
        class="nvg-toolbar-title"
        v-if="collapse != undefined"
        :class="{
      'collapse':collapse === false,
      'no-collapse':collapse === true
    }"
      >
        <div class="button">
          <i
            :class="{
              'fa fa-indent':collapse === false,
              'fa fa-outdent':collapse === true
            }"
            aria-hidden="true"
            @click="toggleLeft"
          ></i>
        </div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>{{render.title}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-breadcrumb separator-class="el-icon-arrow-right" v-else>
        <el-breadcrumb-item>{{render.title}}</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="more-Info">
        <i :class="render.icon[0]" @click="more = true"></i>
      </div>
      <div class="userInfoWrapper" v-popover:popover>
        <div class="user-img">
          <el-avatar :size="40" :src="iconUrl">
            <img :src="$store.state.render.info.avatar" />
          </el-avatar>
          <span class="user-name">{{ $store.state.user.userName }}</span>
        </div>
      </div>
      <el-popover
        popper-class="header-bottom-popover"
        ref="popover"
        placement="bottom"
        trigger="click"
      >
        <ul class="setting-user-drop">
          <li class="setting-user-item" @click="info()">
            <span class="li-item">
              {{ render.info }}
              <i :class="render.icon[2]"></i>
            </span>
          </li>

          <li class="setting-user-item" @click="signOut">
            <span class="li-item">
              {{ render.signout }}
              <i :class="render.icon[1]"></i>
            </span>
          </li>
        </ul>
      </el-popover>
    </div>
    <slot></slot>
    <el-drawer :visible.sync="more" :with-header="false" size="360px">
      <version v-if="more" :render="$store.state.render.version"></version>
    </el-drawer>
  </div>
</template>

<script>
import { inter } from "api/interface";
import Version from "page/version.vue";
export default {
  data() {
    return {
      type: "manager",
      more: false,
      loading: null
    };
  },
  props: {
    collapse: {
      type: Boolean,
      default() {
        return undefined;
      }
    }
  },
  components: { Version },
  computed: {
    render() {
      return this.$store.state.render.info;
    },
    iconUrl() {
      let icon = "";
      if (this.$store.state.user.avatar) {
        icon = this.$store.state.user.avatar;
      }
      return icon;
    }
  },
  mounted() {
    this.$bus.$on("openLoad", this.openLoad);
    this.$bus.$on("closeLoad", this.closeLoad);
  },
  methods: {
    info() {
      this.$bus.$emit("open", "user");
    },
    toggleLeft() {
      this.$emit("toggleLeft");
    },
    signOut() {
      this.$fetch(inter.auth.signOut).then(res => {
        if (res && res.success) {
          this.$router.push({ name: "login" });
        }
      });
    },
    /**
     * 加载动画
     */
    openLoad(tips) {
      if (this.loading == null) {
        this.loading = this.$loading({
          lock: true,
          text: tips,
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.3)"
        });
      }
    },
    closeLoad() {
      if (this.loading != null) {
        this.loading.close();
        this.loading = null;
      }
    }
  }
};
</script>

<style lang="less">
.el-loading-mask.is-fullscreen {
  .el-loading-spinner {
    i {
      color: #ffffff;
      font-size: 50px;
    }
    .el-loading-text {
      color: #ffffff;
      margin: 8px 0;
      font-size: 22px;
    }
  }
}
.header-bottom-popover {
  padding: 10px !important;
}
.header-bottom-popover[x-placement^="bottom"] {
  margin-top: 5px;
  text-align: center;
  min-width: 100px;
  .setting-user-item {
    cursor: pointer;
    &:hover {
      color: rgb(35, 183, 229);
    }
  }
}
.container-main {
  height: 100%;
  min-width: 1000px;

  .el-page-header {
    padding: 10px 10%;
    background-color: #fff;
  }
  .nvg-toolbar-bg {
    background-color: var(--tabs-header);
    border-bottom: 1px solid #e0e0e0;
    .el-breadcrumb {padding-left: 140px;}
  }
  .nvg-toolbar {
    width: 100%;
    height: 60px;
    line-height: 59px;

    position: absolute;
    z-index: 9;

    .nvg-toolbar-title {
      // width: 500px;
      float: left;
      .button {
        float: left;
        font-size: 20px !important;
        line-height: 59px !important;
        padding: 0px 15px;
        cursor: pointer;
      }
      .button:hover {
        color: rgb(35, 183, 229);
      }
    }
    .collapse {
      margin-left: 65px;
    }
    .no-collapse {
      margin-left: 220px;
    }
    .el-breadcrumb {
      float: left;
      font-size: 16px;
      line-height: 59px !important;
    }
    .more-Info {
      padding: 2px 15px 0px 0px;
      float: right;
      i {
        cursor: pointer;
        font-size: 16px;
        padding: 0px 6px;
        color: #969697;
        &:hover {
          color: rgb(35, 183, 229);
        }
      }
    }
    .userInfoWrapper {
      float: right;
      height: 59px;
      line-height: 40px;
      padding: 10px 0px;
      margin-left: 10px;
      font-size: 12px;
      cursor: pointer;
      .user-img {
        height: 40px;
      }
      .user-name {
        padding: 0px 10px;
        float: right;
        color: #969697;
        font-weight: bold;
        &:hover {
          color: rgb(35, 183, 229);
        }
      }
      .user-name i {
        font-size: 12px;
        margin-left: 3px;
      }
    }
    .notice-message {
      font-size: 16px;
      padding: 13px;
      line-height: 20px;
      float: right;
      color: #969697;
      .el-badge__content.is-fixed.is-dot {
        right: 3px !important;
      }
      i:hover {
        color: rgb(35, 183, 229);
      }
    }
  }
}
</style>
