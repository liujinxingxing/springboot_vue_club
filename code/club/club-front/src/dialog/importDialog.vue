<!--
封装导入数据对话框 * author:阳春白雪
-->
<template>
  <dialog-box
    class="importDialog"
    :params="params"
    :title="params.title"
    :visible="params.visible"
    @before-close="closeDialog"
  >
    <vue-scroll :ops="scrollOps">
      <!-- 步骤标题 -->
      <div class="step-progress">
        <div
          class="progress-item"
          :class="{ active: currentStep >= index + 1 }"
          v-for="(item, index) in info.titles"
          :key="index"
        >
          <div class="square">
            <span v-if="currentStep <= index + 1">{{ index + 1 }}</span>
            <i v-else class="el-icon-check"></i>
          </div>
          <p class="step-name">{{ item }}</p>
        </div>
      </div>

      <!-- 文件上传，步骤一 -->
      <div class="upload-box" v-show="currentStep === 1">
        <el-upload
          class="upload-demo"
          ref="fileUpload"
          :action="importUrl"
          :show-file-list="false"
          :limit="1"
          :before-upload="beforeAvatarUpload"
          :on-success="uploadSuccess"
          :file-list="fileList"
        >
          <div class="el-upload__text">{{ info.upload.selectFile }}</div>
        </el-upload>
        <ul class="upload-tips">
          <li v-for="(item, index) in info.upload.tips" :key="index">{{ index + 1 }} . {{ item }}</li>
        </ul>
        <div class="upload-template">
          <span @click="downloadTemplate">
            <i class="fa fa-cloud-download" aria-hidden="true"></i>
            {{ info.template }}
          </span>
          <form ref="downloadForm" class="hidden-form" method="post" :action="templateUrl">
            <input type="hidden" name="name" v-model="dialog.templateName" />
          </form>
        </div>
      </div>

      <transition @enter="resizeTable">
        <div class="preview-box" v-show="currentStep === 2">
          <datagrid :datagrid="datagrid" :data="uploadData" ref="uploadDatagrid">
            <!-- 显示数据 -->
            <el-table-column
              v-for="item in dialog.columns"
              :key="item.proped"
              :class-name="item.className"
              :label-class-name="item.labelClassName"
              :width="item.width"
              :min-width="item.minWidth"
              :prop="item.proped"
              :label="item.label"
              :sortable="item.sortable"
              :header-align="item.headerAlign"
              :align="item.align == undefined ? 'center' : item.align"
              :formatter="item.formatter"
              :fixed="item.fixed"
            ></el-table-column>
            <div slot="footbar" class="select-tips">{{ info.selectTips }}</div>
          </datagrid>
        </div>
      </transition>

      <div class="import-box" v-show="currentStep === 3">
        <i
          class="icon-tips iconfont"
          :class="{
            'icon-bda-sys-wait': importStatus == 1,
            'icon-bda-sys-success': importStatus == 2,
            'icon-bda-sys-error': importStatus == 3,
          }"
        ></i>
        <p class="import-tips">
          <template v-if="importStatus == 1">{{ info.importTips[0] }}</template>
          <template v-else-if="importStatus == 2">
            {{
            info.importTips[1]
            }}
          </template>
          <template v-else-if="importStatus == 3">
            {{
            info.importTips[2]
            }}
          </template>
        </p>
        <el-progress :stroke-width="8" :percentage="currentPercentage"></el-progress>
      </div>
    </vue-scroll>

    <!-- 脚部按钮 -->
    <div slot="footer">
      <div class="push-right">
        <u-button
          class="btn-item"
          :disabled="importStatus == 1"
          type="cancle"
          v-show="currentStep === 1"
          @click="closeDialog"
        >{{$store.state.render.common.cancle}}</u-button>
        <u-button
          class="btn-item"
          :disabled="importStatus == 1"
          type="submit"
          v-show="currentStep > 1"
          @click="jumpPrevStep"
        >{{info.prefix}}</u-button>
        <u-button
          class="btn-item"
          :disabled="importStatus == 1"
          type="submit"
          v-show="currentStep < 3"
          @click="jumpNextStep"
        >{{info.next}}</u-button>
        <u-button
          class="btn-item"
          :disabled="importStatus == 1"
          type="submit"
          v-show="currentStep === 3"
          @click="saveSuccess"
        >{{info.finish}}</u-button>
      </div>
    </div>
  </dialog-box>
