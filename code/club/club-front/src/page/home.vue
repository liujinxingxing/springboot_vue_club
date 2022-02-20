<!--
首页 * author:Daye Shing
-->
<template>
  <el-row class="club-main">
    <el-col :span="8" class="home-info left-info">
      <el-card shadow="hover">
        <div slot="header" class="clearfix">
          <span>{{info.discuss}}</span>
        </div>
        <div
          class="user-suggest"
          v-infinite-scroll="loadMore"
          infinite-scroll-disabled="loading"
          infinite-scroll-distance="20"
        >
          <ul>
            <li v-for="(item, key) in activities" :key="key">
              <span class="user-title">
                {{
                item.nickname ? item.nickname : item.updateUserName
                }}
              </span>
              <span class="user-time">
                {{
                item.updateTime.substring(0, 16)
                }}
              </span>
              <p>{{ item.descripter }}</p>
            </li>
          </ul>
          <el-divider v-if="loading">{{info.loading}}</el-divider>
          <el-divider v-if="noMore">{{info.nomore}}</el-divider>
        </div>
      </el-card>
    </el-col>
    <el-col :span="16" class="home-info right-info">
      <el-row class="home-info-item">
        <el-col :span="10" class="home-info left-info">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>{{info.notice}}</span>
            </div>
            
            <p v-if="notice.length > 0" class="notice-info">{{notice[0].descripter}}</p>
            <p v-else  class="notice-info">{{info.empty}}</p>
          </el-card>
        </el-col>
        <el-col :span="14" class="home-info">
          <el-card shadow="hover">
            <div slot="header" class="clearfix">
              <span>{{render.version.suggest}}</span>
            </div>
            <el-form
              ref="reform"
              :model="suggest"
              :rules="render.version.formFules"
              label-position="top"
            >
              <el-form-item prop="descripter">
                <el-input
                  type="textarea"
                  :rows="7"
                  :maxlength="300"
                  :placeholder="render.version.suggestp"
                  v-model="suggest.descripter"
                ></el-input>
              </el-form-item>
              <u-button
                class="btn-item"
                type="submit"
                @click="submit"
                :disabled="btnState"
              >{{render.version.submit}}</u-button>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
      <div class="home-info-tab">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>{{info.tabs}}</span>
          </div>
          <datagrid :datagrid="datagrid" ref="datagrid">
            <el-table-column
              :label="$store.state.render.common.opt"
              width="60"
              slot="last"
              v-if="$store.state.permitteds['btn:club:welcome:order']"
              align="center"
            >
              <template scope="scope">
                <div class="table-btn-group">
                  <u-button
                    shape="text"
                    @click="order(scope.row)"
                  >{{info.btn}}</u-button>
                </div>
              </template>
            </el-table-column>
          </datagrid>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { inter } from "api/interface";
import Qs from "qs";
import uButton from "components/button/index";
import Datagrid from "components/table/datagrid.vue";
export default {
  data() {
    return {
      activities: [],
      notice: [],
      loading: false,
      noMore: false,
      page: 1,
      size: 10,
      btnState: false,
      suggest: {
        descripter: ""
      }
    };
  },
  components: { uButton, Datagrid },
  mounted() {
    this.loadData();
  },
  computed: {
    render() {
      return this.$store.state.render;
    },
    info() {
      return this.$store.state.render.home.info;
    },
    datagrid() {
      var data = this.$store.state.render.home.datagrid;
      data.url = inter.court.empty;
      return data;
    }
  },
  methods: {
    reload() {
      this.$refs.datagrid.loadData({}, true);
    },
    order(row) {
      this.$bus.$emit("openLoad", this.$store.state.render.common.valid);
      this.$fetch(
        inter.order.court,
        Qs.stringify({
          code: row.code
        })
      )
        .then(res => {
          this.btnState = false;
          if (res.success) {
            // 访问，超时3分钟
            this.$store.commit("setOrder", {
              data: [row],
              prefixId: res.data,
              flag: false
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
          this.btnState = false;
          this.$bus.$emit("closeLoad");
          this.$message({
            type: "error",
            message: this.render.version.error
          });
        });
    },
    submit() {
      this.$refs.reform.validate(valid => {
        if (valid) {
          this.btnState = true;
          this.$fetch(inter.discuss.save, this.suggest, {
            headers: {
              "Content-Type": "application/json"
            }
          })
            .then(res => {
              this.btnState = false;
              if (res.success) {
                this.activities = [];
                this.loading = false;
                this.noMore = false;
                this.page = 1;
                this.size = 10;
                this.suggest = {
                  nickname: "",
                  descripter: "",
                  score: 5
                };
                this.loadMore();
                return;
              }
              this.$message({
                type: "error",
                message: this.render.version.error
              });
            })
            .catch(err => {
              this.btnState = false;
              this.$message({
                type: "error",
                message: this.render.version.error
              });
            });
        } else {
          return false;
        }
      });
    },
    loadData() {
      this.$fetch(
        inter.notice.list,
        Qs.stringify({
          mode: 1,
          roll: true
        })
      ).then(res => {
        if (res.success) {
          this.notice = res.data || [];
          console.log(this.notice);
        }
      });
    },
    loadMore() {
      let $this = this;
      if (!$this.noMore) {
        $this.loading = true;
        $this
          .$fetch(
            inter.discuss.list,
            Qs.stringify({
              page: $this.page,
              rows: $this.size,
              roll: true
            })
          )
          .then(res => {
            if (res.success) {
              if (res.data) {
                for (let i = 0; i < res.data.length; i++) {
                  $this.activities.push(res.data[i]);
                }
              }
              if (res.next) {
                $this.page += 1;
              }
              $this.noMore = !res.next;
            } else {
              $this.noMore = true;
            }
            $this.loading = false;
          });
      }
    }
  }
};
</script>

<style lang="less">
.club-main {
  height: 100%;
  padding: 8px;
  background-color: var(--main-bg) !important;
  overflow: hidden;
  .notice-info{
    line-height: 34px;
    font-size: 18px;
    font-weight: 600;
    color: #999999;
        padding: 20px;
    text-align: inherit;
  }
  .home-info {
    height: 100%;
    &-item {
      height: 250px;
      padding-bottom: 8px;
    }
    .home-info-tab {
      height: calc(~"100% - 250px");
    }
  }
  .left-info {
    padding-right: 8px;
    .user-suggest {
      width: 100%;
      height: 100%;
      padding: 0px 15px;
      overflow-y: auto;
      color: #666;
      ul {
        padding: 0px 10px;
        li {
          position: relative;
          padding: 12px 0px 25px 0px;
          border-bottom: 1px solid #eee;
          .user-title {
            padding-bottom: 0px;
            line-height: 15px;
            font-size: 14px;
            font-weight: 700;
          }
          .el-rate {
            width: 160px !important;
            float: left;
          }
          .user-time {
            line-height: 16px;
            font-size: 13px;
            float: right;
          }
          p {
            line-height: 24px;
            font-size: 14px;
            padding-bottom: 8px;
          }
          &:last-child {
            border-bottom: none;
          }
        }
      }
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
    }
  }
}
</style>
