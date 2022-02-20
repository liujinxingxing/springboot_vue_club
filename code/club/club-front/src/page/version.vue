<!--
版本说明 * author:阳春白雪
-->
<template>
  <div class="verison">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ render.info }}</span>
      </div>
      <div class="verison-main">
        <el-carousel :interval="5000" height="170px" indicator-position="none">
          <el-carousel-item v-for="item in render.demos" :key="item">
            <img :src="item" />
          </el-carousel-item>
        </el-carousel>
        <div class="vinfo">
          <p v-for="(item,index) in render.tips" :key="index">{{ item }}</p>
        </div>
        <div class="suggest">
          <el-divider content-position="left">
            {{
            render.feel
            }}
          </el-divider>
          <el-form
            ref="form"
            :model="suggest"
            :rules="render.formFules"
            label-suffix=":"
            label-width="75px"
          >
            <el-form-item :label="render.name">
              <el-input v-model="suggest.nickname" :placeholder="render.namep" :maxlength="30"></el-input>
            </el-form-item>
            <el-form-item :label="render.suggest" prop="descripter">
              <el-input
                type="textarea"
                :rows="7"
                :maxlength="300"
                :placeholder="render.suggestp"
                v-model="suggest.descripter"
              ></el-input>
            </el-form-item>
            <el-form-item :label="render.score" class="no-margin">
              <el-rate v-model="suggest.score" show-text></el-rate>
            </el-form-item>
            <div style="float: right;">
              <el-button
                type="primary"
                @click="saveData"
                :disabled="btnState"
                size="mini"
              >{{ render.submit }}</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
import { inter } from "api/interface";
export default {
  data() {
    return {
      btnState: false,
      suggest: {
        nickname: "",
        descripter: "",
        score: 5
      }
    };
  },
  props: {
    render: Object
  },
  methods: {
    saveData() {
      this.$refs.form.validate(validate => {
        if (validate) {
          this.submit();
        } else {
          return false;
        }
      });
    },
    submit() {
      this.btnState = true;
      this.$fetch(inter.discuss.save, this.suggest, {
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(res => {
          this.btnState = false;
          if (res.success) {
            this.$message({
              type: "success",
              message: this.render.feel
            });
            this.suggest = {
              nickname: "",
              descripter: "",
              score: 5
            };
            return;
          }
          this.$message({
            type: "error",
            message: this.render.error
          });
        })
        .catch(err => {
          this.btnState = false;
          this.$message({
            type: "error",
            message: this.render.error
          });
        });
    }
  }
};
</script>
<style lang="less">
.verison {
  width: 100%;
  height: 100%;
  .verison-main {
    position: relative;
    width: 100%;
    height: 100%;
    img {
      height: 170px;
    }
    .vinfo {
      line-height: 25px;
      color: #333;
      font-size: 14px;
      padding: 20px 15px;
    }
    .suggest {
      height: 300px;
      width: 100%;
      position: absolute;
      bottom: 0px;
      z-index: 99;
      background-color: #fff;
      .el-form-item__label {
        font-weight: normal !important;
      }
      .no-margin {
        margin-bottom: 2px !important;
      }
      textarea {
        resize: none;
      }
    }
  }
  .el-card {
    height: 100%;
    border: none;
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