</template>

<script type="es6">
import Datagrid from "components/table/datagrid.vue";
import DialogBox from "components/dialog/dialogbox";
import uButton from "components/button/index";
import { inter } from "api/interface";
import Qs from "qs";
export default {
  components: {
    Datagrid,
    DialogBox,
    uButton
  },
  data() {
    return {
      columns: [],
      importUrl: inter.io.import,
      templateUrl: inter.io.template,
      params: {
        title: "",
        width: "800px",
        height: "500px",
        visible: false,
        isScroll: false
      },
      datagrid: {
        selection: {
          showSelection: true
        },
        pagination: {
          showPagination: false,
          autoRoll: false,
          showMore: true
        },
        creatScrollBar: true
      },
      scrollOps: {
        vuescroll: {},
        scrollPanel: {
          scrollingX: false
        },
        rail: {},
        bar: {
          background: "#cccccc",
          opacity: 0.9,
          keepShow: true
        }
      },
      fileList: [],
      currentStep: 1,
      uploadData: [],
      importStatus: 0, //1导入过程中，2导入成功，3导入失败
      currentPercentage: 0
    };
  },
  computed: {
    info() {
      return this.$store.state.render.dialog.imp;
    }
  },
  props: {
    dialog: {
      type: Object,
      default() {
        return {
          saveDataUrl: "",
          templateName: "",
          title: "",
          columns: []
        };
      }
    }
  },
  methods: {
    downloadTemplate() {
      this.$refs.downloadForm.submit();
    },
    saveSuccess() {
      this.$emit("saveSuccess");
      this.closeDialog();
    },
    resizeTable() {
      this.$refs.uploadDatagrid.handleResize();
    },
    beforeAvatarUpload(file) {
      let arr = file.name.split(".");
      let isExcel =
        arr[arr.length - 1] === "xls" || arr[arr.length - 1] === "xlsx";
      if (!isExcel) {
        this.$message.error(this.info.notfile);
      }
      return isExcel;
    },
    /** 上传文件后的回调 */
    uploadSuccess: function(res) {
      this.$refs.fileUpload.clearFiles();
      if (res && res.success) {
        if (res.data) {
          this.uploadData = res.data;
          this.$message({ type: "success", message: this.info.success });
          this.currentStep = 2;
        }
      } else {
        this.$message.error(this.info.fail);
      }
    },
    openDialog() {
      this.params.visible = true;
      this.$nextTick(() => {
        this.params.title = this.dialog.title;
      });
    },
    closeDialog() {
      this.clearData();
      this.params.visible = false;
    },
    clearData() {
      this.currentStep = 1;
      this.importStatus = 0;
      this.currentPercentage = 0;
      this.uploadData = [];
    },
    jumpPrevStep() {
      this.currentStep--;
    },
    jumpNextStep() {
      if (this.currentStep == 1) {
        if (this.uploadData.length > 0) {
          this.currentStep++;
        } else {
          this.$message({ type: "warning", message: this.info.beford });
        }
      } else {
        let selectlist = this.$refs.uploadDatagrid.getSelection();
        if (selectlist.length > 0) {
          this.currentStep++;
          this.importStatus = 1;
          this.currentPercentage = 0;
          this.submitImport(selectlist);
        } else {
          this.$message({ type: "warning", message: this.info.required });
        }
      }
    },
    formatData(list) {
      let map = {};
      for (var i = 0; i < this.dialog.columns.length; i++) {
        map[this.dialog.columns[i].proped] = this.dialog.columns[i];
      }
      let data = [];
      for (var i = 0; i < list.length; i++) {
        var obj = list[i];
        var tmp = {};
        for (var key in obj) {
          var prop = map[key];
          if (prop) {
            var value = undefined;
            if (prop.variable && prop.variable == "int") {
              value = parseInt(obj[key]);
              value = !value ? 0 : value;
            } else if (prop.variable && prop.variable == "float") {
              value = parseFloat(obj[key]);
              value = !value ? 0 : value;
            } else if (prop.variable && prop.variable == "boolean") {
              value = obj[key].toLowerCase() == "true" ? true : false;
            } else if (prop.variable && prop.variable == "enum") {
              value = null;
              if (prop.enum) {
                var t = prop.enum[obj[key]];
                if (t) {
                  value = t;
                }
              } else {
                value = obj[key];
              }
            } else {
              value = obj[key];
            }
            tmp[prop.prop] = value;
          }
        }
        data.push(tmp);
      }
      console.log(data);
      return data;
    },
    submitImport(list) {
      this.$fetch(this.dialog.saveDataUrl, this.formatData(list), {
        headers: { "Content-Type": "application/json" }
      })
        .then(res => {
          if (res && res.success) {
            this.currentPercentage = 100;
            setTimeout(() => {
              this.$nextTick(() => {
                this.importStatus = 2;
                this.$message({ type: "success", message: this.info.put });
              });
            }, 500);
          } else {
            this.currentPercentage = 70;
            setTimeout(() => {
              this.$nextTick(() => {
                this.importStatus = 3;
                this.info.importTips[2] =
                  this.info.error[0] + res.message + this.info.error[1];
                // this.messages(this, "error", res.code);
              });
            }, 500);
          }
        })
        .catch(err => {
          this.currentPercentage = 70;
          setTimeout(() => {
            this.$nextTick(() => {
              this.importStatus = 3;
            });
          }, 500);
        });
    }
  }
};
</script>

