import { reqUpload, reqUserInfo2, reqUserResume, reqSaveUserInfo, reqChangUserPassword,
    getUpdateUserResume, reqUserDeliver, reqCancelDeliver, reqSearchDeliver, reqUserAdmission,
    reqSearchUserAllAdmission} from "@/api"
// state：仓库存储数据的地方
const state = {
    userInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {


}
// action：书写业务逻辑（method方法）
const actions = {
    // 上传头像
    async getUpload({ commit }, data){
        let result = await reqUpload(data)
        return result
    },
    // 获取用户信息
    async getUserInfo2({ commit }, data) {
        let result = await reqUserInfo2(data)
        return result
    },
    // 获取用户简历信息
    async getUserResume({ commit }){
        let result = await reqUserResume()
        return result
    },
    // 更新用户信息
    async saveUserInfo({commit}, data){
        let result = await reqSaveUserInfo(data)
        return result
    },
    // 修改用户密码
    async getChangUserPassword({commit}, data){
        let result = await reqChangUserPassword(data)
        return result
    },
    // 更新用户简历
    async getUpdateUserResume({commit}, data){
        let result = await getUpdateUserResume(data)
        return result
    },
    // 获取用户投递
    async getUserDeliver({commit}, data){
        return await reqUserDeliver(data)
    },
    // 取消用户投递
    async getCancelDeliver({commit}, data){
        return await reqCancelDeliver(data)
    },
    // 搜索用户投递
    async getSearchDeliver({commit}, data){
        return await reqSearchDeliver(data)
    },
    // 获取用户录取信息
    async getUserAdmission({commit}, data){
        return await reqUserAdmission(data)
    },
    // 搜索用户录取
    async getSearchUserAllAdmisson({commit}, data){
        return await reqSearchUserAllAdmission(data)
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