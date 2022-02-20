import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  // 全局状态
  state: {
    user: {
      account: "0000",
      userName: "游客",
      level:0,
      admin:false,
      avatar:"",
    },
    init:true,
    order:{},
    permitteds:{},
    render:{}
  },
  // 修改全局状态
  mutations: {
    setOrder(state, order){
      state.order = order;
    },
    setRender(state, render){
      state.render = render;
      state.init = false;
    },
    setPermitteds(state, permitteds){
      state.permitteds = permitteds;
    },
    setUser(state, user) {
      state.user = user;
    }
  },
  getters: {},
  actions: {},
  modules: {}
})