import {
    reqAdminList,
    reqSaveAdmin,
    reqSearchAdmin,
    reqAdminResetAdmin,
    reqAdminReturnAdmin,
    reqAdminRemoveAdmin,
    reqInterviewList,
    reqSubmitSave,
    reqAdmissionEmail,
    reqAdmissionList,
    reqDeliverList,
    reqSetOnboard,
    reqSendOnboardEmail,
    reqSearchResume
} from "@/api"
// state：仓库存储数据的地方
const state = {
  
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
   
}
// action：书写业务逻辑（method方法）
const actions = {
    // 获取管理员列表
    async getAdminList({commit}, data) {
        return await reqAdminList(data)
    },
    async searchResume({commit}, data) {
        return await reqSearchResume(data)
    },
    async getDeliverList({commit}, data) {
        return await reqDeliverList(data)
    },
    async getAdmissionList({commit}, data){
        return await reqAdmissionList(data)
    },
    async getSubmitSave({commit}, data){
        return await reqSubmitSave(data)
    },
    // 添加管理员
    async saveAdmin({commit}, data){
        return await reqSaveAdmin(data)
    },
    async getAdmissionEmail({commit}, data){
        return await reqAdmissionEmail(data)
    },
    //查询全部面试信息
    async getInterviewList({commit}, data){
        return await reqInterviewList(data)
    },

    // 管理员搜索
    async getSearchAdmin({commit}, data){
        return await reqSearchAdmin(data)
    },
    // 重置密码
    async adminResetAdmin({commit}, data){
        return await reqAdminResetAdmin(data)
    },
    // 激活
    async adminReturnAdmin({commit}, data){
        return await reqAdminReturnAdmin(data)
    },
    // 删除
    async adminRemoveAdmin({commit}, data){
        return await reqAdminRemoveAdmin(data)
    },
    // 设为入职
    async setOnboard({commit}, data){
        return await reqSetOnboard(data)
    },
    // 发送入职邮件
    async sendOnboardEmail({commit}, data){
        return await reqSendOnboardEmail(data)
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