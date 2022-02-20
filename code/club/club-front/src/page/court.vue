<!--
场地页面 * author:阳春白雪
-->
<template>
  <div class="user-view">
    <datagrid :datagrid="datagrid" ref="datagrid">
      <div slot="toolbar">
        <div class="toolbar-btn push-left">
          <u-button
            type="build"
            @click="addRow()"
            v-if="$store.state.permitteds[secretkey + 'add']"
          >{{info.add}}</u-button>
        </div>
        <div class="search push-right">
          <search-input v-model="keyword" @search="reload()" @clear="reload()"></search-input>
        </div>
      </div>

      <el-table-column
        :label="$store.state.render.common.opt"
        width="170"
        slot="last"
        align="center"
      >
        <template scope="scope">
          <div class="table-btn-group">
            <u-button
              shape="text"
              @click="addRow(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'edit']"
            >{{info.edit}}</u-button>
            <u-button
              shape="text"
              @click="deleteRow(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'delete'] && scope.row.entry == null"
            >{{info.delete}}</u-button>
            <u-button
              shape="text"
              @click="back(scope.row,true)"
              v-if="$store.state.permitteds[secretkey + 'back'] && scope.row.entry == 1"
            >{{info.back}}</u-button>
            <u-button
              shape="text"
              @click="unsubscribe(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'unsubscribe'] && scope.row.entry == 0"
            >{{info.unsubscribe}}</u-button>
            <u-button
              shape="text"
              @click="lend(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'lend'] && scope.row.entry == null"
            >{{info.lend}}</u-button>
            <u-button
              shape="text"
              @click="detail(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'detail'] && scope.row.entry != null"
            >{{info.detail}}</u-button>
          </div>
        </template>
      </el-table-column>
      <div slot="footbar">
        <el-button
          type="text"
          size="mini"
          @click="deleteRow()"
          style="color: #F56C6C;"
          v-if="$store.state.permitteds[secretkey + 'delete']"
        >{{$store.state.render.common.delete}}</el-button>
        <el-button
          type="text"
          size="mini"
          @click="importRows()"
          style="color: #F56C6C;"
          v-if="$store.state.permitteds[secretkey + 'import']"
        >{{$store.state.render.common.import}}</el-button>
        <el-button
          type="text"
          size="mini"
          @click="exportRows()"
          style="color: #F56C6C;"
          v-if="$store.state.permitteds[secretkey + 'export']"
        >{{$store.state.render.common.export}}</el-button>
      </div>
    </datagrid>
    <BackDialog ref="backDialog" @submit="reload"></BackDialog>
    <ContentDialog ref="dialog" @submit="reload"></ContentDialog>
    <import-dialog :dialog="dialog" ref="importDialog" @saveSuccess="reload"></import-dialog>
    <form ref="exportForm" class="hidden-form" method="post">
      <input type="hidden" name="keyword" v-model="keyword" v-if="keyword != ''" />
      <input type="hidden" name="ids" :value="item" v-for="(item, index) in ids" :key="index" />
    </form>
  </div>
