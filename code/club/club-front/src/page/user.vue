<!--
个人中心 * author:Daye Shing
-->
<template>
  <div class="user-main">
    <el-page-header @back="goBack" :content="info.page[0]" :title="info.page[1]" v-if="head" />

    <div class="user-main-body">
      <el-row class="user-info-item">
        <el-col :span="7" class="user-info user-info-left">
          <el-card shadow="hover">
            <div class="user-info-base">
              <el-upload
                :action="uploadUrl"
                :before-upload="beforeAvatarUpload"
                :limit="1000"
                :show-file-list="false"
                list-type="picture"
                :on-success="onIconUploadSuccess"
              >
                <el-avatar :size="88" :src="iconUrl" v-popover:popover>
                  <img :src="$store.state.render.info.avatar" />
                </el-avatar>
              </el-upload>

              <el-popover
                popper-class="avatar-popover"
                ref="popover"
                placement="right"
                trigger="hover"
              >{{info.avatar}}</el-popover>
              <div class="user-info-base-span">{{ $store.state.user.userName }}</div>
            </div>
            <template v-for="(item,key) in info.base">
              <p class="mb-3 ml-3" :key="key" v-if="$store.state.user[item.prop]">
                <i class="mr-3" :class="item.icon"></i>
                {{ $store.state.user[item.prop] }}
              </p>
            </template>
            <p class="ml-3">
              <i class="mr-3" :class="info.date"></i>
              {{$store.state.user.days}} {{info.days}}
            </p>

            <el-divider content-position="left">{{info.log[3]}}</el-divider>
            <el-timeline :reverse="false" class="ml-3">
              <el-timeline-item
                v-for="(activity, index) in logins"
                :key="index"
                :color="activity.quiter==null?'#409EFF':'#67C23A'"
                :icon="activity.quiter?'':'el-icon-more'"
                :timestamp="activity.startTime + (activity.quiter==null?info.log[0]:(activity.quiter?info.log[2]:info.log[1]))"
              ></el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
        <el-col :span="17" class="user-info">
          <el-card shadow="hover">
            <el-tabs v-model="activeName">
              <el-tab-pane name="info">
                <span slot="label">
                  <i :class="info.tabs[0].icon"></i>
                  {{info.tabs[0].title}}
                </span>
                <el-form
                  ref="reform"
                  :model="user"
                  :rules="formRules[0]"
                  label-width="75px"
                  label-suffix=":"
                  label-position="top"
                >
                  <el-form-item
                    :label="$store.state.render.dialog.user.info.account[0]"
                    prop="account"
                  >
                    <el-input
                      v-model="user.account"
                      :disabled="true"
                      :placeholder="$store.state.render.dialog.user.info.account[1]"
                    ></el-input>
                  </el-form-item>
                  <el-form-item
                    :label="$store.state.render.dialog.user.info.userName[0]"
                    prop="userName"
                  >
                    <el-input
                      :placeholder="$store.state.render.dialog.user.info.userName[1]"
                      v-model="user.userName"
                    ></el-input>
                  </el-form-item>
                  <el-form-item :label="$store.state.render.dialog.user.info.email[0]" prop="email">
                    <el-input
                      :placeholder="$store.state.render.dialog.user.info.email[1]"
                      v-model="user.email"
                    ></el-input>
                  </el-form-item>
                  <el-form-item :label="$store.state.render.dialog.user.info.gender.label">
                    <el-radio
                      class="radio"
                      v-model="user.gender"
                      :label="key"
                      v-for="(item,key) in $store.state.render.dialog.user.info.gender.items"
                      :key="key"
                    >{{item}}</el-radio>
                  </el-form-item>

                  <el-form-item :label="info.descripter[0]" prop="descripter">
                    <el-input
                      :maxlength="300"
                      :placeholder="info.descripter[1]"
                      v-model="user.descripter"
                      type="textarea"
                      :rows="7"
                    ></el-input>
                  </el-form-item>
                  <u-button
                    class="btn-item"
                    type="submit"
                    @click="saveUser"
                    :disabled="btnState"
                  >{{info.submit[0]}}</u-button>
                </el-form>
              </el-tab-pane>
              <el-tab-pane name="passwd">
                <span slot="label">
                  <i :class="info.tabs[1].icon"></i>
                  {{info.tabs[1].title}}
                </span>
                <el-form
                  ref="password"
                  :model="password"
                  :rules="formRules[1]"
                  label-width="75px"
                  label-suffix=":"
                  label-position="top"
                >
                  <el-form-item :label="info.old[0]" prop="old">
                    <el-input
                      :placeholder="info.old[1]"
                      v-model="password.old"
                      type="password"
                      auto-complete="off"
                      show-password
                    ></el-input>
                  </el-form-item>
                  <el-form-item :label="info.passwd[0]" prop="passwd">
                    <el-input
                      :placeholder="info.passwd[1]"
                      v-model="password.passwd"
                      type="password"
                      auto-complete="off"
                      show-password
                    ></el-input>
                  </el-form-item>

                  <el-form-item :label="info.comfirm[0]" prop="comfirm">
                    <el-input
                      :placeholder="info.comfirm[1]"
                      v-model="password.comfirm"
                      type="password"
                      auto-complete="off"
                      show-password
                    ></el-input>
                  </el-form-item>

                  <u-button
                    class="btn-item"
                    type="submit"
                    @click="savePasswd"
                    :disabled="btnState"
                  >{{info.submit[1]}}</u-button>
                </el-form>
              </el-tab-pane>

              <el-tab-pane :name="key" v-for="(val, key, index) in data" :key="key">
                <span slot="label">
                  <i :class="info.tabs[index+2].icon"></i>
                  {{info.tabs[index+2].title}}
                </span>
                <template v-if="val">
                  <template v-for="(order,dd) in val">
                    <Detail :val="order" :key="dd" :class="'el-description-'+key"></Detail>
                    <u-button
                      :key="dd"
                      v-if="key == 'subscribe' && $store.state.permitteds['btn:club:user:unsubscribe']"
                      @click="unsubscribe(order.uuid)"
                      class="btn-item"
                      type="submit"
                    >{{info.unsubscribe}}</u-button>
                  </template>
                </template>
                <el-empty :description="info.empty[index]" v-else></el-empty>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { inter } from "api/interface";
