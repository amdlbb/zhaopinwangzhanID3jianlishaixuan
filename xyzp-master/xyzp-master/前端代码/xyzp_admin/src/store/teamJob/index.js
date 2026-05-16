import { reqTeamJobExamine, reqChangeExamineStatus, reqAdminSearchExamine } from "@/api"
// state：仓库存储数据的地方
const state = {
    adminInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
    

}
// action：书写业务逻辑（method方法）
const actions = {
    // 获取团队管理信息
    async getTeamJobExamine({commit}, data) {
        return await reqTeamJobExamine(data)
    },
    // 审核状态改变
    async getChangeExamineStatus({commit}, data){
        return await reqChangeExamineStatus(data)
    },
    // 管理员搜索审核
    async adminSearchExamine({commit}, data){
        return await reqAdminSearchExamine(data)
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