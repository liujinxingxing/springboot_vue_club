<!--
用户管理页面 * author:阳春白雪
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
      <el-table-column :label="info.avatar" width="70" slot="first" align="center">
        <template scope="scope">
          <el-avatar :size="46" :src="scope.row.avatar" class>
            <img :src="$store.state.render.info.avatar" />
          </el-avatar>
        </template>
      </el-table-column>

      <el-table-column
        :label="$store.state.render.common.opt"
        width="160"
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
              @click="initRow(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'init']"
            >{{info.init}}</u-button>
            <u-button
              shape="text"
              @click="deleteRow(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'delete']"
            >{{info.delete}}</u-button>
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
          @click="initRow()"
          style="color: #F56C6C;"
          v-if="$store.state.permitteds[secretkey + 'init']"
        >{{info.init}}</el-button>
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
    <UserDialog ref="dialog" @submit="reload"></UserDialog>
    <import-dialog :dialog="dialog" ref="importDialog" @saveSuccess="reload"></import-dialog>
    <form ref="exportForm" class="hidden-form" method="post">
      <input type="hidden" name="keyword" v-model="keyword" v-if="keyword != ''" />
      <input type="hidden" name="ids" :value="item" v-for="(item, index) in ids" :key="index" />
    </form>
  </div>
</template>
<script>
import ImportDialog from "dialog/importDialog";
import UserDialog from "dialog/userDialog.vue";
import SearchInput from "components/search-input.vue";
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";

import Qs from "qs";
export default {
  name: "user",
  components: { Datagrid, uButton, SearchInput, UserDialog, ImportDialog },
  data() {
    return {
      keyword: "",
      secretkey: "btn:club:users:",
      ids: []
    };
  },
  computed: {
    info() {
      return this.$store.state.render.users.info;
    },
    datagrid() {
      var data = this.$store.state.render.users.datagrid;
      data.url = inter.user.list;
      data.columns[3].formatter = this.formatter1;
      data.columns[4].formatter = this.formatter2;
      return data;
    },
    dialog() {
      var data = this.$store.state.render.users.dialog;
      data.saveDataUrl = inter.user.importUser;
      return data;
    }
  },
  methods: {
    formatter1(row) {
      switch (row.leveles) {
        case 3:
        case 2:
        case 1:
          return this.info.enum[0][row.leveles];
        default:
          return this.info.enum[0][0];
      }
    },
    formatter2(row) {
      switch (row.gender) {
        case 0:
        case 2:
        case 1:
          return this.info.enum[1][row.gender];
        default:
          return this.info.enum[1][3];
      }
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
      exportForm.setAttribute("action", inter.user.exportUser);
      this.$nextTick(() => {
        exportForm.submit();
      });
    },
    importRows() {
      this.$refs.importDialog.openDialog();
    },
    initRow(row) {
      var ids = [];
      if (row == undefined) {
        ids = this.getIds();
      } else {
        ids.push(row.account);
      }
      if (!ids || ids.length == 0) {
        this.$message({ message: this.info.initWarning, type: "warning" });
        return;
      }
      this.$confirm(this.info.initTitle, this.info.init, {
        confirmButtonText: this.$store.state.render.common.ok,
        cancelButtonText: this.$store.state.render.common.cancle,
        type: "warning"
      }).then(() => {
        this.$fetch(
          inter.user.init,
          Qs.stringify({ ids: ids }, { arrayFormat: "repeat" })
        )
          .then(res => {
            if (res && res.success) {
              this.$message.success(this.info.success);
              return;
            }
            this.$message.error(res.message);
          })
          .catch(err => {
            console.log(err);
          });
      });
    },
    getIds() {
      this.ids = [];
      let selectlist = this.$refs.datagrid.getSelection();
      if (selectlist.length > 0) {
        for (var i = 0; i < selectlist.length; i++) {
          this.ids.push(selectlist[i].account);
        }
      }
      return this.ids;
    },
    deleteRow(row) {
      var ids = [];
      if (row == undefined) {
        ids = this.getIds();
      } else {
        ids.push(row.account);
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
        inter.user.delete,
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
      this.$refs.dialog.openDialog(row);
    }
  },
  watch: {}
};
</script>
<style lang="less">
.user-view {
  height: 100%;
  padding:8px;
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