import Qs from "qs";
import Crypto from "crypto";
import uButton from "components/button/index";
import Detail from "components/detail";
export default {
  data() {
    return {
      logins: [],
      data: {
        // 预约
        subscribe: null,
        // 当前订单
        order: null,
        // 历史
        his: null
      },

      activeName: "info",
      btnState: false,
      user: {},
      uploadUrl: inter.io.upload,
      password: {
        old: "",
        passwd: "",
        comfirm: ""
      }
    };
  },
  props: {
    head: {
      type: Boolean,
      default() {
        return false;
      }
    }
  },
  components: { uButton, Detail },
  computed: {
    info() {
      return this.$store.state.render.user.info;
    },
    formRules() {
      var data = this.$store.state.render.user.formRules;
      data[0].email.push({
        validator: (rule, value, callback) => {
          var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; //匹配邮箱
          var isok = reg.test(value);
          isok = value ? isok : true;
          if (!isok) {
            callback(new Error(this.info.tip[5]));
          } else {
            callback();
          }
        },
        trigger: "blur"
      });
      data[1].comfirm.push({
        validator: (rule, value, callback) => {
          if (value !== this.password.passwd) {
            callback(new Error(this.info.tip[4]));
          } else {
            callback();
          }
        },
        trigger: "blur"
      });
      return data;
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
    this.getUserInfo();
    this.loadData();
    this.loadlog();
    this.loadDataHis();
  },
  methods: {
    goBack() {
      this.$emit("back");
    },
    unsubscribe(uuid) {
      this.$confirm(
        this.$store.state.render.common.unsubscribe[1],
        this.$store.state.render.common.unsubscribe[0],
        {
          confirmButtonText: this.$store.state.render.common.ok,
          cancelButtonText: this.$store.state.render.common.cancle,
          type: "warning"
        }
      ).then(() => {
        this.$fetch(
          inter.order.unsubscribe,
          Qs.stringify({
            uuid: uuid
          })
        ).then(res => {
          if (res.success) {
            this.$message({ type: "success", message: this.info.tip[6] });
            this.loadData(true);
          }
        });
      });
    },
    getData(data) {
      if (data.subscribe) {
        this.data.subscribe = data.subscribe;
      }
      if (data.order) {
        this.data.order = data.order;
      }
      if (data.his) {
        this.data.his = data.his;
      }
    },
    loadData(flag = false) {
      this.$fetch(
        inter.order.list,
        Qs.stringify({
          roll: true
        })
      ).then(res => {
        if (res.success) {
          if (flag) {
            this.data.subscribe = null;
            this.data.order = null;
          }
          this.getData(this.$formatter(res.data, false));
        }
      });
    },
    loadDataHis() {
      this.$fetch(
        inter.order.his,
        Qs.stringify({
          roll: true
        })
      ).then(res => {
        if (res.success) {
          this.getData(this.$formatter(res.data, true));
        }
      });
    },
    loadlog() {
      this.$fetch(
        inter.auth.list,
        Qs.stringify({
          rows: 5,
          roll: true
        })
      ).then(res => {
        if (res.success) {
          this.logins = res.data || [];
        }
      });
    },
    saveUser() {
      this.$refs.reform.validate(valid => {
        if (valid) {
          this.submit(this.user, true);
        } else {
          return false;
        }
      });
    },
    submit(user, flag) {
      this.btnState = true;
      this.$fetch(inter.user.modify, user, {
        headers: { "Content-Type": "application/json" }
      })
        .then(res => {
          this.btnState = false;
          if (res && res.success) {
            this.$store.commit("setUser", res.data);
            this.getUserInfo();
            if (flag) {
              this.$message({ type: "success", message: this.info.tip[0] });
            }
            return;
          }
          this.$message.error(this.info.tip[1] + res.message);
        })
        .catch(err => {
          this.btnState = false;
        });
    },
    savePasswd() {
      this.$refs.password.validate(valid => {
        if (valid) {
          this.btnState = true;
          var old = Crypto.createHash("sha256")
            .update(this.password.old.trim())
            .digest("hex");
          var passwd = Crypto.createHash("sha256")
            .update(this.password.passwd.trim())
            .digest("hex");
          this.$fetch(
            inter.user.passwd,
            Qs.stringify(
              { old: old, password: passwd },
              { arrayFormat: "repeat" }
            )
          )
            .then(res => {
              this.btnState = false;
              if (res && res.success) {
                this.$message({ type: "success", message: this.info.tip[0] });
                this.password = {
                  old: "",
                  passwd: "",
                  comfirm: ""
                };
                return;
              }
              this.$message.error(this.info.tip[1] + res.message);
            })
            .catch(err => {
              this.btnState = false;
            });
        } else {
          return false;
        }
      });
    },
    getUserInfo() {
      this.user = this.deepClone(this.$store.state.user);
    },
    /**
     * 控制图片格式
     */
    beforeAvatarUpload: function(file) {
      var $this = this;
      const isIMG =
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/gif" ||
        file.type === "image/svg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isIMG) {
        $this.$message.error(this.info.tip[2]);
      }
      if (!isLt2M) {
        $this.$message.error(this.info.tip[3]);
      }
      return isIMG && isLt2M;
    },
    /**在图标上传成功的时候*/
    onIconUploadSuccess: function(res, file, fileList) {
      if (res.success) {
        this.user.avatar = res.data;
        this.submit(this.user, false);
      } else {
        this.$message.error(res.message);
      }
    }
  }
};
</script>

