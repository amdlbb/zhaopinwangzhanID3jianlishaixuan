<template>
    <div class="admissionItemDiv">
        <div class="admissionItemDivTop">
            <span>面试安排</span>
        </div>
        <div class="admissionItemDivContent">
            <div class="admissionItemDivContentTop">
                <i class="iconfont icon-sousuo admissionItemDivContentTopIcon"></i>
                <input type="text" placeholder="按职位搜索" v-model="searchContent" @keydown.enter="search">
            </div>
            <div class="admissionItemDivContentMain">
                <el-table :data="tableData" border style="width: 100%">
                    <el-table-column prop="name" label="姓名" width="140">
                    </el-table-column>
                    <el-table-column prop="jobName" label="招聘的职位" width="140">
                    </el-table-column>
                    <el-table-column prop="beginTime" label="面试开始时间" width="170">
                    </el-table-column>
                    <el-table-column prop="endTime" label="面试结束时间" width="170">
                    </el-table-column>
                    <el-table-column prop="address" label="面试地点" width="300">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="190">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="lookMore(scope.row)">查看更多</el-button>
                            <el-button type="text" size="small"
                                @click="admissionBtn(scope.row.teamJobId, scope.row.userId, scope.row.interviewId, scope.row.jobName)">设为录取</el-button>
                            <el-button type="text" size="small"
                                @click="submitLookOk(scope.row.userId, scope.row.interviewId)">淘汰</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="admissionItemDivContentBotton">
                <el-pagination background layout="prev, pager, next" :page-size="10" :total="totalPage" :current-page="page"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
        <el-dialog title="用户简历" :visible.sync="userResumeMoreDialogVisible" width="40%" custom-class="dialogDiv">
            <div>
                <div class="cvItemDivContentLine">
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-xingming">姓名</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.name" disabled>
                        </div>
                    </div>
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-xingbie">性别</span>
                        </div>
                        <div class="cvItemDivContentItemChoice">
                            <div
                                :class="[moreInfo.sex == '男' ? 'cvItemDivContentItemChoice1' : 'cvItemDivContentItemChoice2']">
                                <span>男</span>
                            </div>
                            <div
                                :class="[moreInfo.sex == '女' ? 'cvItemDivContentItemChoice1' : 'cvItemDivContentItemChoice2']">
                                <span>女</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="cvItemDivContentLine">
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-nianling">年龄</span>
                        </div>
                        <div>
                            <input type="number" v-model="moreInfo.age" disabled>
                        </div>
                    </div>
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-dianhua">电话</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.phone" disabled>
                        </div>
                    </div>
                </div>
                <div class="cvItemDivContentLine">
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-xuexiao">毕业院校</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.school" disabled>
                        </div>
                    </div>
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-xueli">学历</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.education" disabled>
                        </div>
                    </div>
                </div>
                <div class="cvItemDivContentLine">
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-zhiwei">期望职位</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.exceptionJob" disabled>
                        </div>
                    </div>
                    <div class="cvItemDivContentItem">
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-xinzi">期望薪资</span>
                        </div>
                        <div>
                            <input type="text" v-model="moreInfo.exceptionSalary" disabled>
                        </div>
                    </div>
                </div>
                <div class="cvItemDivContentLine1">
                    <div>
                        <div class="cvItemDivContentItemLabel">
                            <span class="iconfont icon-jianli">详细介绍</span>
                        </div>
                        <div>
                            <el-input type="textarea" autosize placeholder="" v-model="moreInfo.content" disabled>
                            </el-input>
                        </div>
                    </div>
                </div>
            </div>
        </el-dialog>

        <el-dialog title="录取设置" :visible.sync="admissionDialogVisible" width="40%" custom-class="dialogDiv">
            <div class="admissionDialog">
                <input type="text" v-model="admissionInfo.workAddress" placeholder="报道地点">
            </div>
            <div class="admissionTime">
                <el-date-picker v-model="admissionInfo.workTime" type="datetime" value-format="timestamp"
                    placeholder="报到时间">
                </el-date-picker>
            </div>
            <div class="admissionSubmitBtn">
                <button @click="submitAdmission">确定</button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
