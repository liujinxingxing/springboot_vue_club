module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  outputDir: 'dist',
  productionSourceMap: false, // 打包时不要map文件
  lintOnSave: true, // 是否在保存的时候检查
  configureWebpack: {
    resolve: {
      alias: {
        'components': '@/components',
        'assets': '@/assets',
        'page': '@/page',
        'font': '@/font',
        'layout': '@/layout',
        'script': '@/script',
        'api': '@/api',
        'dialog': '@/dialog'
      }
    },
  },
  devServer: {
    port: 9000,
    hot: true,
    host: "0.0.0.0",
    // 设置代理
    proxy: {
      '/club': {
        target: 'http://127.0.0.1:38080',
        changeOrigin: true,
        pathRewrite: {
          '^/club': '/club'
        }
      },
    },
    disableHostCheck: true
  },
}