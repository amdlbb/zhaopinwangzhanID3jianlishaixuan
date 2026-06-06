const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  productionSourceMap: false, // 生成关闭map
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8888',
        ws: true,
        changeOrigin: true
      },
    }
  },
})
