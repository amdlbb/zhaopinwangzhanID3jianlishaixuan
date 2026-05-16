<template>
    <div class="teamJobDiv">
        <NavTop></NavTop>
        <NavHistory></NavHistory>
        <div class="mainContent">
            <div class="mainContentSearch">
                <span class="iconfont icon-sousuo"></span>
                <input type="text" placeholder="团队搜索" v-model="searchContent" @keydown.enter="search">
            </div>
            <div class="mainContentTable">
                <el-table ref="multipleTable" :data="tableData" style="width: 100%">
                    <el-table-column prop="nickname" label="团队名称" width="200">
                    </el-table-column>
                    <el-table-column prop="name" label="职位名称" width="160">
                    </el-table-column>
                    <el-table-column prop="salary" label="薪资" width="160">
                    </el-table-column>
                    <el-table-column prop="number" label="数量" width="160">
                    </el-table-column>
                    <el-table-column prop="address" label="工作地点" width="300">
                    </el-table-column>
                    <el-table-column label="操作" fixed="right">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="showMore(scope.row)">查看更多</el-button>
                            <el-button type="text" size="small" @click="changeExamineStatus(scope.row, 1)">通过</el-button>
                            <el-button type="text" size="small" @click="changeExamineStatus(scope.row, 2)">不通过</el-button>
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
        <el-dialog title="更多信息" :visible.sync="showMoreInfo" width="30%" custom-class="dialogDiv">
            <div>
                <div class="dialogDivTitle"><span>团队招聘详细内容</span></div>
                <div class="dialogTextera">
                    <el-input type="textarea" autosize placeholder="详细内容" v-model="showInfo.content">
                    </el-input>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import NavTop from '@/components/NavTop'
import NavHistory from "@/components/NavHistory"
export default {
    name: "TeamJobManager",
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
            // 是否展示更多审核信息
            showMoreInfo: false,
            // 展示的信息
            showInfo: {},
            // 搜索内容
            searchContent: "",
        }
    },
    mounted() {
        this.init()
    },
    methods: {
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getTeamJobExamine", {
                page: this.page,
                pageSize: this.pageSize
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
        // 更换页数
        async handleCurrentChange(val) {
            let res = await this.$store.dispatch("getTeamJobExamine", {
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
        // 展示更多
        showMore(data) {
            this.showInfo = data
            this.showMoreInfo = true
        },
        // 更改审核状态
        async changeExamineStatus(data, result) {
            let res = await this.$store.dispatch("getChangeExamineStatus", {
                id: data.id,
                result: result,
                teamId: data.teamId,
                teamJobId: data.teamJobId
            })
            if (res.code == 200) {
                this.$message({
                    message: "审核成功",
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
        // 搜索团队
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
            let res = await this.$store.dispatch("adminSearchExamine", {
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

.mainContentTableBtn ul {
    list-style: none;
    padding-left: 0;
}

.mainContentTableBtn ul li {
    float: left;
    margin-right: 15px;
}

.dialogTextera textarea {
    width: 100%;
    font-size: 1rem;
    border: 0;
    overflow-y: hidden;
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

.moveBtn {
    padding: 7px 15px;
    font-size: 0.9rem;
    background: rgb(244, 49, 49);
    color: white;
    border: 0;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

.mainPag {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
}

.dialogDivTitle {
    font-size: 1.1rem;
    font-weight: 600;
    height: 55px;
    line-height: 55px;
    text-align: center;
}

@media screen and (min-width: 1200px) {
    .teamJobDiv {
        width: 83%;
        margin-left: 17%;
    }

    .mainContent {
        width: 95%;
        margin: auto;
        margin-top: 120px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }

    .dialogDiv {
        width: 30% !important;
    }
}

@media screen and (max-width: 1199px) {
    .teamJobDiv {
        width: 100%;
    }

    .mainContent {
        width: 100%;
        margin: auto;
        margin-top: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }

    .dialogDiv {
        width: 90% !important;
    }
}
</style>