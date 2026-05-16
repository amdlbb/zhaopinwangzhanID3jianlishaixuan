import { reqJob, reqJobCategory, reqUpdateJob, reqSearchJob, reqRemoveJob, reqActiveJob, reqSaveJob
 } from "@/api"
// state：仓库存储数据的地方
const state = {
    adminInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
    SAVEADMININFO(state, res) {
        state.adminInfo = res
    }

}
// action：书写业务逻辑（method方法）
const actions = {
    // 登录
    async getJob({ commit }, data) {
        return await reqJob(data)
    },
    // 获取职位分类
    async getJobCategory({ commit }) {
        return await reqJobCategory()
    },
    // 修改职位
    async updateJob({ commit }, data) {
        return await reqUpdateJob(data)
    },
    // 搜索职位
    async searchJob({commit}, data){
        return await reqSearchJob(data)
    },
    // 删除职位
    async removeJob({commit}, data){
        return await reqRemoveJob(data)
    },
    // 激活职位
    async activeJob({commit}, data){
        return await reqActiveJob(data)
    },
    // 添加职位
    async getSaveJob({commit}, data){
        return await reqSaveJob(data)
    },
}
// getters：计算属性
const getters = {

}

export default {
    state,
    mutations,
    actions,
    getters
}