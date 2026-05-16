// 模块式开发
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

// 引入小仓库
import login from './login'
import home from './home'
import team from './team'
import user from './user'
import job from './job'
import teamJob from './teamJob'
import admin from './admin'

export default new Vuex.Store({
    // 实现Vuex仓库模式开发存储数据
    modules:{
        login,
        home,
        team,
        user,
        teamJob,
        job,
        admin
    }
})

