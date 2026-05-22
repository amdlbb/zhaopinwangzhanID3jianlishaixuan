import requests from "./requests"

export const reqAdminLogin = (data) => {
    return requests({
        url: "/authorization/login",
        method: 'post',
        data: data
    })
}

export const getAdminInfo = () => {
    return requests({
        url: "/admin/getAdminInfo",
        method: 'get',
    })
}

export const updateExamineResult = (data) => {
    return requests({
        url: "/admin/updateExamineResult",
        method: 'post',
        data: data
    })
}

export const reqUserAllInfo = (data) => {
    return requests({
        url: "/user/getUserAll",
        method: 'get',
        params: data
    })
}

export const getTeamJobExamine = (data) => {
    return requests({
        url: "/team/getTeamJobExamine",
        method: 'get',
        params: data
    })
}

export const reqIndexInfo = () => {
    return requests({
        url: "/admin/getIndexInfo",
        method: 'get',
    })
}

export const reqTeamInfo = (data) => {
    return requests({
        url: "/team/getTeamAll",
        method: 'get',
        params: data
    })
}

export const reqSearch = (data) => {
    return requests({
        url: "/team/adminSearchTeam",
        method: 'post',
        data: data
    })
}

export const reqAdminRemoveTeam = (data) => {
    return requests({
        url: "/team/adminRemoveTeam",
        method: 'post',
        data: data
    })
}

export const reqAdminReturnTeam = (data) => {
    return requests({
        url: "/team/adminReturnTeam",
        method: 'post',
        data: data
    })
}

export const reqAdminResetTeam = (data) => {
    return requests({
        url: "/team/adminResetTeam",
        method: 'put',
        data: data
    })
}

export const reqSearchUser = (data) => {
    return requests({
        url: "/user/adminSearchUser",
        method: 'post',
        data: data
    })
}

export const reqAdminRemoveUser = (data) => {
    return requests({
        url: "/user/adminRemoveUser",
        method: 'put',
        data: data
    })
}

export const reqAdminReturnUser = (data) => {
    return requests({
        url: "/user/adminReturnUser",
        method: 'put',
        data: data
    })
}

export const reqAdminResetUser = (data) => {
    return requests({
        url: "/user/adminResetUser",
        method: 'put',
        data: data
    })
}

export const reqTeamJobExamine = (data) => {
    return requests({
        url: "/team/adminTeamJobExamineAll",
        method: 'get',
        params: data
    })
}

export const reqJob = (data) => {
    return requests({
        url: "/job/adminJobAll",
        method: 'get',
        params: data
    })
}

export const reqChangeExamineStatus = (data) => {
    return requests({
        url: "/team/adminChangeExamineStatus",
        method: 'put',
        data: data
    })
}

export const reqAdminSearchExamine = (data) => {
    return requests({
        url: "/team/adminSearchExamine",
        method: 'post',
        data: data
    })
}


export const reqLogout = () => {
    return requests({
        url: "/authorization/logout",
        method: 'post',
    })
}


export const reqAdminList = (data) => {
    return requests({
        url: "/admin/getAdminAll",
        method: 'get',
        params: data
    })
}

export const reqInterviewList = (data) => {
    return requests({
        url: "/interview/getInterviewAll",
        method: 'get',
        params: data
    })
}

export const reqDeliverList = (data) => {
    return requests({
        url: "/deliver/list",
        method: 'get',
        params: data
    })
}


export const reqSaveAdmin = (data) => {
    return requests({
        url: "/admin/saveAdmin",
        method: 'post',
        data: data
    })
}

export const reqSearchAdmin = (data) => {
    return requests({
        url: "/admin/searchAdmin",
        method: 'post',
        data: data
    })
}

export const reqAdminResetAdmin = (data) => {
    return requests({
        url: "/admin/resetAdmin",
        method: 'put',
        data: data
    })
}

export const reqAdminReturnAdmin = (data) => {
    return requests({
        url: "/admin/returnAdmin",
        method: 'put',
        data: data
    })
}

export const reqAdminRemoveAdmin = (data) => {
    return requests({
        url: "/admin/removeAdmin",
        method: 'put',
        data: data
    })
}

export const updateAdminInfo = (data) => {
    return requests({
        url: "/admin/updateAdminInfo",
        method: 'put',
        data: data
    })
}


export const reqJobCategory = () => {
    return requests({
        url: "/job/getJobCategory",
        method: 'get',
    })
}


export const reqUpdateJob = (data) => {
    return requests({
        url: "/job/updateJob",
        method: 'put',
        data: data
    })
}

export const reqSearchJob = (data) => {
    return requests({
        url: "/job/searchJob",
        method: 'get',
        params: data
    })
}

export const reqRemoveJob = (data) => {
    return requests({
        url: "/job/removeJob",
        method: 'post',
        data: data
    })
}

export const reqActiveJob = (data) => {
    return requests({
        url: "/job/activeJob",
        method: 'post',
        data: data
    })
}


export const reqSaveJob = (data) => {
    return requests({
        url: "/job/save",
        method: 'post',
        data: data
    })
}

