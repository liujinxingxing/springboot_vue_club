<!--
公告管理页面 * author:阳春白雪
-->
<template>
  <div class="notice-view">
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
              @click="pushRow(scope.row,1)"
              v-if="$store.state.permitteds[secretkey + 'push'] && scope.row.mode != 1"
            >{{info.push}}</u-button>
            <u-button
              shape="text"
              @click="pushRow(scope.row,2)"
              v-if="$store.state.permitteds[secretkey + 'push'] && scope.row.mode == 1"
            >{{info.unpush}}</u-button>
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
      </div>
    </datagrid>
    <NoticeDialog ref="dialog" @submit="reload"></NoticeDialog>
  </div>
</template>
<script>
import Datagrid from "components/table/datagrid.vue";
import NoticeDialog from "dialog/noticeDialog.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";
import SearchInput from "components/search-input.vue";
import Qs from "qs";
export default {
  name: "notice",
  components: { Datagrid, uButton, SearchInput, NoticeDialog },
  data() {
    return {
      keyword: "",
      secretkey: "btn:club:notice:"
    };
  },
  computed: {
    info() {
      return this.$store.state.render.notice.info;
    },
    datagrid() {
      var data = this.$store.state.render.notice.datagrid;
      data.url = inter.notice.list;
      data.columns[1].formatter = this.formatter;
      return data;
    }
  },
  methods: {
    formatter(row) {
      switch (row.mode) {
        case 0:
        case 1:
          return this.info.enum[row.mode];
        default:
          return this.info.enum[2];
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
    pushRow(row,push) {
      this.$fetch(
        inter.notice.push,
        Qs.stringify({ ids: [row.uuid] ,mode:push}, { arrayFormat: "repeat" })
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
    deleteRow(row) {
      var ids = [];
      if (row == undefined) {
        let selectlist = this.$refs.datagrid.getSelection();
        if (selectlist.length > 0) {
          for (var i = 0; i < selectlist.length; i++) {
            ids.push(selectlist[i].uuid);
          }
        }
      } else {
        ids.push(row.uuid);
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
        inter.notice.delete,
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
.notice-view {
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
