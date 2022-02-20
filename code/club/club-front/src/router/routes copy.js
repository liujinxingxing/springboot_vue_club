// 一级路由
const main = "content";
const Login = () => import("layout/login.vue");
const Index = () => import("layout/index.vue");


// 二级路由
const container = "container";
const Manager = () => import("layout/manager.vue");
const Club = () => import("layout/club.vue");


// 三级路由
const index = "home";
const User = () => import("page/user.vue");
const Home = () => import("page/home.vue");

const routes = [
  {
    path: "/",
    redirect: "/club",
  },
  {
    path: "/club",
    name: "clubMain",
    components: {
      [main]: Index,
    },
    children: [
      {
        path: "/club/:id",
        name: "club",
        components: {
          [container]: Club,
        },
        children: [
          {
            path: "/club/home",
            name: "club:home",
            components: {
              [index]: Home,
            },
          },
          {
            path: "/club/user",
            name: "club:user",
            components: {
              [index]: User,
            },
          },
        ],
      },
    ],
  },
  {
    path: "/manager",
    name: "managerMain",
    components: {
      [main]: Index,
    },
    children: [
      {
        path: "/manager/:id",
        name: "manager",
        components: {
          [container]: Manager,
        },
      },
    ],
  },
  {
    path: "/login",
    name: "auth",
    components: {
      [main]: Login,
    },
  },
];
export default routes;
