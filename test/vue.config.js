module.exports = {
    devServer: {
      https: true,
      port: 8083,
      open: true,
        proxy: {
            '/': {
                target: 'http://localhost:8443/',
                chageOrigin: true
          },
        '/api/v1': {
            target: 'http://localhost:8443/',
            chageOrigin: true
        }
      },
      historyApiFallback: true,
      hot: true
    },
    css: {
      requireModuleExtension: false // import 시에 경로에 .module 포함 안해도 됨.
    },
    transpileDependencies: [
      'element-plus'
    ],
    lintOnSave: false,
    outputDir: '../backend/src/main/resources/dist'
  }
  