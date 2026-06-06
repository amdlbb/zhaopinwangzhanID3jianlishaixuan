import {reqTeamInfo2, reqSaveTeamInfo, reqChangTeamPassword, reqSaveTeamJob, reqTeamJob,
    reqUserResumeTeam, reqChangTeamJobStatus, reqSaveInterView, reqDeliverChangeStatus,
    reqInterviewEmail, reqSearchTeamJob, reqSearchUserResume, reqInterviewInfo, reqSubmitSave,
    reqAdmissionEmail, reqChangAdmissionStatus, reqSearchInterview, reqAdmissionList,
    reqSearchAdmission, updateAgainChangeTeamJobStatus} from "@/api"
// state：仓库存储数据的地方
const state = {
    userInfo: {}
}
// mutations：修改state的唯一手段，因此第一个参数必须是state，第二个参数为将要修改的数据
const mutations = {
  

}
// action：书写业务逻辑（method方法）
const actions = {
    async getTeamInfo2({commit}, data){
        return await reqTeamInfo2(data)
    },
    async saveTeamInfo({commit}, data){
        return await reqSaveTeamInfo(data)
    },
    async getChangTeamPassword({commit}, data){
        return await reqChangTeamPassword(data)
    },
    async saveTeamJob({commit}, data){
        return await reqSaveTeamJob(data)
    },
    async getTeamJob({commit}, data){
        return await reqTeamJob(data)
    },
    async getUserResumeTeam({commit}, data){
        return await reqUserResumeTeam(data)
    },
    async getChangTeamJobStatus({commit}, data){
        return await reqChangTeamJobStatus(data)
    },
    async getSaveInterView({commit}, data){
        return await reqSaveInterView(data)
    },
    async getDeliverChangeStatus({commit}, data){
        return await reqDeliverChangeStatus(data)
    },
    async getInterviewEmail({commit}, data){
        return await reqInterviewEmail(data)
    },
    async getSearchTeamJob({commit}, data){
        return await reqSearchTeamJob(data)
    },
    async getSearchUserResume({commit}, data){
        return await reqSearchUserResume(data)
    },
    async getInterviewInfo({commit}, data){
        return await reqInterviewInfo(data)
    },
    async getSubmitSave({commit}, data){
        return await reqSubmitSave(data)
    },
    async getAdmissionEmail({commit}, data){
        return await reqAdmissionEmail(data)
    },
    async getChangAdmissionStatus({commit}, data){
        return await reqChangAdmissionStatus(data)
    },
    async getSearchInterview({commit}, data){
        return await reqSearchInterview(data)
    },
    async getAdmissionList({commit}, data){
        return await reqAdmissionList(data)
    },
    async getSearchAdmission({commit}, data){
        return await reqSearchAdmission(data)
    },
    async getAgainChangeTeamJobStatus({commit}, data){
        return await updateAgainChangeTeamJobStatus(data)
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