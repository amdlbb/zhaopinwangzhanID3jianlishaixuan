<template>
    <div class="JobItemDiv">
        <div class="JobItemDivTop">
            <div class="JobItemDivTopTitle">
                <span>在招职位</span>
            </div>
            <div class="JobItemDivTopCreate">
                <button @click="addDialogVisible = true">+发布招聘</button>
            </div>
        </div>
        <div class="JobItemDivContent">
            <div class="JobItemDivContentTop">
                <i class="iconfont icon-sousuo JobItemDivContentTopIcon"></i>
                <input type="text" placeholder="按职位搜索" v-model="searchContent" @keyup.enter="search">
            </div>
            <div class="JobItemDivContentMain">
                <el-table :data="tableData" border style="width: 100%">
                    <el-table-column prop="name" label="职位名称" width="140">
                    </el-table-column>
                    <el-table-column prop="number" label="数量" width="120">
                    </el-table-column>
                    <el-table-column prop="salary" label="薪资" width="120">
                    </el-table-column>
                    <el-table-column prop="address" label="工作地点" width="340">
                    </el-table-column>
                    <el-table-column prop="status" label="状态" width="120">
                        <template slot-scope="scope">
                            <span v-show="scope.row.status == 1">正在审核中</span>
                            <span v-show="scope.row.status == 2">审核通过</span>
                            <span v-show="scope.row.status == 3">审核不合格</span>
                            <span v-show="scope.row.status == 4">已发布</span>
                            <span v-show="scope.row.status == 5">已下架</span>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="160">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="submitChangeBtn(scope.row)">编辑</el-button>
                            <el-button type="text" size="small" v-show="scope.row.status == 5 || scope.row.status == 2"
                                @click="submitChangeStatus(scope.row.id, 4)">发布</el-button>
                            <el-button type="text" size="small" v-show="scope.row.status == 4"
                                @click="submitChangeStatus(scope.row.id, 5)">下架</el-button>
                            <el-button type="text" size="small" v-show="scope.row.status == 3"
                                @click="againChangeStatus(scope.row.id, 1)">重新审核</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="JobItemDivContentBotton">
                <el-pagination background layout="prev, pager, next" :page-size="10" :total="totalPage" :current-page="page"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
        <el-dialog title="添加招聘信息" :visible.sync="addDialogVisible" width="60%" custom-class="dialogDiv">
            <WorkSearch></WorkSearch>
            <div class="JobItemDivAddItem">
                <input type="text" placeholder="薪资" v-model="addTeamInfo.salary">
            </div>
            <div class="JobItemDivAddItem">
                <input type="number" placeholder="数量" v-model="addTeamInfo.number">
            </div>
            <div class="JobItemDivAddItem">
                <input type="text" placeholder="工作地点" v-model="addTeamInfo.address">
            </div>
            <div class="JobItemDivAddItem">
                <input type="text" placeholder="学历" v-model="addTeamInfo.education">
            </div>
            <div class="JobItemDivAddItem1">
                <el-input type="textarea" autosize placeholder="详细内容" v-model="addTeamInfo.content">
                </el-input>
            </div>
            <div class="JobItemDivAddItem">
                <button @click="submitPublish">发布</button>
            </div>
        </el-dialog>
        <el-dialog title="编辑招聘信息" :visible.sync="changeDialogVisible" width="60%" custom-class="dialogDiv">
            <div class="JobItemDivAddItem">
                <div class="JobItemDivAddItemLine1">职位名称</div>
                <input type="text" v-model="changeTeamInfo.name" disabled>
            </div>
            <div class="JobItemDivAddItem">
                <div class="JobItemDivAddItemLine1">薪资</div>
                <input type="text" placeholder="薪资" v-model="changeTeamInfo.salary">
            </div>
            <div class="JobItemDivAddItem">
                <div class="JobItemDivAddItemLine1">数量</div>
                <input type="number" placeholder="数量" v-model="changeTeamInfo.number">
            </div>
            <div class="JobItemDivAddItem">
                <div class="JobItemDivAddItemLine1">工作地点</div>
                <input type="text" placeholder="工作地点" v-model="changeTeamInfo.address">
            </div>
            <div class="JobItemDivAddItem">
                <div class="JobItemDivAddItemLine1">学历</div>
                <input type="text" placeholder="学历" v-model="changeTeamInfo.education">
            </div>
            <div class="JobItemDivAddItem1">
                <div class="JobItemDivAddItem1Line1">详细内容</div>
                <el-input type="textarea" autosize placeholder="详细内容" v-model="changeTeamInfo.content">
                </el-input>
            </div>
            <div class="JobItemDivAddItem">
                <button @click="submitChange">编辑</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import WorkSearch from "@/pages/WorkHome/WorkSearch"
