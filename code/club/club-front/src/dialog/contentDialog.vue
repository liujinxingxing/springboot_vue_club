<!--
场地新建编辑、器材新建编辑 对话框 * author:阳春白雪
-->
<template>
  <dialog-box
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <el-form ref="reform" :model="mod" :rules="formRules" label-width="75px" label-suffix=":">
      <el-form-item :label="info.code[mod.mode][0]" prop="code">
        <el-input v-model="mod.code" :disabled="flag" :placeholder="info.code[mod.mode][1]"></el-input>
      </el-form-item>
      <el-form-item :label="info.positon[mod.mode][0]" prop="positon">
        <el-input :placeholder="info.positon[mod.mode][1]" v-model="mod.positon"></el-input>
      </el-form-item>

      <el-form-item :label="info.price[0]" prop="price">
        <el-input :placeholder="info.price[1]" v-model="mod.price"></el-input>
      </el-form-item>

      <el-form-item :label="info.descripter[0]" prop="descripter">
        <el-input
          :maxlength="300"
          :placeholder="info.descripter[1]"
          v-model="mod.descripter"
          type="textarea"
          :rows="4"
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
        code: "",
        positon: "",
        descripter: "",
        price: 0,
        mode: 1
      },
      btnState: false
    };
  },
  computed: {
    info() {
      return this.$store.state.render.dialog.content.info;
    },
    formRules() {
      return this.$store.state.render.dialog.content.formRules;
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
            this.mod.mode == 0 ? inter.appa.exsist : inter.court.exsist,
            Qs.stringify({ code: this.mod.code }, { arrayFormat: "repeat" })
          ).then(res => {
            this.btnState = false;
            if (res && res.success) {
              if (res.data) {
                this.$message({
                  type: "error",
                  message: this.info.exsist[this.mod.mode]
                });
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
      this.$fetch(
        this.mod.mode == 0 ? inter.appa.save : inter.court.save,
        this.mod,
        {
          headers: { "Content-Type": "application/json" }
        }
      ).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.$emit("submit", res.data);
          this.closeDialog();
        } else {
          this.$message({ type: "error", message: res.message });
        }
      });
    },
    openDialog(mod, type) {
      this.params.title = this.info.add;
      this.flag = false;
      if (mod) {
        this.mod = this.deepClone(mod);
        this.params.title = this.info.edit;
        this.flag = true;
      }
      this.mod.mode = type;
      this.params.title += this.info.type[type];
      this.params.visible = true;
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.mod = {
        code: "",
        positon: "",
        descripter: "",
        price: 0,
        mode: 1
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
