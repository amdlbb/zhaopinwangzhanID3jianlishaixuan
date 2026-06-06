import requests from "./requests"

export const reqUpload = (data) => {
    return requests({
        url: '/image/upload',
        method: 'post',
        data: data
    })
}

export const reqEmail = (data) => {
    return requests({
        url: '/email',
        method: 'get',
        params: data
    })
}

export const reqRegister = (data) => {
    return requests({
        url: '/authorization/register',
        method: 'post',
        data: data
    })
}

export const reqLogin = (data) => {
    return requests({
        url: '/authorization/login',
        method: 'post',
        data: data
    })
}


export const reqUserInfo = (data) => {
    return requests({
        url: '/user',
        method: 'get'
    })
}

export const reqUserInfo2 = (data) => {
    return requests({
        url: `/user/${data.id}`,
        method: 'get'
    })
}


export const reqJobs = () => {
    return requests({
        url: "/job",
        method: 'get'
    })
}


export const reqUserResume = () => {
    return requests({
        url: "/user/getUserResume",
        method: 'get'
    })
}


export const reqSaveUserInfo = (data) => {
    return requests({
        url: "/user/updateUserInfo",
        method: 'put',
        data: data
    })
}

export const reqChangUserPassword = (data) => {
    return requests({
        url: "/user/updateUserPassword",
        method: 'put',
        data: data
    })
}

export const getUpdateUserResume = (data) => {
    return requests({
        url: "/user/updateUserResume",
        method: 'put',
        data: data
    })
}

export const reqTeamInfo2 = (data) => {
    return requests({
        url: `/team/${data.id}`,
        method: 'get',
    })
}

export const reqTeamInfo = () => {
    return requests({
        url: "/team",
        method: 'get',
    })
}

export const reqSaveTeamInfo = (data) => {
    return requests({
        url: "/team/updateTeamInfo",
        method: 'put',
        data: data
    })
}

export const reqChangTeamPassword = (data) => {
    return requests({
        url: "/team/updateTeamPassword",
        method: 'put',
        data: data
    })
}


export const reqSaveTeamJob = (data) => {
    return requests({
        url: "/team/saveTeamJob",
        method: 'post',
        data: data
    })
}


export const reqTeamJob = (data) => {
    return requests({
        url: "/team/getTeamJob",
        method: 'get',
        params: data
    })
}


export const reqNewTeamJob = (data) => {
    return requests({
        url: "/team/getNewTeamJob",
        method: 'get',
        params: data
    })
}

export const reqTeamJobInfo = (data) => {
    return requests({
        url: `/team/getTeamJobInfo/${data.id}`,
        method: 'get',
    })
}

export const reqUpdateTeamJob = (data) => {
    return requests({
        url: "/team/updateTeamJob",
        method: 'put',
        data: data
    })
}

export const reqUserDeliver = (data) => {
    return requests({
        url: "/user/getUserDeliver",
        method: 'get',
        params: data
    })
}

export const reqCancelDeliver = (data) => {
    return requests({
        url: "/user/cancelUserDeliver",
        method: 'put',
        data: data
    })
}

export const reqLogout = () => {
    return requests({
        url: "/authorization/logout",
        method: 'post',
    })
}


export const reqSearchDeliver = (data) => {
    return requests({
        url: "/user/getUserDeliver",
        method: 'post',
        data: data
    })
}

export const reqUserResumeTeam = (data) => {
    return requests({
        url: "/team/getUserResume",
        method: 'get',
        params: data
    })
}


export const reqChangTeamJobStatus = (data) => {
    return requests({
        url: "/team/updateTeamJobStatus",
        method: 'put',
        data: data
    })
}


export const reqSaveInterView = (data) => {
    return requests({
        url: "/team/saveInterView",
        method: 'post',
        data: data
    })
}

export const reqDeliverChangeStatus = (data) => {
    return requests({
        url: "/team/updateDeliverStatus",
        method: 'post',
        data: data
    })
}

export const reqInterviewEmail = (data) => {
    return requests({
        url: "/email/sendInterview",
        method: 'get',
        params: data
    })
}

export const reqSearchTeamJob = (data) => {
    return requests({
        url: "/team/searchTeamJob",
        method: 'get',
        params: data
    })
}

export const reqSearchUserResume = (data) => {
    return requests({
        url: "/team/searchUserResume",
        method: 'get',
        params: data
    })
}

export const reqInterviewInfo = (data) => {
    return requests({
        url: "/team/getInterviewInfo",
        method: 'get',
        params: data
    })
}

export const reqSubmitSave = (data) => {
    return requests({
        url: "/team/admissionUser",
        method: 'post',
        data: data
    })
}


export const reqAdmissionEmail = (data) => {
    return requests({
        url: "/email/sendAdmission",
        method: 'get',
        params: data
    })
}

export const reqChangAdmissionStatus = (data) => {
    return requests({
        url: "/team/updateInterviewStatus",
        method: 'put',
        data: data
    })
}


export const reqSearchInterview = (data) => {
    return requests({
        url: "/team/searchInterview",
        method: 'get',
        params: data
    })
}


export const reqAdmissionList = (data) => {
    return requests({
        url: "/team/getAdmissionInfo",
        method: 'get',
        params: data
    })
}


export const reqSearchAdmission = (data) => {
    return requests({
        url: "/team/searchAdmission",
        method: 'get',
        params: data
    })
}

export const reqRecommendTeamJob = () => {
    return requests({
        url: "/team/recommendTeamJob",
        method: 'get'
    })
}

export const reqTeamJobSearch = (data) => {
    return requests({
        url: "/team/searchIndexTeamJob",
        method: 'get',
        params: data
    })
}

export const updateAgainChangeTeamJobStatus = (data) => {
    return requests({
        url: "/team/updateAgainChangeTeamJobStatus",
        method: 'put',
        data: data
    })
}

export const saveUserDeliver = (data) => {
    return requests({
        url: "/user/saveUserDeliver",
        method: 'post',
        data: data
    })
}

export const reqUserAdmission = (data) => {
    return requests({
        url: "/user/getAdmission",
        method: 'get',
        params: data
    })
}


export const reqSearchUserAllAdmission = (data) => {
    return requests({
        url: "/user/searchUserAdmisson",
        method: 'get',
        params: data
    })
}


export const retJobNum = () => {
    return requests({
        url: "/job/getJobAllTest",
        method: 'get',
    })
}
