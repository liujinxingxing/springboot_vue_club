<!--
登录日志管理页面 * author:阳春白雪
-->
<template>
  <div class="loginlog-view">
    <datagrid :datagrid="datagrid" ref="datagrid">
      <div slot="toolbar">
        <div class="search push-right">
          <search-input v-model="keyword" @search="reload()" @clear="reload()"></search-input>
        </div>
      </div>
      <el-table-column
        :label="$store.state.render.common.opt"
        width="120"
        slot="last"
        align="center"
        fixed="right"
      >
        <template scope="scope">
          <div class="table-btn-group">
            <u-button
              shape="text"
              @click="clearRow(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'clear'] && scope.row.quiter == null"
            >{{info.clear}}</u-button>
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
  </div>
</template>
<script>
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";
import SearchInput from "components/search-input.vue";
import Qs from "qs";
export default {
  name: "loginlog",
  components: { Datagrid, uButton, SearchInput },
  data() {
    return {
      keyword: "",
      secretkey: "btn:club:loginlog:"
    };
  },
  computed: {
    info() {
      return this.$store.state.render.loginlog.info;
    },
    datagrid() {
      var data = this.$store.state.render.loginlog.datagrid;
      data.url = inter.auth.list;
      data.columns[2].formatter = this.formatter3;
      data.columns[3].formatter = this.formatter2;
      data.columns[4].formatter = this.formatter1;
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
    formatter3(row) {
      switch (row.quiter) {
        case true:
          return this.info.enum[2][2];
        case false:
          return this.info.enum[2][1];
        default:
          return this.info.enum[2][0];
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
    clearRow(row) {
      this.$fetch(
        inter.auth.signOut,
        Qs.stringify({ id: row.uuid }, { arrayFormat: "repeat" })
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
        inter.user.deleteLog,
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
    }
  },
  watch: {}
};
</script>
<style lang="less">
.loginlog-view {
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
