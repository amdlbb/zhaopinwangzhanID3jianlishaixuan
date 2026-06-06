// 模块式开发
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

// 引入小仓库
import home from './home'
import userhome from './userhome'
import workhome from './workhome'
import nav from './nav'
import teamhome from './teamhome'

export default new Vuex.Store({
    // 实现Vuex仓库模式开发存储数据
    modules:{
        home,
        userhome,
        workhome,
        nav,
        teamhome
        
    }
})

