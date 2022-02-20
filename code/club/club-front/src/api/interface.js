/**
 * 全局接口定义，管理全局接口
 */
var ContextPath = "/club";
var inter = {
  // 用户
  user: {
    save: ContextPath + "/user/${type}/save",
    list: ContextPath + "/user/list",
    delete: ContextPath + "/user/delete",
    modify: ContextPath + "/user/modify",
    passwd: ContextPath + "/user/passwd",
    exsist: ContextPath + "/user/exsist",
    importUser: ContextPath + "/user/import",
    exportUser: ContextPath + "/user/export",
    init: ContextPath + "/user/init",
    deleteLog: ContextPath + "/user/log/delete",
  },
  // 场地
  court: {
    save: ContextPath + "/court/save",
    list: ContextPath + "/court/true/list",
    empty: ContextPath + "/court/false/list",
    delete: ContextPath + "/court/delete",
    exsist: ContextPath + "/court/exsist",
    importCourt: ContextPath + "/court/import",
    exportCourt: ContextPath + "/court/export",
  },
  // 器材
  appa: {
    save: ContextPath + "/appa/save",
    list: ContextPath + "/appa/true/list",
    empty: ContextPath + "/appa/false/list",
    delete: ContextPath + "/appa/delete",
    exsist: ContextPath + "/appa/exsist",
    importAppa: ContextPath + "/appa/import",
    exportAppa: ContextPath + "/appa/export",
  },
  // 订单
  order:{
    appa: ContextPath + "/order/appa/prefix",
    court: ContextPath + "/order/court/prefix",
    clear: ContextPath + "/order/clear/prefix",
    db: ContextPath + "/order/db/prefix",
    lend: ContextPath + "/order/lend",
    hisSingle: ContextPath + "/order/his/single/list",
    listSingle: ContextPath + "/order/single/list",
    his: ContextPath + "/order/his/list",
    delete: ContextPath + "/order/his/delete",
    list: ContextPath + "/order/list",
    back: ContextPath + "/order/back",
    subscribe: ContextPath + "/order/subscribe",
    unsubscribe: ContextPath + "/order/unsubscribe",
  },
  // 基本
  auth: {
    code: ContextPath + "/auth/signIn/code",
    signIn: ContextPath + "/auth/${realm}/${code}/signIn",
    signOut: ContextPath + "/auth/signOut",
    hasPermissions: ContextPath + "/auth/hasPermissions",
    getCurrentUser: ContextPath + "/auth/getCurrentUser",
    list: ContextPath + "/auth/list",
  },
  // 公告
  notice: {
    save: ContextPath + "/opt/notice/save",
    list: ContextPath + "/opt/notice/list",
    delete: ContextPath + "/opt/notice/delete",
    push: ContextPath + "/opt/notice/push",
  },
  // 留言
  discuss: {
    save: ContextPath + "/opt/discuss/save",
    list: ContextPath + "/opt/discuss/list",
    delete: ContextPath + "/opt/discuss/delete",
    top: ContextPath + "/opt/discuss/top",
  },
  // 文件处理
  io: {
    import: ContextPath + "/io/import",
    upload: ContextPath + "/io/upload",
    downlaod: ContextPath + "/io/downlaod",
    template: ContextPath + "/io/template",
  },
  moduleNames: "club",
};

export { inter };
