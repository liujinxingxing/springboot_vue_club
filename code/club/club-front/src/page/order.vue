<!--
预定或者订单购物车页面 * author:Daye Shing
-->
<template>
  <div class="order-main">
    <el-page-header
      @back="goBack"
      :content="info.page[flag?1:2]"
      :title="info.page[0]"
      v-if="head"
    />

    <div class="order-main-body" :class="{'order-main-body-back':head}">
      <el-row class="order-info-item">
        <el-col :span="12" class="order-info order-info-left">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>{{info.title[0]}}</span>
            </div>
            <div class="order-info order-info-left">
              <el-descriptions class="margin-top" :column="4" border direction="vertical">
                <el-descriptions-item>
                  <template slot="label">{{datagrid.columns[0].label}}</template>
                  {{data.code}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">{{datagrid.columns[1].label}}</template>
                  {{data.positon}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">{{info.price}}</template>
                  {{data.price}}
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">{{$store.state.render.common.opt}}</template>
                  <u-button shape="text" @click="cancle()">{{info.cancle}}</u-button>
                </el-descriptions-item>
                <template v-for="(car,key) in cars">
                  <el-descriptions-item
                    labelClassName="label--hidden"
                    v-for="p in prop"
                    :key="p+key"
                  >{{car[p]}}</el-descriptions-item>

                  <el-descriptions-item labelClassName="label--hidden" :key="key">
                    <u-button shape="text" @click="remove(car)">{{info.remove}}</u-button>
                  </el-descriptions-item>
                </template>
              </el-descriptions>

              <el-form
                ref="reform"
                :model="mod"
                :rules="formRules"
                label-width="75px"
                label-suffix=":"
              >
                <template v-if="flag">
                  <el-form-item :label="info.useInfo[0]" prop="useInfo">
                    <el-input v-model="mod.useInfo" :placeholder="info.useInfo[1]"></el-input>
                  </el-form-item>
                  <el-form-item :label="info.contact[0]" prop="contact">
                    <el-input
                      @blur="blurUser"
                      :placeholder="info.contact[1]"
                      v-model="mod.contact"
                      v-show="mod.flag ==1"
                    ></el-input>
                    <SelectRoles ref="SelectRole" @change="changeUser" v-show="mod.flag ==0" />
                  </el-form-item>
                  <el-form-item :label="info.flag.label">
                    <el-radio
                      class="radio"
                      v-model="mod.flag"
                      :label="key"
                      v-for="(item,key) in info.flag.items"
                      :key="key"
                    >{{item}}</el-radio>
                  </el-form-item>
                </template>
                <el-form-item :label="info.startTime[0]" prop="startTime">
                  <el-date-picker
                    v-model="mod.startTime"
                    type="datetime"
                    :placeholder="info.startTime[1]"
                    align="right"
                    @change="changePicker"
                  ></el-date-picker>
                  <el-tooltip placement="right" effect="light">
                    <div slot="content">
                      {{info.time}}-{{info.subscribe}}
                    </div>
                    <i class="fa fa-question-circle" style="color: #4b71a4;"></i>
                  </el-tooltip>
                </el-form-item>
                <el-form-item :label="info.descripter[0]" prop="descripter" v-if="flag">
                  <el-input
                    :maxlength="300"
                    :placeholder="info.descripter[1]"
                    v-model="mod.descripter"
                    type="textarea"
                    :rows="5"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <u-button @click="lend()" v-if="flag" style="margin-right:8px;">{{info.btn[0]}}</u-button>
                  <u-button @click="subscribe()">{{info.btn[1]}}</u-button>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12" class="order-info">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>{{info.title[1]}}</span>
            </div>
            <datagrid :datagrid="datagrid" ref="datagrid">
              <el-table-column
                :label="$store.state.render.common.opt"
                width="80"
                slot="last"
                align="center"
              >
                <template scope="scope">
                  <div class="table-btn-group">
                    <u-button
                      shape="text"
                      @click="addOrder(scope.row)"
                      v-if="$store.state.permitteds['btn:club:welcome:order']"
                    >{{info.select}}</u-button>
                  </div>
                </template>
              </el-table-column>
            </datagrid>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>


<script>
import { inter } from "api/interface";
import Qs from "qs";
import uButton from "components/button/index";
import Datagrid from "components/table/datagrid.vue";
import SelectRoles from "components/selectRoles.vue";

export default {
  data() {
    return {
      mod: {
        prefixId: "",
        entry: 0,
        useInfo: "",
        contact: "",
        startTime: "",
        descripter: "",
        flag: 0
      },
      prop: ["code", "positon", "price"],
      data: {},
      cars: [],
      flag: false,
      self: false,
      detail: false
    };
  },
  components: { uButton, Datagrid, SelectRoles },
  props: {
    head: {
      type: Boolean,
      default() {
        return false;
      }
    },
    mode: {
      type: Boolean,
      default() {
        return true;
      }
    }
  },
  computed: {
    info() {
      return this.$store.state.render.order.info;
    },
    datagrid() {
      var data = this.$store.state.render.order.datagrid;
      data.url = inter.appa.empty;
      return data;
    },
    formRules() {
      return this.$store.state.render.order.formRules;
    }
  },
  mounted() {
    if (this.$store.state.order.detail) {
      this.detail = true;
      this.self = true;
      this.mod.flag = 1;
    }
    if (!this.detail) {
      this.mod.startTime = new Date(new Date().getTime() + 3600000 * 5);
    }
    this.mod.prefixId = this.$store.state.order.prefixId;
    this.flag = this.$store.state.order.flag;
    for (var i = 0; i < this.$store.state.order.data.length; i++) {
      if (i == 0) {
        this.data = this.$store.state.order.data[i];
        if (this.detail) {
          this.mod.startTime = this.data.startTime;

          this.mod.useInfo = this.data.useInfo;
          this.mod.contact = this.data.contact;
        }
        continue;
      }
      this.cars.push(this.$store.state.order.data[i]);
    }
  },
  methods: {
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
            this.goBack(true);
            if (res.success) {
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
    cancle() {
      if (this.detail) {
        this.unsubscribe(this.data);
        return;
      }

      this.$confirm(
        this.$store.state.render.common.leave[1],
        this.$store.state.render.common.leave[0],
        {
          confirmButtonText: this.$store.state.render.common.ok,
          cancelButtonText: this.$store.state.render.common.cancle,
          type: "warning"
        }
      ).then(() => {
        this.$fetch(
          inter.order.clear,
          Qs.stringify({
            prefixId: this.mod.prefixId,
            mode: this.data.mode,
            codes: this.data.code
          })
        )
          .then(res => {
            this.goBack(true);
          })
          .catch(err => {
            this.goBack(true);
          });
      });
    },
    goBack(ok = false) {
      if (ok) {
        this.$emit("back");
        this.$bus.$emit("close", undefined);
        return;
      }
      this.cancle();
    },
    remove(row) {
      if (row.uuid) {
        this.$fetch(
          inter.order.db,
          Qs.stringify({
            prefixId: this.mod.prefixId,
            uuid: row.uuid,
            codes: row.code
          })
        )
          .then(res => {
            if (res.success && res.data) {
              this.$refs.datagrid.loadData({}, true);
              // setTimeout(() => {
              for (var i = 0; i < this.cars.length; i++) {
                if (this.cars[i].code == row.code) {
                  this.cars.splice(i, 1);
                }
                // if(this.cars[i].uuid == undefined ||this.cars[i].uuid == null){
                //   this.$refs.datagrid.toggleSelection(this.cars[i]);
                // }
              }
              // }, 1500);
              return;
            }
          })
          .catch(err => {});
        return;
      }
      this.$fetch(
        inter.order.clear,
        Qs.stringify({
          prefixId: this.mod.prefixId,
          mode: row.mode,
          codes: row.code
        })
      )
        .then(res => {
          if (res.success && res.data) {
            for (var i = 0; i < this.cars.length; i++) {
              if (this.cars[i].code == row.code) {
                this.cars.splice(i, 1);
              }
            }
            this.$refs.datagrid.toggleSelection(row, false);
            return;
          }
        })
        .catch(err => {});
    },
    lend(lend = true) {
      this.mod.entry = lend ? 1 : 0;

      if (this.mod.startTime instanceof Date) {
        this.mod.startTime = this.mod.startTime.getTime();
      }
      if(this.detail){
        this.mod.uuid = this.data.uuid;
      }

      this.$fetch(inter.order.lend, this.mod, {
        headers: { "Content-Type": "application/json" }
      }).then(res => {
        this.btnState = false;
        if (res && res.success) {
          this.$message({
            type: "success",
            message: this.info.success[this.mod.entry]
          });
          this.goBack(true);
          return;
        }
        this.$message({ type: "error", message: res.message });
      });
    },
    blurUser() {
      var tmp = this.mod.contact;
      this.self = true;
      this.$refs.SelectRole.setValue({
        value: tmp,
        text: tmp,
        name: this.mod.useInfo
      });
    },
    changeUser(user) {
      if (this.self) {
        this.self = false;
        return;
      }
      this.mod.useInfo = user.name;
      this.mod.contact = user.value;
      this.$refs.reform.validate(valid => {});
    },
    changePicker() {
      var millis = new Date().getTime() + 3600000;
      if (millis > this.mod.startTime.getTime()) {
        this.$message({
          type: "error",
          message: this.info.time
        });
        this.mod.startTime = new Date(millis);
      }
    },
    subscribe() {
      // 管理员预约
      if (this.flag) {
        this.lend(false);
        return;
      }
      // VIP预约
      this.$fetch(
        inter.order.subscribe,
        Qs.stringify({
          prefixId: this.mod.prefixId,
          startTime: this.mod.startTime
        })
      )
        .then(res => {
          this.btnState = false;
          if (res.success) {
            this.$message({
              type: "success",
              message: this.info.success[0]
            });
            this.goBack(true);
            return;
          }
          this.$message({
            type: "error",
            message: res.message
          });
        })
        .catch(err => {
          this.btnState = false;
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    },
    addOrder(row) {
      var rows = [];
      if (row == undefined) {
        rows = this.$refs.datagrid.getSelection();
      } else {
        rows.push(row);
      }
      if (rows.length == 0) {
        this.$message({
          message: this.info.addWarning,
          type: "warning"
        });
        return;
      }
      var codes = [];
      for (var i = 0; i < rows.length; i++) {
        codes.push(rows[i].code);
      }

      this.$fetch(
        inter.order.appa,
        Qs.stringify(
          {
            codes: codes,
            prefixId: this.mod.prefixId
          },
          { arrayFormat: "repeat" }
        )
      )
        .then(res => {
          this.btnState = false;
          if (res.success) {
            codes = res.data;
            if (codes.length == 0) {
              this.$message({
                type: "error",
                message: this.info.empty
              });
            }
            for (var i = 0; i < codes.length; i++) {
              for (var j = 0; j < rows.length; j++) {
                if (rows[j].code == codes[i]) {
                  this.cars.push(rows[j]);
                  this.$refs.datagrid.toggleSelection(rows[j]);
                }
              }
            }
            return;
          }
          this.$message({
            type: "error",
            message: res.message
          });
        })
        .catch(err => {
          this.btnState = false;
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    }
  }
};
</script>

<style lang="less">
.order-main {
  height: 100%;
  .order-main-body-back {
    height: calc(~"100% - 44px");
  }
  .order-main-body:not(.order-main-body-back) {
    height: 100%;
  }
  &-body {
    .el-table-column--selection {
      pointer-events: none;
    }
    .no-padding {
      .el-checkbox {
        display: none;
      }
    }
    .el-form {
      padding: 30px 10px;
      .el-date-editor.el-input,
      .el-date-editor.el-input__inner {
        width: 220px;
        margin-right: 10px;
      }
    }
    padding: 8px;
    .label--hidden {
      display: none;
    }
    .order-info-item {
      height: 100%;
    }
    .order-info-base {
      text-align: center;
      padding: 3px 6px;

      &-span {
        color: rgba(0, 0, 0, 0.85);
        margin-top: 1rem;
        line-height: 1.25rem;
        font-size: 1.25rem;
        font-weight: 500;
        margin-bottom: 0.25rem;
      }
    }
    .order-info-left {
      padding-right: 8px;
    }
    .order-info {
      height: 100%;
    }
  }
  .el-card {
    height: 100%;

    .el-card__header {
      height: 42px;
      line-height: 42px;
      padding: 0 10px;
      color: #333;
      font-size: 14px;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
    }
    .el-card__body {
      padding: 6px !important;
      height: calc(~"100% - 43px");
      overflow-y: auto;
    }
  }
}
</style>