/**
 * 上传文件接口
 * @param {FormData} data - 表单数据，通常包含文件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUpload = (data) => {
    return requests({
        url: '/image/upload',
        method: 'post',
        data: data
    })
}

/**
 * 发送邮件接口
 * @param {Object} data - 查询参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqEmail = (data) => {
    return requests({
        url: '/email',
        method: 'get',
        params: data
    })
}

/**
 * 用户注册接口
 * @param {Object} data - 用户注册信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqRegister = (data) => {
    return requests({
        url: '/authorization/register',
        method: 'post',
        data: data
    })
}

/**
 * 用户登录接口
 * @param {Object} data - 用户登录信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqLogin = (data) => {
    return requests({
        url: '/authorization/login',
        method: 'post',
        data: data
    })
}

/**
 * 根据ID获取用户信息
 * @param {Object} data - 包含用户ID的对象
 * @param {string} data.id - 用户ID
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUserInfo2 = (data) => {
    return requests({
        url: `/user/${data.id}`,
        method: 'get'
    })
}

/**
 * 获取所有职位类别
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqJobs = () => {
    return requests({
        url: "/job",
        method: 'get'
    })
}

/**
 * 获取当前用户的简历
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUserResume = () => {
    return requests({
        url: "/user/getUserResume",
        method: 'get'
    })
}

/**
 * 保存用户信息（更新）
 * @param {Object} data - 更新后的用户信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSaveUserInfo = (data) => {
    return requests({
        url: "/user/updateUserInfo",
        method: 'put',
        data: data
    })
}

/**
 * 修改用户密码
 * @param {Object} data - 包含旧密码和新密码
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqChangUserPassword = (data) => {
    return requests({
        url: "/user/updateUserPassword",
        method: 'put',
        data: data
    })
}

/**
 * 更新用户简历
 * @param {Object} data - 新的简历信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const getUpdateUserResume = (data) => {
    return requests({
        url: "/user/updateUserResume",
        method: 'put',
        data: data
    })
}

/**
 * 根据ID获取团队信息
 * @param {Object} data - 包含团队ID的对象
 * @param {string} data.id - 团队ID
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqTeamInfo2 = (data) => {
    return requests({
        url: `/team/${data.id}`,
        method: 'get',
    })
}

/**
 * 保存团队信息（更新）
 * @param {Object} data - 更新后的团队信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSaveTeamInfo = (data) => {
    return requests({
        url: "/team/updateTeamInfo",
        method: 'put',
        data: data
    })
}

/**
 * 修改团队密码
 * @param {Object} data - 包含旧密码和新密码
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqChangTeamPassword = (data) => {
    return requests({
        url: "/team/updateTeamPassword",
        method: 'put',
        data: data
    })
}

/**
 * 保存团队职位（发布新职位）
 * @param {Object} data - 职位信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSaveTeamJob = (data) => {
    return requests({
        url: "/team/saveTeamJob",
        method: 'post',
        data: data
    })
}

/**
 * 获取团队的职位列表
 * @param {Object} data - 可能包含分页参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqTeamJob = (data) => {
    return requests({
        url: "/team/getTeamJob",
        method: 'get',
        params: data
    })
}

/**
 * 获取团队的新职位（最新发布）
 * @param {Object} data - 可能包含分页参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqNewTeamJob = (data) => {
    return requests({
        url: "/team/getNewTeamJob",
        method: 'get',
        params: data
    })
}

/**
 * 获取特定职位的详细信息
 * @param {Object} data - 包含职位ID的对象
 * @param {string} data.id - 职位ID
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqTeamJobInfo = (data) => {
    return requests({
        url: `/team/getTeamJobInfo/${data.id}`,
        method: 'get',
    })
}

/**
 * 更新团队职位信息
 * @param {Object} data - 更新后的职位信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUpdateTeamJob = (data) => {
    return requests({
        url: "/team/updateTeamJob",
        method: 'put',
        data: data
    })
}

/**
 * 获取用户投递记录
 * @param {Object} data - 可能包含分页参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUserDeliver = (data) => {
    return requests({
        url: "/user/getUserDeliver",
        method: 'get',
        params: data
    })
}

/**
 * 取消投递
 * @param {Object} data - 包含投递记录ID等信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqCancelDeliver = (data) => {
    return requests({
        url: "/user/cancelUserDeliver",
        method: 'put',
        data: data
    })
}

/**
 * 搜索用户投递记录
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchDeliver = (data) => {
    return requests({
        url: "/user/getUserDeliver",
        method: 'post',
        data: data
    })
}

/**
 * 获取投递给团队的用户简历
 * @param {Object} data - 可能包含职位ID等参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUserResumeTeam = (data) => {
    return requests({
        url: "/team/getUserResume",
        method: 'get',
        params: data
    })
}

/**
 * 更改团队职位状态（如开启/关闭招聘）
 * @param {Object} data - 包含职位ID和状态
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqChangTeamJobStatus = (data) => {
    return requests({
        url: "/team/updateTeamJobStatus",
        method: 'put',
        data: data
    })
}

/**
 * 保存面试信息
 * @param {Object} data - 面试安排信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSaveInterView = (data) => {
    return requests({
        url: "/team/saveInterView",
        method: 'post',
        data: data
    })
}

/**
 * 更改投递状态（如筛选通过/不通过）
 * @param {Object} data - 包含投递记录ID和状态
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqDeliverChangeStatus = (data) => {
    return requests({
        url: "/team/updateDeliverStatus",
        method: 'post',
        data: data
    })
}

/**
 * 发送面试邮件
 * @param {Object} data - 包含收件人和面试信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqInterviewEmail = (data) => {
    return requests({
        url: "/email/sendInterview",
        method: 'get',
        params: data
    })
}

/**
 * 搜索团队职位
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchTeamJob = (data) => {
    return requests({
        url: "/team/searchTeamJob",
        method: 'get',
        params: data
    })
}

/**
 * 搜索用户简历
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchUserResume = (data) => {
    return requests({
        url: "/team/searchUserResume",
        method: 'get',
        params: data
    })
}

/**
 * 获取面试信息
 * @param {Object} data - 可能包含面试ID等参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqInterviewInfo = (data) => {
    return requests({
        url: "/team/getInterviewInfo",
        method: 'get',
        params: data
    })
}

/**
 * 录用用户
 * @param {Object} data - 录用信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSubmitSave = (data) => {
    return requests({
        url: "/team/admissionUser",
        method: 'post',
        data: data
    })
}

/**
 * 发送录用邮件
 * @param {Object} data - 包含收件人和录用信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqAdmissionEmail = (data) => {
    return requests({
        url: "/email/sendAdmission",
        method: 'get',
        params: data
    })
}

/**
 * 更改录用状态
 * @param {Object} data - 包含录用记录ID和状态
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqChangAdmissionStatus = (data) => {
    return requests({
        url: "/team/updateInterviewStatus",
        method: 'put',
        data: data
    })
}

/**
 * 搜索面试记录
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchInterview = (data) => {
    return requests({
        url: "/team/searchInterview",
        method: 'get',
        params: data
    })
}

/**
 * 获取录用列表
 * @param {Object} data - 可能包含分页参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqAdmissionList = (data) => {
    return requests({
        url: "/team/getAllAdmissionInfo",
        method: 'get',
        params: data
    })
}

/**
 * 搜索录用记录
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchAdmission = (data) => {
    return requests({
        url: "/team/searchAdmission",
        method: 'get',
        params: data
    })
}

/**
 * 获取推荐的团队职位
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqRecommendTeamJob = () => {
    return requests({
        url: "/team/recommendTeamJob",
        method: 'get'
    })
}

/**
 * 首页搜索团队职位
 * @param {Object} data - 搜索关键词
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqTeamJobSearch = (data) => {
    return requests({
        url: "/team/searchIndexTeamJob",
        method: 'get',
        params: data
    })
}

/**
 * 设为入职
 * @param {Object} data - 包含 admissionIds 数组
 * @returns {Promise}
 */
