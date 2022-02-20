import axios from 'axios'
var util = {}
util.install = function (Vue, options) {

  //================ 工具类操作 - 开始 ================
  /**
   * 格式化时间输出
   * @param {*} date
   * @param {*} fmt
   */
  Vue.prototype.datetimeFormat = function (date, fmt) {
    var date = new Date(date);
    var o = {
      "M+": date.getMonth() + 1, //月份
      "d+": date.getDate(), //日
      "h+": date.getHours(), //小时
      "m+": date.getMinutes(), //分
      "s+": date.getSeconds(), //秒
      "q+": Math.floor((date.getMonth() + 3) / 3), //季度
      "S": date.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
    }
    return fmt;
  }
  /**
   *
   * @param {*} time1
   * @param {*} time2
   */
  Vue.prototype.diffTime = function (time1, time2) {
    let date1 = new Date(time1); //开始时间
    let date2 = new Date(time2); //结束时间
    let date3 = date2.getTime() - date1.getTime(); //时间差的毫秒数
    //计算出相差年数
    let years = Math.floor(date3 / (24 * 3600 * 1000 * 365));
    //计算出相差月数
    let leave1 = date3 % (24 * 3600 * 1000 * 365); //计算年数后剩余的毫秒数
    let mouths = Math.floor(leave1 / (24 * 3600 * 1000 * 30));
    //计算出相差天数
    let levave2 = leave1 % (24 * 3600 * 1000 * 30); //计算月数后剩余的毫秒数
    let days = Math.floor(levave2 / (24 * 3600 * 1000));
    //计算出小时数
    let leave3 = levave2 % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
    let hours = Math.floor(leave3 / (3600 * 1000));
    //计算相差分钟数
    let leave4 = leave3 % (3600 * 1000); //计算小时数后剩余的毫秒数
    let minutes = Math.floor(leave4 / (60 * 1000));
    //计算相差秒数
    let leave5 = leave4 % (60 * 1000); //计算分钟数后剩余的毫秒数
    let seconds = Math.round(leave5 / 1000);

    let format = '';
    if (years != 0) {
      format = years + '年' + mouths + '月' + days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (mouths != 0) {
      format = mouths + '月' + days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (days != 0) {
      format = days + '日' + hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (hours != 0) {
      format = hours + '小时' + minutes + '分钟' + seconds + '秒';
    } else if (minutes != 0) {
      format = minutes + '分钟' + seconds + '秒';
    } else {
      format = seconds + '秒';
    }
    return format;
  }
  Vue.prototype.deepClone = function (obj) {
    var o;
    // 如果  他是对象object的话  , 因为null,object,array  也是'object';
    if (typeof obj === "object") {
      // 如果  他是空的话
      if (obj === null) {
        o = null;
      } else {
        // 如果  他是数组arr的话
        if (obj instanceof Array) {
          o = [];
          for (var i = 0, len = obj.length; i < len; i++) {
            o.push(this.deepClone(obj[i]));
          }
        }
        // 如果  他是对象object的话
        else {
          o = {};
          for (var j in obj) {
            o[j] = this.deepClone(obj[j]);
          }
        }
      }
    } else {
      o = obj;
    }
    return o;
  }
  //================ 工具类操作 - 结束 ================

  //================ 网络类操作 - 开始 ================
  /**
   * 设置cookie
   * @param {*} name
   * @param {*} value
   * @param {*} time
   */
  Vue.prototype.setCookie = function (name, value, time) {
    var strsec = this.getsec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
  }
  /**
   *
   * @param {*} str
   */
  Vue.prototype.getsec = function (str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
      return str1 * 1000;
    } else if (str2 == "h") {
      return str1 * 60 * 60 * 1000;
    } else if (str2 == "d") {
      return str1 * 24 * 60 * 60 * 1000;
    }
  }
  Vue.prototype.getCookie = function (name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
      return unescape(arr[2]);
    else
      return null;
  }
  // 4. 添加实例方法
  Vue.prototype.$fetch = function (url, params, config) {
    config = config ? config : {};
    config.headers = config.headers ? config.headers : {};
    var crsfToken = "";
    if (this.getCookie("Csrf-Token")) {
      crsfToken = this.getCookie("Csrf-Token")
    }
    config.headers['Csrf-Token'] = crsfToken
    return new Promise((resolve, reject) => {
      axios.post(url, params, config)
        .then(res => {
          if (!res.data.success) {
            if (res.data.code === '107') {
              // this.$router.push('/login');
            } else {
              resolve(res.data);
            }
          } else {
            resolve(res.data);
          }
        })
        .catch((err) => {
          reject(err);
        })
    })
  }
  Vue.prototype.$catch = function (url, params) {
    params = params ? params : {}
    params.headers = params.headers ? params.headers : {}

    var crsfToken = "";
    if (this.getCookie("Csrf-Token")) {
      crsfToken = this.getCookie("Csrf-Token")
    }
    params.headers['Csrf-Token'] = crsfToken

    return new Promise((resolve, reject) => {
      axios.get(url, params)
        .then(res => {
          resolve(res.data);
        })
        .catch((err) => {
          reject(err);
        })
    })
  }
  Vue.prototype.$getch = function (
      $url,
      $method,
      $data,
      $headers,
    ) {
      return new Promise((resolve, reject) => {
        axios({
          url: $url,
          method: $method.toLowerCase(),
          data: $data,
          headers: $headers,
          timeout: 30000
        }).then(res => {
          resolve(res.data);
        }).catch((err) => {
          reject(err);
        })
      });
    },
    Vue.prototype.$req = function (
      $url,
      $method,
      $data,
      $headers,
      $auth,
      show = false
    ) {
      var startTime = new Date();
      return new Promise((resolve, reject) => {
        axios({
          url: $url,
          method: $method.toLowerCase(),
          data: $data,
          headers: $headers,
          timeout: 30000,
          auth: $auth
        }).then(res => {
          var endTime = new Date();
          var data = {
            requestTime: this.datetimeFormat(startTime, "yyyy-MM-dd hh:mm:ss"),
            responseTime: this.datetimeFormat(endTime, "yyyy-MM-dd hh:mm:ss"),
            diffTime: (endTime.getTime() - startTime.getTime()),
            result: res.data,
            show: show,
            code: res.status,
            requestHeader: $headers,
            responseHeader: res.headers,
          };
          resolve(data);
        }).catch((err) => {
          reject(err);
          console.log(err);
        })
      })
    }
  Vue.prototype.msg = function (res) {
    if (res.code && res.code == '101') {
      throw new Error('错误代码：101:')
      return;
    }
    if (res.success) {
      if (res.message === undefined)
        res.message = "成功";
      var type = 'info';
    } else {
      if (res.message === undefined)
        res.message = "失败";
      var type = 'error';
    }
    this.$message({
      type: type,
      message: res.message
    });
  }
  //================ 网络类操作 - 结束 ================
  Vue.prototype.$formatter = function(data, flag) {
    if (data && data.length > 0) {
      var subscribe = [];
      var order = [];
      var his = [];
      var tmp = null;
      for (var i = 0; i <= data.length; i++) {
        if (i == data.length) {
          if (tmp != null) {
            if (flag) {
              his.push(tmp);
            } else if (tmp.entry == 0) {
              subscribe.push(tmp);
            } else {
              order.push(tmp);
            }
            tmp = null;
          }
          break;
        }
        if (tmp != null && tmp.uuid != data[i].uuid) {
          if (flag) {
            his.push(tmp);
          } else if (tmp.entry == 0) {
            subscribe.push(tmp);
          } else {
            order.push(tmp);
          }
          tmp = null;
        }
        if (flag) {
          tmp = this.$getData(tmp, data[i], flag);
          continue;
        }
        if (data[i].entry == 0) {
          tmp = this.$getData(tmp, data[i], flag);
          continue;
        }
        tmp = this.$getData(tmp, data[i], flag);
      }
      if (his.length == 0) {
        his = null;
      }
      if (subscribe.length == 0) {
        subscribe = null;
      }
      if (order.length == 0) {
        order = null;
      }
      return {
        subscribe: subscribe,
        order: order,
        his: his
      };
    }
  },
  Vue.prototype.$getData = function(ret, data, flag) {
    if (!ret) {
      ret = this.deepClone(data);
      ret.positons = [];
      ret.price = 0;
      if (flag) {
        ret.price = data.price;
      }
    }
    if (!flag) {
      ret.price += data.allPrice;
    }
    if (data.mode == 0) {
      ret.positons.push(data.positon);
    } else {
      ret.positon = data.positon;
    }
    return ret;
  }
  //================ swagger操作 - 结束 ================
}
export default util