</template>
<script>
import ImportDialog from "dialog/importDialog";
import ContentDialog from "dialog/contentDialog.vue";
import BackDialog from "dialog/backDialog.vue";
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";
import SearchInput from "components/search-input.vue";
import Qs from "qs";
export default {
  name: "court",
  components: {
    Datagrid,
    uButton,
    SearchInput,
    ContentDialog,
    ImportDialog,
    BackDialog
  },
  data() {
    return {
      keyword: "",
      secretkey: "btn:club:court:",
      ids: []
    };
  },
  computed: {
    info() {
      return this.$store.state.render.court.info;
    },
    datagrid() {
      var data = this.$store.state.render.court.datagrid;
      data.url = inter.court.list;
      data.columns[4].formatter = this.formatter;
      return data;
    },
    dialog() {
      var data = this.$store.state.render.court.dialog;
      data.saveDataUrl = inter.court.importCourt;
      return data;
    }
  },
  methods: {
    formatter(row) {
      switch (row.entry) {
        case 0:
        case 1:
          return this.info.enum[row.entry];
        default:
          return this.info.enum[2];
      }
    },
    // 打开结算页面
    back(row, flag = true) {
      this.$fetch(
        inter.order.list,
        Qs.stringify(
          {
            uuid: row.uuid,
            entry: row.entry
          },
          { arrayFormat: "repeat" }
        )
      )
        .then(res => {
          if (res.success) {
            if (flag) {
              this.$refs.backDialog.openDialog(
                this.$formatter(res.data, false).order
              );
              return;
            }
            this.$bus.$emit("open", "detail", false, res.data);
            return;
          }
          if (!flag) {
            this.$bus.$emit("closeLoad");
          }
          this.$message({
            type: "error",
            message: res.message
          });
        })
        .catch(err => {
          if (!flag) {
            this.$bus.$emit("closeLoad");
          }
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    },
    // 详细，已经预定的情况
    detail(row) {
      this.$bus.$emit("openLoad", this.$store.state.render.common.valid);
      if (row.entry == 1) {
        
        this.back(row, false);
        return;
      }
      this.$fetch(
        inter.order.list,
        Qs.stringify(
          {
            uuid: row.uuid,
            entry: row.entry,
            detail:true
          },
          { arrayFormat: "repeat" }
        )
      )
        .then(res => {
          if (res.success) {
            this.$store.commit("setOrder", {
              data: res.data,
              prefixId: res.info,
              flag: true,
              detail:true
            });
            this.$bus.$emit("open", "order");
            return;
          }
          this.$bus.$emit("closeLoad");
          this.$message({
            type: "error",
            message: res.message
          });
        })
        .catch(err => {
          this.$bus.$emit("closeLoad");
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    },
    // 出借或者预定
    lend(row) {
      this.$bus.$emit("openLoad", this.$store.state.render.common.valid);
      this.$fetch(
        inter.order.court,
        Qs.stringify({
          code: row.code
        })
      )
        .then(res => {
          if (res.success) {
            // 访问，超时3分钟
            row.prefixId = res.data;
            row.flag = true;
            this.$store.commit("setOrder", {
              data: [row],
              prefixId: res.data,
              flag: true
            });
            this.$bus.$emit("open", "order");
            return;
          }
          this.$bus.$emit("closeLoad");
          this.$message({
            type: "error",
            message: res.message
          });
        })
        .catch(err => {
          this.$bus.$emit("closeLoad");
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    },
    // 取消预定
    unsubscribe(row) {
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
            uuid: row.uuid
          })
        )
          .then(res => {
            if (res.success) {
              this.reload();
              return;
            }
            this.$message({
              type: "error",
              message: res.message
            });
          })
          .catch(err => {
            this.$message({
              type: "error",
              message: this.render.version.error
            });
          });
      });
    },
    reload() {
      this.$refs.datagrid.loadData(
        {
          keyword: this.keyword
        },
        true
      );
    },
    exportRows() {
      this.getIds();
      const exportForm = this.$refs.exportForm;
      exportForm.setAttribute("action", inter.court.exportCourt);
      this.$nextTick(() => {
        exportForm.submit();
      });
    },
    importRows() {
      this.$refs.importDialog.openDialog();
    },
    getIds(flag = true) {
      this.ids = [];
      let selectlist = this.$refs.datagrid.getSelection();
      if (selectlist.length > 0) {
        for (var i = 0; i < selectlist.length; i++) {
          if (flag) {
            this.ids.push(selectlist[i].code);
            continue;
          }
          if (selectlist[i].entry == null) {
            this.ids.push(selectlist[i].code);
          }
        }
      }
      return this.ids;
    },
    deleteRow(row) {
      var ids = [];
      if (row == undefined) {
        ids = this.getIds(false);
      } else {
        ids.push(row.code);
      }
      if (!ids || ids.length == 0) {
        this.$message({
          message: this.$store.state.render.common.deleteWarning,
          type: "warning"
        });
        return;
      }
      this.$confirm(
        this.$store.state.render.common.deleteTitle,
        this.$store.state.render.common.delete,
        {
          confirmButtonText: this.$store.state.render.common.ok,
          cancelButtonText: this.$store.state.render.common.cancle,
          type: "warning"
        }
      ).then(() => {
        this.remove(ids);
      });
    },
    remove(ids) {
      this.$fetch(
        inter.court.delete,
        Qs.stringify({ ids: ids }, { arrayFormat: "repeat" })
      )
        .then(res => {
          if (res && res.success) {
            this.reload();
            return;
          }
          this.$message.error(res.message);
        })
        .catch(err => {
          console.log(err);
        });
    },
    addRow(row) {
      this.$refs.dialog.openDialog(row, 1);
    }
  },
  watch: {}
};
</script>
<style lang="less">
.user-view {
  height: 100%;
  padding: 8px;
  .toolbar-btn {
    padding-top: 4px;
    .u-button {
      margin-right: 5px;
    }
  }
  .u-search-input {
    width: 300px;
    .el-input--small,
    .el-input--small .el-input__inner {
      height: 32px !important;
      line-height: 32px !important;
    }
    .el-input__icon {
      line-height: 36px !important;
    }
    .el-input__inner {
      padding-right: 65px !important;
    }
    .el-icon-search {
      right: 5px;
      font-weight: 600;
    }
    .clear {
      right: 35px;
      font-weight: 600;
    }
    input {
      background-color: #eaecf1;
      border-radius: 15px;
      border: 0;
      outline: 0;
    }
  }
}
</style>