<style lang="less">
.importDialog {
  .step-progress {
    padding: 25px 0px 30px 0px;
    text-align: center;
    .progress-item {
      display: inline-block;
      font-size: 0;
      margin-left: 160px;
      position: relative;
      .square {
        background-color: #ababab;
        width: 24px;
        height: 24px;
        margin: 0 auto;
        border-radius: 2px;
        span {
          font-size: 14px;
          line-height: 24px;
          font-weight: bold;
          color: #f2f2f2;
        }
        i {
          font-size: 14px;
          color: #f2f2f2;
          font-weight: bold;
          line-height: 24px;
        }
      }
      .step-name {
        color: #333333;
        font-weight: bold;
        font-size: 12px;
        margin-top: 10px;
        max-width: 48px;
        white-space: nowrap;
        position: relative;
      }
      &.active {
        .square {
          background-color: #1177e2;
          &:before {
            border-color: #1177e2;
          }
        }
      }
      &:before {
        content: "";
        position: absolute;
        left: -162px;
        top: 12px;
        width: 160px;
        height: 0;
        border-bottom: 1px dashed #cbcbcb;
      }
      &:first-child {
        margin-left: 0;
        // .step-name {
        //   left: -11px;
        // }
        &:before {
          display: none;
        }
      }
    }
  }
  .upload-box {
    padding: 0 50px 0px;
    position: relative;
    .upload-demo {
      background-color: #fafafa;
      border: 1px solid #dddddd;
      border-radius: 2px;
      height: 150px;
      position: relative;
      .el-upload__text {
        position: absolute;
        left: 50%;
        top: 50%;
        margin-left: -100px;
        margin-top: -25px;
        width: 200px;
        height: 50px;
        line-height: 48px;
        border-radius: 2px;
        border: 1px dashed #a6a6a6;
        text-decoration: underline;
        font-size: 12px;
        color: #333333;
        font-weight: bold;
      }
    }
    .upload-tips {
      padding-top: 15px;
      position: relative;
      li {
        font-size: 12px;
        color: #898989;
        line-height: 20px;
      }
    }
    .upload-template {
      position: absolute;
      right: 50px;
      top: 150px;
      width: 100%;
      font-size: 12px;
      text-align: right;
      color: rgb(115, 173, 85);
      padding: 5px 5px 0px 0px;
      span {
        cursor: pointer;
      }
    }
  }
  .preview-box {
    padding: 0 50px 0px;
    .datagrid {
      height: 252px;
    }
    .select-tips {
      color: #e6a1a1 !important;
      font-size: 14px;
    }
  }
  .import-box {
    padding: 65px 132px 10px;
    text-align: center;
    .icon-tips {
      font-size: 70px;
      color: #1177e2;
    }
    .import-tips {
      color: #333333;
      font-weight: bold;
      font-size: 14px;
      padding: 15px 0;
    }
  }
}
</style>
