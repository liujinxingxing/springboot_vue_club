<!--
订单管理页面 * author:阳春白雪
-->
<template>
  <div class="orders-view">
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
              @click="detail(scope.row)"
              v-if="$store.state.permitteds[secretkey + 'detail']"
            >{{info.detail}}</u-button>
          </div>
        </template>
      </el-table-column>
    </datagrid>
    <BackDialog ref="backDialog" @submit="reload"></BackDialog>
  </div>
</template>
<script>
import Datagrid from "components/table/datagrid.vue";
import { inter } from "api/interface";
import uButton from "components/button/index";
import SearchInput from "components/search-input.vue";
import BackDialog from "dialog/backDialog.vue";
import Qs from "qs";
export default {
  name: "orders",
  components: { Datagrid, uButton, SearchInput,BackDialog },
  data() {
    return {
      keyword: "",
      secretkey: "btn:club:orders:"
    };
  },
  computed: {
    info() {
      return this.$store.state.render.orders.info;
    },
    datagrid() {
      var data = this.$store.state.render.orders.datagrid;
      data.url = inter.order.listSingle;
      data.columns[4].formatter = this.formatter;
      return data;
    }
  },
  methods: {
    formatter(row) {
      switch (row.entry) {
        case 1:
          return this.info.enum[1];
        default:
          return this.info.enum[0];
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
            detail: true
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
              detail: true
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
    }
  },
  watch: {}
};
</script>
<style lang="less">
.orders-view {
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
