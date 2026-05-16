import { getAdminInfo, reqAdminLogin } from "@/api"
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
    async getLogin({commit},data) {
        return await reqAdminLogin(data)
    },
    // 获取管理员信息
    async getAdminInfo({commit}) {
        let result = await getAdminInfo()
        if (result.code == 200) commit("SAVEADMININFO", result.data)
        return result
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