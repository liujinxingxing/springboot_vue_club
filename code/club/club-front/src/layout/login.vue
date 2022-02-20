<!--
登录页面 * author:阳春白雪
-->
<template>
  <div id="module-login">
    <div class="login-bg login-bg-tl"></div>
    <div class="login-bg login-bg-tr"></div>
    <div class="login-bg login-bg-bt"></div>
    <div class="login-contaniter">
      <div class="login-contaniter-header">
        <div class="title-cn">{{render.title}}</div>
        <div class="title-en">{{render.subtitle}}</div>
      </div>
      <div class="login-form">
        <el-form ref="loginForm" :model="loginInfo" :rules="render.loginFormRules">
          <el-form-item prop="account">
            <el-input
              size="medium"
              :placeholder="render.placeholder.account"
              v-model="loginInfo.account"
              icon="a fa fa-user-o"
            >
              <template slot="prepend">{{render.form.account}}</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              size="medium"
              type="password"
              :placeholder="render.placeholder.password"
              v-model="loginInfo.password"
              show-password
            >
              <template slot="prepend">{{render.form.password}}</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="vacode">
            <el-row class="valid-code">
              <el-col :span="14">
                <el-input
                  size="medium"
                  :placeholder="render.placeholder.vacode"
                  v-model="loginInfo.vacode"
                >
                  <template slot="prepend">{{render.form.vacode}}</template>
                </el-input>
              </el-col>
              <el-col :span="10">
                <div class="valid-img">
                  <img :src="vacodeUrl" @click="getCodeUrl(111, 34)" />
                </div>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-radio-group v-model="loginInfo.realm">
              <el-radio-button label="normal">{{render.form.radio.normal}}</el-radio-button>
              <el-radio-button label="vip">{{render.form.radio.vip}}</el-radio-button>
              <el-radio-button label="admin">{{render.form.radio.admin}}</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button
              type="login"
              class="login-btn"
              @click="handleLogin"
              :disabled="btnState"
            >{{render.form.btn}}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script type="es6">
import { inter } from "api/interface";
import Qs from "qs";
import Crypto from "crypto";
export default {
  name: "auth",
  data() {
    return {
      loginInfo: {
        account: "",
        password: "",
        vacode: "",
        realm: "normal"
      },
      vacodeUrl: "",
      btnState: false
    };
  },
  computed: {
    render(){
      return this.$store.state.render.login;
    },
  },
  mounted() {
    this.getCodeUrl(111, 34);
  },
  methods: {
    getCodeUrl(width, height) {
      this.loginInfo.vacode = "";
      this.vacodeUrl =
        inter.auth.code +
        "?width=" +
        width +
        "&height=" +
        height +
        "&d=" +
        Math.random();
    },
    handleLogin: function() {
      this.btnState = true;
      this.$refs["loginForm"].validate(valid => {
        if (valid) {
          var u = inter.auth.signIn.replace(
            /\$\{code\}/g,
            this.loginInfo.vacode
          );
          u = u.replace(/\$\{realm\}/g, this.loginInfo.realm);
          this.$fetch(
            u,
            {
              account: this.loginInfo.account.trim(),
              password: Crypto.createHash("sha256")
                .update(this.loginInfo.password.trim())
                .digest("hex")
            },
            {
              headers: { "Content-Type": "application/json" }
            }
          ).then(res => {
            this.btnState = false;
            if (res.success) {
              this.$store.commit("setUser", res.data);
              if (res.data.admin) {
                this.$router.push({ name: "manager" });
                return;
              }
              this.$router.push({ name: "club" });
              return;
            }
            this.getCodeUrl(111, 34);
            this.$message({
              type: "error",
              message: res.message
            });
          });
        } else {
          this.btnState = true;
          return false;
        }
      });
    }
  }
};
</script>

<style lang="less">
#module-login {
  height: 100%;
  min-height: 600px;
  background-color: #1fc8db;
  background-image: linear-gradient(
    141deg,
    var(--login-1) 0%,
    var(--login-2) 51%,
    var(--login-3) 75%
  );
  overflow: hidden;
  position: relative;
  .login-bg-bt {
    background-color: rgba(255, 255, 255, 0.1);
    left: 0%;
    width: 434px;
    height: 273px;
    bottom: -192px;
  }
  .login-bg-tl {
    background-color: rgba(255, 255, 255, 0.1);
    left: -349px;
    width: 434px;
    height: 339px;
  }
  .login-bg-tr {
    background-color: rgba(0, 0, 0, 0.03);
    right: -351px;
    width: 473px;
    top: 64px;
    height: 565px;
  }
  .login-bg {
    border-radius: 6px;
    position: absolute;
    -webkit-transform: rotate(338deg);
    -moz-transform: rotate(338deg);
    -o-transform: rotate(338deg);
    transform: rotate(338deg);
    z-index: 1;
  }
  .el-radio-button--small .el-radio-button__inner {
    padding: 9px 23px !important;
  }
  .el-radio-button__orig-radio:checked + .el-radio-button__inner {
    background-color: #2cb5e8 !important;
    border-color: #2cb5e8 !important;
    box-shadow: -1px 0 0 0 #2cb5e8 !important;
  }
  .forget-pass {
    height: 0px;
    position: relative;
    display: flex;
    right: -218px;
    top: -25px;
    .el-button--text {
      line-height: 25px;
      padding: 0px !important;
    }
  }
  .el-input-group__prepend {
    width: 69px !important;
    padding: 0 10px !important;
    line-height: 34px;
    color: #909399;
    font-size: 12px;
    text-align: center;
  }
  .valid-code {
    .valid-img {
      height: 36px;
      border: 1px solid #cbcbcb;
      border-left: none;
      img {
        width: 100%;
        height: 34px;
        vertical-align: middle;
        cursor: pointer;
      }
    }
  }
  .login-contaniter {
    position: relative;
    top: 28%;
    margin: 0 auto;
    width: 350px;
    background: #fff;
    border-radius: 8px;
    z-index: 3;
    .login-contaniter-header {
      color: #fff;
      position: absolute;
      top: -46px;
      left: 50%;
      transform: translate(-50%, 0);
      text-align: center;
      .title-cn {
        font-size: 20px;
        font-weight: bold;
        line-height: 1;
        margin-bottom: 8px;
      }
      .title-en {
        font-size: 12px;
        line-height: 1;
      }
    }
    .login-form {
      padding: 20px;
      .el-form {
        padding: 10px 20px 0px 20px;
        .el-input__inner {
          background-color: #fafafa !important;
          border-color: #cbcbcb !important;
          color: #898989 !important;
          border-radius: 0px;
        }
        .el-form-item__error {
          padding-top: 5px !important;
          padding-left: 70px;
        }
      }
      .fa-lock {
        font-size: 18px;
      }
      .el-form-item {
        margin-bottom: 25px;
        h2 {
          text-align: center;
          color: #666;
        }
      }
      .login-btn {
        font-size: 16px;
        height: 40px;
        width: 100%;
        color: #f2f2f2 !important;
        background: #2cb5e8;
        border: none;
      }
    }
  }
}
</style>
