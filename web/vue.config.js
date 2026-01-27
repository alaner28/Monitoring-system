const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // vue.config.js
  publicPath: './',
  devServer: {
    port: 8081, // 设置端口
    proxy: {
      '/api': {
        target: 'http://localhost:10215', // 要请求的后端地址
        changeOrigin: true,
        /*pathRewrite: {
          '^/api': '', // 重写路径，如果你的请求路径是 '/api/xxx'，实际会去请求 'http://localhost:10215/xxx'
        },*/
      },
    },
  },
  configureWebpack: {
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.vue']
    }
  },
  chainWebpack: config => {
    config.plugins.delete('eslint')
  }
})
