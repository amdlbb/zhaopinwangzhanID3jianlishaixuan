<template>
    <div class="admissionItemDiv">
        <div class="admissionItemDivTop">
            <span>录用管理</span>
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
                    <el-table-column prop="workTime" label="报道时间" width="170">
                    </el-table-column>
                    <el-table-column prop="workAddress" label="报道地点" width="400">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="100">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="lookMore(scope.row)">查看更多</el-button>
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
    </div>
</template>

<script>
export default {
    name: "AdmissionItem",
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
        this.$bus.$on("getTeamUserAdmission", () => {
            this.init()
        })
    },
    methods: {
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getAdmissionList", {
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
            let res = await this.$store.dispatch("getAdmissionList", {
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
            let res = await this.$store.dispatch("getSearchAdmission", {
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
        this.$bus.$off('getTeamUserAdmission')

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