<style lang="less">
.user-main {
  height: 100%;
  &-body {
    .el-descriptions:not(.el-description-subscribe) {
      padding-bottom: 8px;
      &:last-child {
        padding-bottom: 0px;
      }
    }
    .el-description-subscribe {
      padding-bottom: 6px;
      padding-top: 6px;
      &:first-child {
        padding-top: 0px;
      }
    }
    max-width: 1000px;
    margin: 0 auto;
    padding-top: 36px;

    .user-info-base {
      text-align: center;
      padding: 3px 6px;

      &-span {
        color: rgba(0, 0, 0, 0.85);
        margin-top: 1rem;
        line-height: 1.25rem;
        font-size: 1.25rem;
        font-weight: 500;
        margin-bottom: 0.25rem;
      }
    }
    .user-info-left {
      .el-card__body {
        padding: 12px !important;
      }
      padding-right: 8px;
      .mb-3 {
        margin-bottom: 0.75rem;
      }
      .mr-3 {
        margin-right: 0.35rem;
      }
      .ml-3 {
        margin-left: 0.75rem;
      }
    }
    .el-card {
      .el-card__header {
        height: 42px;
        line-height: 42px;
        padding: 0 10px;
        color: #333;
        font-size: 14px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
      }
      .el-card__body {
        padding: 6px;
      }
      .el-tabs {
        height: auto !important;
        .el-tabs__header {
          padding: 3px 6px 3px 6px !important;
          margin: 0px !important;
          border-bottom: 1px solid #ebeef5;
          .el-tabs__item {
            padding: 0 6px !important;
            height: 36px !important;
            line-height: 36px !important;
          }
        }
        .el-tabs__nav-wrap::after {
          background-color: #fff !important;
        }
        .el-tabs__content {
          padding: 6px !important;
          background-color: #fff !important;
          .el-tab-pane {
            height: 100%;
          }
        }
      }
    }
  }
}
</style>
