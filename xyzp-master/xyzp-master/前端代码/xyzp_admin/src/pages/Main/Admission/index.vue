<template>
    <div class="adminDiv">
        <NavTop></NavTop>
        <NavHistory></NavHistory>
        <div class="mainContent">
            <div class="mainContentSearch">
                <span class="iconfont icon-sousuo"></span>
                <input type="text" placeholder="姓名搜索" @keydown.enter="search" v-model="searchContent">
            </div>
            <div class="mainContentTable">
                <div class="mainContentTableBtn">
                    <ul>
                        <li><button class="addBtn" @click="setOnboard">设为入职</button></li>
                        <li v-show="multipleSelection.length > 0" class="showChoiceNum"><span>已经选择{{
                            multipleSelection.length }}个</span></li>
                    </ul>
                </div>
                <el-table ref="multipleTable" :data="tableData" style="width: 100%"
                    @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="70">
                    </el-table-column>
                    <el-table-column prop="username" label="账号" width="120">
                    </el-table-column>
                    <el-table-column prop="email" label="邮箱" width="220">
                    </el-table-column>
                    <el-table-column prop="jobName" label="应聘岗位" width="180">
                    </el-table-column>
                    <el-table-column prop="workAddress" label="报到地点" width="200">
                    </el-table-column>
                    <el-table-column prop="workTime" label="报到时间" width="180">
                        <template slot-scope="scope">
                            <span>{{ formatTime(scope.row.workTime) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="状态" width="120">
                        <template slot-scope="scope">
                            <span v-if="scope.row.status != 8" class="main-table-unonboard">未入职</span>
                            <span v-else class="main-table-onboard">已入职</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" width="120">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="openEmailDialog(scope.row)">邮件沟通</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="mainPag">
                    <el-pagination background layout="prev, pager, next" :total="totalNum"
                        @current-change="handleCurrentChange" :current-page="page" :page-size="10">
                    </el-pagination>
                </div>
            </div>
        </div>

        <!-- 发送邮件对话框 -->
        <el-dialog title="发送邮件" :visible.sync="emailDialogVisible" width="45%" custom-class="dialogDiv">
            <div class="tableLine">
                <div class="tableLine1">
                    <span>发件邮箱</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="fromEmail" class="tableInput" disabled>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>收件邮箱</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="currentToEmail" class="tableInput" disabled>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>邮件标题</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="emailTitle" class="tableInput" placeholder="请输入邮件标题">
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>邮件正文</span>
                </div>
                <div class="tableLine2">
                    <textarea v-model="emailContent" class="tableTextarea" rows="8" placeholder="请输入邮件正文内容"></textarea>
                </div>
            </div>
            <div class="tableBtnLine">
                <button class="addBtn" @click="sendEmail">发送</button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
import NavTop from '@/components/NavTop'
import NavHistory from "@/components/NavHistory"
export default {
    name: "AdminManager",
    components: {
        NavTop,
        NavHistory
    },
    data() {
        return {
            // 表格数据
            tableData: [],
            // 多选的数据
            multipleSelection: [],
            // 页数
            page: 1,
            pageSize: 10,
            // 总页数
            totalNum: 0,
            // 搜索内容
            searchContent: "",
            // 邮件对话框
            emailDialogVisible: false,
            fromEmail: "783129571@qq.com",
            emailTitle: "",
            emailContent: "",
            currentToEmail: "",
            currentUserId: null
        }
    },
    mounted() {
        this.init()
    },
    methods: {
        // 格式化时间
        formatTime(time) {
            if (!time) return ''
            return time.replace('T', ' ')
        },
        // 勾选行
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getAdmissionList", {
                page: this.page,
                pageSize: this.pageSize
            })
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.tableData = res.data.data
                this.totalNum = parseInt(res.data.totalNum)
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
                this.totalNum = parseInt(res.data.totalNum)
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
                    message: "搜索内容不能为空",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("getSearchAdmission", {
                content: this.searchContent
            })
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.tableData = res.data.data
                this.totalNum = parseInt(res.data.totalNum)
            }
        },
        // 设为入职
        async setOnboard() {
            if (this.multipleSelection.length == 0) {
                this.$message({
                    message: "请先勾选需要设为入职的记录",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            const admissionIds = this.multipleSelection.map(item => item.id)
            let res = await this.$store.dispatch("setOnboard", {
                admissionIds: admissionIds
            })
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.$message({
                    message: "设为入职成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.init()
            }
        },
        // 打开发送邮件对话框
        openEmailDialog(row) {
            this.emailDialogVisible = true
            this.currentToEmail = row.email
            this.currentUserId = row.userId
            this.emailTitle = ""
            this.emailContent = ""
        },
        // 发送邮件
        async sendEmail() {
            if (!this.emailTitle.trim()) {
                this.$message({
                    message: "请输入邮件标题",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            if (!this.emailContent.trim()) {
                this.$message({
                    message: "请输入邮件正文",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("sendOnboardEmail", {
                title: this.emailTitle,
                content: this.emailContent,
                userId: this.currentUserId
            })
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.$message({
                    message: "邮件已发送",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.emailDialogVisible = false
            }
        }
    },
}
</script>

<style>
.mainContentSearch {
    position: relative;
    height: 50px;
    line-height: 50px;
    padding: 5px 25px;
    background-color: rgb(248, 248, 248);
}

.mainContentSearch .iconfont {
    position: absolute;
    left: 31px;
}

.mainContentSearch input {
    padding-top: 5px;
    padding-bottom: 5px;
    padding-left: 27px;
    height: 23px;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.mainContentTableBtn {
    padding: 5px 25px;
    height: 40px;
    line-height: 40px;
}

.main-table-unonboard {
    padding: 3px 8px;
    background-color: #e6a23c;
    color: white;
    border-radius: 3px;
    font-size: 0.85rem;
}

.main-table-onboard {
    padding: 3px 8px;
    background-color: #968ced;
    color: white;
    border-radius: 3px;
    font-size: 0.85rem;
}

.mainContentTableBtn ul {
    list-style: none;
    padding-left: 0;
}

.mainContentTableBtn ul li {
    float: left;
    margin-right: 15px;
}

.addBtn {
    padding: 7px 15px;
    font-size: 0.9rem;
    background: #968ced;
    color: white;
    border: 0;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

.addBtn:active {
    background: #857aee;
}

.emailBtn {
    padding: 4px 12px;
    font-size: 0.85rem;
    background: #44ca35;
    color: white;
    border: 0;
    border-radius: 3px;
    cursor: pointer;
}

.emailBtn:active {
    background: #26c018;
}

.showChoiceNum {
    font-size: 0.9rem;
    color: gray;
}

.tableBtnLine {
    text-align: center;
    height: 60px;
    line-height: 60px;
}

.tableInput {
    padding-top: 5px;
    padding-bottom: 5px;
    height: 23px;
    width: 80%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.tableInput:disabled {
    background-color: #f5f5f5;
    color: #999;
    cursor: not-allowed;
}

.tableTextarea {
    width: 80%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    padding: 8px;
    resize: vertical;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.mainPag {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
}

.tableLine {
    display: flex;
    align-items: center;
    margin-top: 5px;
    margin-bottom: 5px;
}

.tableLine1 {
    width: 15%;
    text-align: right;
    padding-right: 10px;
    font-size: 0.95rem;
}

.tableLine2 {
    width: 85%;
}

@media screen and (min-width: 1200px) {
    .adminDiv {
        width: 83%;
        margin-left: 17%;
    }

    .mainContent {
        width: 95%;
        margin: auto;
        margin-top: 120px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        margin-bottom: 15px;
    }

    .dialogDiv {
        width: 45% !important;
    }
}

@media screen and (max-width: 1199px) {
    .adminDiv {
        width: 100%;
    }

    .mainContent {
        width: 100%;
        margin: auto;
        margin-top: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
        margin-bottom: 15px;
    }

    .dialogDiv {
        width: 90% !important;
    }
}
</style>