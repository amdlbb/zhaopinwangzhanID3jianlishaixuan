<template>
    <div class="cvItemDiv">
        <div class="cvItemDivTop">
            <span>简历管理</span>
        </div>
        <div class="cvItemDivContent">
            <div class="cvItemDivContentTop">
                <i class="iconfont icon-sousuo cvItemDivContentTopIcon"></i>
                <input type="text" placeholder="按职位搜索" v-model="searchContent" @keydown.enter="search">
            </div>
            <div class="cvItemDivContentMain">
                <el-table :data="tableData" border style="width: 100%">
                    <el-table-column prop="name" label="姓名" width="140">
                    </el-table-column>
                    <el-table-column prop="age" label="年龄" width="70">
                    </el-table-column>
                    <el-table-column prop="sex" label="性别" width="70">
                    </el-table-column>
                    <el-table-column prop="jobName" label="招聘的职位" width="140">
                    </el-table-column>
                    <el-table-column prop="education" label="学历" width="70">
                    </el-table-column>
                    <el-table-column prop="school" label="学校" width="120">
                    </el-table-column>
                    <el-table-column prop="exceptionSalary" label="期望薪资" width="140">
                    </el-table-column>
                    <el-table-column prop="phone" label="联系电话" width="140">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="190">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="lookMore(scope.row)">查看更多</el-button>
                            <el-button type="text" size="small"
                                @click="interviewBtn(scope.row.teamJobId, scope.row.userId, scope.row.deliverId, scope.row.jobName)">设为面试</el-button>
                            <el-button type="text" size="small"
                                @click="submitLookOk(scope.row.userId, scope.row.deliverId)">淘汰</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="cvItemDivContentBotton">
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

        <el-dialog title="设为面试人员" :visible.sync="interviewDialogVisible" width="40%" custom-class="dialogDiv">
            <div class="interviewInput">
                <input type="text" v-model="interviewInfo.address" placeholder="面试地点">
            </div>
            <div class="interviewTime">
                <el-date-picker v-model="interviewInfo.beginTime" type="datetime" value-format="timestamp"
                    placeholder="开始时间">
                </el-date-picker>
                <el-date-picker v-model="interviewInfo.endTime" type="datetime" value-format="timestamp" placeholder="结束时间">
                </el-date-picker>
            </div>
            <div class="interviewSubmitBtn">
                <button @click="submitInterview">确定</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "CvItem",
    data() {
        return {
            // 当前页数
            page: 1,
            // 总页数
            totalPage: 0,
            // 数据
            tableData: [],
            // 更多信息展示
            userResumeMoreDialogVisible: false,
            // 更多信息展示数据
            moreInfo: {},
            // 面试设置展示
            interviewDialogVisible: false,
            // 面试者数据
            interviewInfo: {
                userId: 0,
                teamJobId: 0,
                deliverId: 0,
                address: "",
                beginTime: "",
                endTime: "",
                jobName: "",
            },
            // 搜索内容
            searchContent: "",

        }
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getTeamUserResumeInfo", () => {
            this.init()
        })
    },
    methods: {
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
            let res = await this.$store.dispatch("getSearchUserResume", {
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
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getUserResumeTeam", {
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
            let res = await this.$store.dispatch("getUserResumeTeam", {
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
        interviewBtn(teamJobId, userId, deliverId, jobName) {
            this.interviewInfo.teamJobId = teamJobId
            this.interviewInfo.userId = userId
            this.interviewInfo.deliverId = deliverId
            this.interviewInfo.jobName = jobName
            this.interviewDialogVisible = true
        },
        // 设置为面试
        async submitInterview() {
            // 发生请求
            let res = await this.$store.dispatch("getSaveInterView", this.interviewInfo)
            if (res.code == 200) {
                this.init()
                this.interviewDialogVisible = false
                // 发送邮件通知
                this.sendInterviewEmail({
                    userId: this.interviewInfo.userId,
                    beginTime: this.interviewInfo.beginTime,
                    endTime: this.interviewInfo.endTime,
                    jobName: this.interviewInfo.jobName,
                    address: this.interviewInfo.address
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
        // 设置已看
        async submitLookOk(userId, deliverId) {
            let res = await this.$store.dispatch("getDeliverChangeStatus", {
                userId: userId,
                deliverId: deliverId
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
        // 发送邮件
        async sendInterviewEmail(data) {
            this.$message({
                message: "设置成功，正在发送邮件通知用户面试",
                type: 'success',
                center: true,
                duration: 1500
            })
            let res = await this.$store.dispatch("getInterviewEmail", data)
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
        }

    },
    beforeDestroy() {
        this.$bus.$off('getTeamUserResumeInfo')
    }

}
</script>

<style>
.cvItemDivItem {
    width: 30%;
    margin: 1.5%;
    border-radius: 5px;
    height: 150px;
    background-color: rgb(230, 228, 228);
    float: left;
}

.cvItemDivTop {
    font-size: 1.2rem;
    height: 40px;
    color: rgb(69, 67, 67);
    border-bottom: 0.1px silver solid;
}

.cvItemDivItem {
    color: #222222;
}

.cvItemDivItemTop {
    text-align: center;
    font-size: 1.1rem;
    height: 30px;
    line-height: 30px;
    font-size: 560;
}

.cvItemDivContentTop {
    height: 58px;
    line-height: 58px;
    margin: 0 10px 0 10px;
    position: relative;
}

.cvItemDivContentTop input {
    padding: 5px 30px;
    border: 1px solid #b8b8b8;
    font-size: 0.9rem;
    color: #1f1f1f;
    transition: .3s;
    height: 25px;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    border-radius: 3px;
}

.cvItemDivContentTopIcon {
    left: 10px;
    position: absolute;
}

.cvItemDivContentBotton {
    display: flex;
    height: 50px;
    align-items: center;
    justify-content: center;
}

.cvItemDivContentLine {
    display: flex;
    align-items: center;
    height: 70px;
}

.cvItemDivContentLine1 {
    margin-top: 7px;
    margin-bottom: 20px;
}

.cvItemDivContentLine1 div {
    width: 100%;
}

.cvItemDivContentLine1 textarea {
    padding: 5px 10px;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .1;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.cvItemDivContentLine1 textarea:focus {
    border: 2px solid black;
    overflow-y: hidden;
}

.cvItemDivContentItem {
    width: 50%;
}

.cvItemDivContentItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.cvItemDivContentItemLabel {
    border-left: 4px solid rgb(189, 189, 251);
    padding-left: 5px;
    font-size: 1rem;
    color: rgb(112, 80, 121);
    margin-bottom: 2px;
}

.cvItemDivContentItemChoice2 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(255, 255, 255);
    height: 40px;
    cursor: pointer;
}

.cvItemDivContentItemChoice1 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(185, 190, 194);
    height: 40px;
    cursor: pointer;
}

.cvItemDivContentItemChoice {
    display: flex;
    align-items: center;
}

.interviewInput {
    height: 60px;
    line-height: 60px;
    text-align: center;
}

.interviewInput input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.interviewTime {
    text-align: center;
}

.interviewSubmitBtn {
    height: 60px;
    text-align: center;
    line-height: 60px;
}

.interviewSubmitBtn button {
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

.interviewSubmitBtn button:active {
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