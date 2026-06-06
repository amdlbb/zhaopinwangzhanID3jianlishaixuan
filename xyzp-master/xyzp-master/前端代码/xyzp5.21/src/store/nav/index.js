import { reqLogin, reqEmail, reqRegister, reqUserInfo, reqTeamInfo, reqLogout, retJobNum } from "@/api"
// state：仓库存储数据的地方
const state = {
    userInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
    USERININFO(state, responseData) {
        state.userInfo = responseData
    }

}
// action：书写业务逻辑（method方法）
const actions = {
    // 获取用户信息
    async getUserInfo({ commit }, id) {
        let result
        if(id == 0) result = await reqUserInfo()
        else result = await reqTeamInfo()
        if (result.code == 200) {
            commit("USERININFO", result.data)
        }
        return result
    },
    // 发送邮件
    async getEmailCode({ commit }, data) {
        let result = await reqEmail(data)
        return result
    },
    // 注册
    async getRegister({ commit }, data) {
        let result = await reqRegister(data)
        return result
    },
    // 登录
    async getLogin({ commit }, data) {
        let result = await reqLogin(data)
        return result
    },
    // 退出登录
    async logout({commit}){
        return await reqLogout()
    },
    async getJobNum({commit}){
        return await retJobNum();
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