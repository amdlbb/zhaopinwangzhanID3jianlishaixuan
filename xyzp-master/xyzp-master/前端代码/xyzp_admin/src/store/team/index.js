import { reqTeamInfo, reqSearch, reqAdminRemoveTeam, reqAdminReturnTeam,
    reqAdminResetTeam , reqEmail,reqSaveTeamInfo, reqRegister} from "@/api"
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
    async getEmailCode({ commit }, data) {
        let result = await reqEmail(data)
        return result
    },
    // 注册
    async getRegister({ commit }, data) {
        let result = await reqRegister(data)
        return result
    },
    // 获取团队管理信息
    async getTeamInfo({commit}, data) {
        return await reqTeamInfo(data)
    },
    // 更新团队信息
    async saveTeamInfo({commit}, data){
        return await reqSaveTeamInfo(data)
    },
    // 获取团队信息
    async getSearch({commit}, data) {
        return await reqSearch(data)
    },
    // 删除团队
    async adminRemoveTeam({commit}, data){
        return await reqAdminRemoveTeam(data)
    },
    // 激活团队
    async getAdminReturnTeam({commit}, data){
        return await reqAdminReturnTeam(data)
    },
    // 重置团队秘密
    async getAdminResetTeam({commit}, data){
        return await reqAdminResetTeam(data)
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