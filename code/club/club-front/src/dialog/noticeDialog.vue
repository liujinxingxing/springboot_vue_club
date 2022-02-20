<!--
公告编辑对话框 * author:阳春白雪
-->
<template>
  <dialog-box
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form ref="reform" :model="mod" :rules="formRules" label-width="75px" label-suffix=":">
      <el-form-item :label="info.descripter[0]" prop="descripter">
        <el-input
          :maxlength="300"
          :placeholder="info.descripter[1]"
          v-model="mod.descripter"
          type="textarea"
          :rows="7"
        ></el-input>
      </el-form-item>

      <el-form-item :label="info.mode[0]">
        <el-radio class="radio" v-model="mod.mode" :label="0">{{info.mode[1]}}</el-radio>
        <el-radio class="radio" v-model="mod.mode" :label="1">{{info.mode[2]}}</el-radio>
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
      mod: {
        descripter: "",
        uuid: "",
        mode: 0
      },
      btnState: false
    };
  },
  computed: {
    info() {
      return this.$store.state.render.dialog.notice.info;
    },
    formRules() {
      return this.$store.state.render.dialog.notice.formRules;
    }
  },
  methods: {
    saveData() {
      this.$refs.reform.validate(validate => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      this.$fetch(inter.notice.save, this.mod, {
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
      if (mod) {
        this.mod = this.deepClone(mod);
        this.params.title = this.info.edit;
      }
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {
        descripter: "",
        uuid: "",
        mode: 0
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