export const reqSetOnboard = (data) => {
    return requests({
        url: "/onboard/setOnboard",
        method: 'put',
        data: data
    })
}

/**
 * 发送入职沟通邮件
 * @param {Object} data - 包含 title, content, userId
 * @returns {Promise}
 */
export const reqSendOnboardEmail = (data) => {
    return requests({
        url: "/onboard/sendEmail",
        method: 'post',
        data: data
    })
}

/**
 * 重新开启/关闭职位
 * @param {Object} data - 包含职位ID和状态
 * @returns {Promise} 返回请求的Promise对象
 */
export const updateAgainChangeTeamJobStatus = (data) => {
    return requests({
        url: "/team/updateAgainChangeTeamJobStatus",
        method: 'put',
        data: data
    })
}

/**
 * 用户投递职位
 * @param {Object} data - 投递信息
 * @returns {Promise} 返回请求的Promise对象
 */
export const saveUserDeliver = (data) => {
    return requests({
        url: "/user/saveUserDeliver",
        method: 'post',
        data: data
    })
}

/**
 * 获取用户录用信息
 * @param {Object} data - 可能包含分页参数
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqUserAdmission = (data) => {
    return requests({
        url: "/user/getAdmission",
        method: 'get',
        params: data
    })
}

/**
 * 搜索用户所有录用记录
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchUserAllAdmission = (data) => {
    return requests({
        url: "/user/searchUserAdmisson",
        method: 'get',
        params: data
    })
}

/**
 * 获取职位数量统计
 * @returns {Promise} 返回请求的Promise对象
 */
export const retJobNum = () => {
    return requests({
        url: "/job/getJobAllTest",
        method: 'get',
    })
}

/**
 * es搜索职位
 * @param {Object} data - 搜索条件
 * @returns {Promise} 返回请求的Promise对象
 */
export const reqSearchResume = (data) => {
    return requests({
        url: "/deliver/searchResume",
        method: 'post',
        data: data
    })
}