export default {
    name: "JobItem",
    components: {
        WorkSearch
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getTeamJobInfo", () => {
            this.init()
        })
        // 收到全局总线传来的结果
        this.$bus.$on("getJobData", (data) => {
            this.addTeamInfo['jobId'] = data
        })
    },
    data() {
        return {
            tableData: [],
            // 是否展示添加团队招聘信息
            addDialogVisible: false,
            // 是否展示修改团队招聘信息
            changeDialogVisible: false,
            // 添加的团队信息
            addTeamInfo: {
                address: "",
                number: "",
                salary: "",
                education: "",
                content: ""
            },
            // 编辑团队信息
            changeTeamInfo: {
                name: "",
                address: "",
                number: "",
                salary: "",
                education: "",
                content: ""
            },
            // 当前页数
            page: 1,
            // 总页数
            totalPage: 0,
            // 搜索内容
            searchContent: "",
        }
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
            let res = await this.$store.dispatch("getSearchTeamJob", {
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
        // 发布招聘信息
        async submitPublish() {
            if (!this.addTeamInfo.address.trim() || !this.addTeamInfo.salary.trim() || !this.addTeamInfo.number) {
                this.$message({
                    message: '请正确填写',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
            }
            // 获取职位id
            this.$bus.$emit("getJobId")
            // 发送请求保存结果
            let res = await this.$store.dispatch('saveTeamJob', this.addTeamInfo)
            if (res.code == 200) {
                this.$message({
                    message: '发布成功，等待管理员审核',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求数据
                this.init()
                this.addDialogVisible = false
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
            let res = await this.$store.dispatch("getTeamJob", {
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
        // 显示编辑招聘信息
        submitChangeBtn(data) {
            this.changeTeamInfo = data
            this.changeDialogVisible = true
        },
        // 显示编辑招聘信息
        async submitChange() {
            let res = await this.$store.dispatch('updateTeamJob', this.changeTeamInfo)
            if (res.code == 200) {
                this.$message({
                    message: "修改成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求数据
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
        // 更换页数
        async handleCurrentChange(val) {
            let res = await this.$store.dispatch("getTeamJob", {
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
        // 更改招聘状态
        async submitChangeStatus(id, status) {
            let res = await this.$store.dispatch("getChangTeamJobStatus", {
                id: id,
                status: status
            })
            if (res.code == 200) {
                this.$message({
                    message: "更新成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求
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
        // 重新审核
        async againChangeStatus(id, status) {
            let res = await this.$store.dispatch("getAgainChangeTeamJobStatus", {
                id: id,
                status: status
            })
            if (res.code == 200) {
                this.$message({
                    message: "更新成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求
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
        }
    },
    beforeDestroy() {
        this.$bus.$off('getJobData')
        this.$bus.$off('getTeamJobInfo')
    }
}
</script>

<style>
.JobItemDivTop {
    height: 40px;
    border-bottom: 0.1px silver solid;
    display: flex;
    color: rgb(69, 67, 67);
}

.JobItemDivTopCreate button {
    background-color: rgb(28, 137, 227);
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    width: 70%;
    border-radius: 2px;
    cursor: pointer;
}

.JobItemDivAddItemLine1{
    width: 20%;
}

.JobItemDivAddItem1Line1{
    margin-top: 10px;
    margin-bottom: 10px;
}

.JobItemDivTopCreate button:active {
    background-color: rgb(95, 95, 227);
}

.JobItemDivContentTop {
    height: 58px;
    line-height: 58px;
    margin: 0 10px 0 10px;
    position: relative;
}

.JobItemDivContentTop input {
    padding: 5px 30px;
    border: 1px solid #b8b8b8;
    font-size: 0.9rem;
    color: #1f1f1f;
    transition: .3s;
    height: 25px;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    border-radius: 3px;
}

.JobItemDivContentTopIcon {
    left: 10px;
    position: absolute;
}

.JobItemDivContentBotton {
    display: flex;
    height: 50px;
    align-items: center;
    justify-content: center;
}

.JobItemDivAddItem {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.JobItemDivAddItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.JobItemDivAddItem button {
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

.JobItemDivAddItem button:active {
    background: rgb(145, 145, 234);
}

.JobItemDivAddItem1 textarea {
    padding: 5px 10px;
    width: 90%;
    margin: auto;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.JobItemDivAddItem1 textarea:focus {
    border: 2px solid black;
    overflow-y: hidden;
}


@media screen and (min-width: 1200px) {
    .dialogDiv {
        width: 60% !important;
    }

    .JobItemDivTopTitle {
        font-size: 1.2rem;
        height: 40px;
        width: 80%;
    }

    .JobItemDivTopCreate {
        width: 20%;
        height: 40px;
    }

}

@media screen and (max-width: 1200px) {
    .dialogDiv {
        width: 95% !important;
    }

    .JobItemDivTopTitle {
        font-size: 1.2rem;
        height: 40px;
        width: 70%;
    }

    .JobItemDivTopCreate {
        width: 30%;
        height: 40px;
    }

}
</style>