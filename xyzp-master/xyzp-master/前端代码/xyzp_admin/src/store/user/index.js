import { reqUserAllInfo, reqSearchUser, reqAdminRemoveUser, reqAdminReturnUser, reqAdminResetUser } from "@/api"
// state：仓库存储数据的地方
const state = {
    adminInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
   

}
// action：书写业务逻辑（method方法）
const actions = {
    // 获取用户信息
    async getUserAllInfo({commit}, data) {
        return await reqUserAllInfo(data)
    },
    // 搜索用户
    async getSearchUser({commit}, data){
        return await reqSearchUser(data)
    },
    // 删除用户
    async adminRemoveUser({commit}, data){
        return await reqAdminRemoveUser(data)
    },
    // 恢复用户
    async adminReturnUser({commit}, data){
        return await reqAdminReturnUser(data)
    },
    // 重置密码
    async adminResetUser({commit}, data){
        return await reqAdminResetUser(data)
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