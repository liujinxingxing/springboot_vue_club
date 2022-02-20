// 一级路由
const main = "content";
const Login = () => import("layout/login.vue");
const Init = () => import("layout/init.vue");
const Manager = () => import("layout/manager.vue");
const Club = () => import("layout/club.vue");
const routes = [
  {
    path: "/",
    redirect: "/init",
  },
  // 初始化组件
  {
    path: "/init",
    components: {
      [main]: Init,
    },
  },
  // 优先等级高过下面的
  {
    path: "/init/:page",
    components: {
      [main]: Init,
    },
  },
  // 普通用户页面
  {
    path: "/init/club",
    name: "club",
    components: {
      [main]: Club,
    },
  },
  // 管理员页面
  {
    path: "/init/manager",
    name: "manager",
    components: {
      [main]: Manager,
    },
  },
  // 登录页面
  {
    path: "/init/login",
    name: "login",
    components: {
      [main]: Login,
    },
  },
];
export default routes;
