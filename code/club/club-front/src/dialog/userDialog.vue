<!--
用户编辑对话框 * author:阳春白雪
-->
<template>
  <dialog-box
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form ref="reform" :model="mod" :rules="formRules" label-width="75px" label-suffix=":">
      <el-form-item :label="info.account[0]" prop="account">
        <el-input v-model="mod.account" :disabled="flag" :placeholder="info.account[1]"></el-input>
      </el-form-item>
      <el-form-item :label="info.userName[0]" prop="userName">
        <el-input :placeholder="info.userName[1]" v-model="mod.userName"></el-input>
      </el-form-item>

      <el-form-item :label="info.email[0]" prop="email">
        <el-input :placeholder="info.email[1]" v-model="mod.email"></el-input>
      </el-form-item>
      <el-form-item :label="info.gender.label">
        <el-radio
          class="radio"
          v-model="mod.gender"
          :label="key"
          v-for="(item,key) in info.gender.items"
          :key="key"
        >{{item}}</el-radio>
      </el-form-item>
      <el-form-item :label="info.leveles.label">
        <el-radio
          class="radio"
          v-model="mod.leveles"
          :label="item.k"
          v-for="(item,key) in info.leveles.items"
          :key="key"
        >{{item.v}}</el-radio>
      </el-form-item>
    </el-form>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button
          class="btn-item"
          type="cancle"
          @click="closeDialog"
        >{{$store.state.render.common.cancle}}</u-button>
        <u-button
          class="btn-item"
          type="submit"
          @click="saveData"
          :disabled="btnState"
        >{{$store.state.render.common.submit}}</u-button>
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "noticeDialog",
  components: {
    DialogBox,
    uButton
  },
  data() {
    return {
      params: {
        title: "",
        width: "500px",
        height: "380px",
        visible: false
      },
      index: null,
      flag: false,
      mod: {
        userName: "",
        account: "",
        email: "",
        gender: 0,
        leveles: 1
      },
      btnState: false
    };
  },
  computed: {
    info() {
      return this.$store.state.render.dialog.user.info;
    },
    formRules() {
      var data = this.$store.state.render.dialog.user.formRules;
      data.email.push({
        validator: (rule, value, callback) => {
          var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; //匹配邮箱
          var isok = reg.test(value);
          isok = value ? isok : true;
          if (!isok) {
            callback(new Error(this.info.error));
          } else {
            callback();
          }
        },
        trigger: "blur"
      });
      return data;
    }
  },
  methods: {
    saveData() {
      this.$refs.reform.validate(validate => {
        if (validate) {
          if (this.flag) {
            this.submit();
            return;
          }
          this.$fetch(
            inter.user.exsist,
            Qs.stringify(
              { account: this.mod.account },
              { arrayFormat: "repeat" }
            )
          ).then(res => {
            this.btnState = false;
            if (res && res.success) {
              if (res.data) {
                this.$message({ type: "error", message: this.info.exsist });
                return;
              }
              this.submit();
              return;
            }
            this.$message({ type: "error", message: res.message });
          });
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      var u = inter.user.save.replace(
        /\$\{type\}/g,
        this.flag ? "update" : "insert"
      );
      this.$fetch(u, this.mod, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.$emit("submit", res.data);
          this.closeDialog();
        } else {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    openDialog(mod) {
      this.params.title = this.info.add;
      this.flag = false;
      if (mod) {
        this.mod = this.deepClone(mod);
        this.params.title = this.info.edit;
        this.flag = true;
      }
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {
        userName: "",
        account: "",
        email: "",
        gender: 0,
        leveles: 1
      };
    }
  }
};
</script>

<style lang="less">
.el-dialog__body {
  padding: 4px 6px !important;
}
</style>
