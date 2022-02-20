<!--
订单结算对话框 * author:阳春白雪
-->
<template>
  <dialog-box
    class="back--dialog"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <Detail :val="order" v-for="(order,dd) in mod" :key="dd"></Detail>
    <el-form ref="reform" :model="val" :rules="formRules" label-width="45px" label-suffix=":">
      <el-form-item :label="info.descripter[0]" prop="descripter">
        <el-input
          :maxlength="300"
          :placeholder="info.descripter[1]"
          v-model="val.descripter"
          type="textarea"
          :rows="7"
        ></el-input>
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
        >{{info.submit}}</u-button>
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import Detail from "components/detail";
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  name: "noticeDialog",
  components: {
    DialogBox,
    uButton,
    Detail
  },
  data() {
    return {
      params: {
        title: "",
        width: "800px",
        height: "500px",
        visible: false
      },
      prop: ["uuid", "code", "positon", "price"],
      mod: {},
      val: {
        uuid: "",
        descripter: ""
      },
      btnState: false
    };
  },
  computed: {
    info() {
      return this.$store.state.render.dialog.back.info;
    },
    formRules() {
      return this.$store.state.render.dialog.back.formRules;
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
      this.$fetch(
        inter.order.back,
        Qs.stringify(this.val, { arrayFormat: "repeat" })
      ).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.$emit("submit", res.data);
          this.closeDialog();
          return;
        }
        this.$message({ type: "error", message: res.message });
      });
    },
    openDialog(mod) {
      this.params.title = this.info.title;
      this.mod = mod;
      this.val = {
        uuid: mod[0].uuid,
        descripter: this.info.descripter[2]
      };
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {};
      this.val = {
        uuid: "",
        descripter: ""
      };
    }
  }
};
</script>

<style lang="less">
.back--dialog {
  .el-dialog__body {
    padding: 30px !important;
  }
  .el-descriptions {
    padding-bottom: 30px;
  }
}
</style>
