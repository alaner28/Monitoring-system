const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // vue.config.js
  publicPath: './',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://8.152.219.117:8001', // 要请求的后端地址
        changeOrigin: true,
        // pathRewrite: {
        //   '^/api': '', // 重写路径，如果你的请求路径是 '/api/xxx'，实际会去请求 'http://123.57.77.71:10215/xxx'
        // },
      },
    },
  },
})