export default {
    name: "InterviewItem",
    data() {
        return {
            // 当前页数
            page: 1,
            // 总页数
            totalPage: 0,
            // 数据
            tableData: [],
            // 搜索内容
            searchContent: "",
            // 更多信息展示
            userResumeMoreDialogVisible: false,
            // 录取界面展示
            admissionDialogVisible: false,
            // 更多信息展示数据
            moreInfo: {},
            // 录取信息设置
            admissionInfo: {
                teamJobId: 0,
                userId: 0,
                interviewId: 0,
                jobName: "",
                workAddress: "",
                workTime: "",
            },

        }
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getInterviewInfo", () => {
            this.init()
        })
    },
    methods: {
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getInterviewInfo", {
                page: this.page,
                pageSize: 10
            })
            if (res.code == 200) {
                this.tableData = res.data.data
                this.totalPage = parseInt(res.data.totalNum)
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 更换页数
        async handleCurrentChange(val) {
            let res = await this.$store.dispatch("getInterviewInfo", {
                page: val,
                pageSize: 10
            })
            if (res.code == 200) {
                this.tableData = res.data.data
                this.totalPage = parseInt(res.data.totalNum)
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 查看更多信息
        lookMore(data) {
            this.moreInfo = data
            this.userResumeMoreDialogVisible = true
        },
        // 显示面试
        admissionBtn(teamJobId, userId, interviewId, jobName) {
            this.admissionInfo.teamJobId = teamJobId
            this.admissionInfo.userId = userId
            this.admissionInfo.interviewId = interviewId
            this.admissionInfo.jobName = jobName
            this.admissionDialogVisible = true
        },
        // 录取
        async submitAdmission() {
            let res = await this.$store.dispatch('getSubmitSave', this.admissionInfo)
            if (res.code == 200) {
                this.init()
                this.admissionDialogVisible = false
                // 发送邮件
                this.sendInterviewEmail({
                    userId: this.admissionInfo.userId,
                    workTime: this.admissionInfo.workTime,
                    jobName: this.admissionInfo.jobName,
                    workAddress: this.admissionInfo.workAddress
                })
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 发送邮件
        async sendInterviewEmail(data) {
            this.$message({
                message: "设置成功，正在发送邮件通知用户报道",
                type: 'success',
                center: true,
                duration: 1500
            })
            let res = await this.$store.dispatch("getAdmissionEmail", data)
            if (res.code == 200) {
                this.$message({
                    message: "发送成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 淘汰
        async submitLookOk(userId, interviewId) {
            let res = await this.$store.dispatch("getChangAdmissionStatus", {
                userId: userId,
                interviewId: interviewId
            })
            if (res.code == 200) {
                this.$message({
                    message: "设置成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.init()
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 搜索
        async search() {
            if (!this.searchContent.trim()) {
                this.$message({
                    message: '请正确填写',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("getSearchInterview", {
                content: this.searchContent
            })
            if (res.code == 200) {
                this.tableData = res.data.data
                this.totalPage = parseInt(res.data.totalNum)
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
    },
    beforeDestroy(){
        this.$bus.$off('getInterviewInfo')
    }

}
</script>

<style>
.admissionItemDivItem {
    width: 30%;
    margin: 1.5%;
    border-radius: 5px;
    height: 150px;
    background-color: rgb(230, 228, 228);
    float: left;
}

.admissionItemDivTop {
    font-size: 1.2rem;
    height: 40px;
    color: rgb(69, 67, 67);
    border-bottom: 0.1px silver solid;
}

.admissionItemDivItem {
    color: #222222;
}

.admissionItemDivItemTop {
    text-align: center;
    font-size: 1.1rem;
    height: 30px;
    line-height: 30px;
    font-size: 560;
}

.admissionItemDivContentTop {
    height: 58px;
    line-height: 58px;
    margin: 0 10px 0 10px;
    position: relative;
}

.admissionItemDivContentTop input {
    padding: 5px 30px;
    border: 1px solid #b8b8b8;
    font-size: 0.9rem;
    color: #1f1f1f;
    transition: .3s;
    height: 25px;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    border-radius: 3px;
}

.admissionItemDivContentTopIcon {
    left: 10px;
    position: absolute;
}

.admissionItemDivContentBotton {
    display: flex;
    height: 50px;
    align-items: center;
    justify-content: center;
}

.admissionDialog {
    height: 60px;
    line-height: 60px;
    text-align: center;
}

.admissionDialog input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.admissionTime {
    text-align: center;
}

.admissionSubmitBtn {
    height: 60px;
    text-align: center;
    line-height: 60px;
}

.admissionSubmitBtn button {
    padding: 5px;
    height: 30px;
    font-size: 0.9rem;
    border-radius: 3px;
    background: #a6a0d9;
    color: white;
    border: 0;
    width: 20%;
    text-align: center;
    cursor: pointer;
}

.admissionSubmitBtn button:active {
    background: rgb(145, 145, 234);
}


@media screen and (min-width: 1200px) {
    .dialogDiv {
        width: 40% !important;
    }
}

@media screen and (max-width: 1200px) {
    .dialogDiv {
        width: 95% !important;
    }
}
</style>