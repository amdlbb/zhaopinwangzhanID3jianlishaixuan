import { reqIndexInfo, reqLogout, updateAdminInfo } from "@/api"
// state：仓库存储数据的地方
const state = {
    adminInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {

}
// action：书写业务逻辑（method方法）
const actions = {
    // 首页信息
    async getIndexInfo({ commit }) {
        return await reqIndexInfo()
    },
    // 退出
    async logout({ commit }) {
        return await reqLogout()
    },
    // 修改信息
    async getUpdateAdminInfo({commit}, data){
        return await updateAdminInfo(data)
    }


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