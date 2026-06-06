import { reqJobs, reqNewTeamJob, reqTeamJobInfo, reqUpdateTeamJob, reqRecommendTeamJob,
    reqTeamJobSearch, saveUserDeliver} from "@/api"
// state：仓库存储数据的地方
const state = {
    jobsInfo: []
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
    JOBSINFO(state, responseData){
        state.jobsInfo = responseData
    }

}
// action：书写业务逻辑（method方法）
const actions = {
    // 获取职位列表
    async getJobs({ commit }){
        let result = await reqJobs()
        if(result.code == 200) commit("JOBSINFO", result.data)
        return result
    },
    // 获取最新团队招聘信息
    async getNewTeamJob({commit}, data){
        return reqNewTeamJob(data)
    },
    // 获取莫个团队招聘信息
    async getTeamJobInfo({commit}, data){
        return reqTeamJobInfo(data)
    },
    // 更新团队招聘信息
    async updateTeamJob({commit}, data){
        return await reqUpdateTeamJob(data)
    },
    // 获取随机推荐的团队招聘
    async getRecommendTeamJob({commit}){
        return await reqRecommendTeamJob()
    },
    // 搜索
    async getTeamJobSearch({commit}, data){
        return await reqTeamJobSearch(data)
    },
    // 投递简历
    async saveUserDeliver({commit}, data){
        return await saveUserDeliver(data)
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