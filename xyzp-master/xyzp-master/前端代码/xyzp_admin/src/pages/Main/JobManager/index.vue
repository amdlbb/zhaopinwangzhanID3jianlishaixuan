<template>
    <div class="jobDiv">
        <NavTop></NavTop>
        <NavHistory></NavHistory>
        <div class="mainContent">
            <div class="mainContentSearch">
                <span class="iconfont icon-sousuo"></span>
                <input type="text" placeholder="职位搜索" v-model="searchContent" @keydown.enter="search">
            </div>
            <div class="mainContentTable">
                <div class="mainContentTableBtn">
                    <ul>
                        <li><button class="addBtn" @click="addShowDiag">添加</button></li>
                        <li><button class="moveBtn" @click="remove">删除</button></li>
                        <li v-show="multipleSelection.length > 0" class="showChoiceNum"><span>已经选择{{
                            multipleSelection.length }}个</span></li>
                    </ul>
                </div>
                <el-table ref="multipleTable" :data="tableData" style="width: 100%"
                    @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55">
                    </el-table-column>
                    <el-table-column prop="name" label="职位名称" width="150">
                    </el-table-column>
                    <el-table-column label="父职位名称" width="160">
                        <template slot-scope="scope">
                            <span>{{ scope.row.pjobName == 0 ? '无' : scope.row.pjobName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="250">
                    </el-table-column>
                    <el-table-column prop="updateTime" label="更新时间" width="250">
                    </el-table-column>
                    <el-table-column label="状态" width="100">
                        <template slot-scope="scope">
                            <span v-show="scope.row.status == 1" class="main-table-active">激活</span>
                            <span v-show="scope.row.status == 0" class="main-table-remove">禁用</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right">
                        <template slot-scope="scope">
                            <el-button type="text" size="small"
                                @click="showEdit = true, editInfo = scope.row">编辑</el-button>
                            <el-button type="text" size="small" @click="activeJob(scope.row.id)"
                                v-show="scope.row.status == 0">激活</el-button>
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
        <el-dialog title="编辑信息" :visible.sync="showEdit" width="30%" custom-class="dialogDiv">
            <div class="tableLine">
                <div class="tableLine1">
                    <span>职位ID</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="editInfo.id" disabled class="tableInput">
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>职位名称</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="editInfo.name" class="tableInput">
                </div>
            </div>
            <div class="tableBtnLine">
                <button class="addBtn" @click="update">修改</button>
            </div>
        </el-dialog>

        <el-dialog title="添加职位" :visible.sync="addShow" width="30%" custom-class="dialogDiv">
            <div class="tableLine">
                <div class="tableLine1">
                    <span>职位级别</span>
                </div>
                <div class="tableLine2">
                    <el-select v-model="addInfo.value" placeholder="请选择">
                        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>职位名称</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="addInfo.name" class="tableInput">
                </div>
            </div>
            <div class="tableLine" v-show="addInfo.value == 3 || addInfo.value == 2">
                <div class="tableLine1">
                    <span>父职位名称</span>
                </div>
                <div class="tableLine2">
                    <el-select v-model="addInfo.pId" placeholder="请选择">
                        <el-option v-for="item in jobCategoryList[addInfo.value - 2]" :key="item.value" :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="tableBtnLine">
                <button class="addBtn" @click="add">添加</button>
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
            // 页数
            page: 1,
            pageSize: 10,
            // 总页数
            totalNum: 0,
            // 搜索内容
            searchContent: "",
            // 受否展示编辑弹窗
            showEdit: false,
            // 展示的信息
            showInfo: {},
            // 编辑的信息
            editInfo: {},
            // 职位分类
            jobCategoryList: [],
            // 展示添加职位
            addShow: false,
            // 添加信息
            addInfo: {
                value: 1,
                name: "",
                pId: ""
            },
            // 多选数据
            multipleSelection: [],
            // 级别
            options: [{
                value: '1',
                label: '第一级别'
            }, {
                value: '2',
                label: '第二级别'
            }, {
                value: '3',
                label: '第三级别'
            }],



        }
    },
    mounted() {
        this.init()
    },
    methods: {
        // 勾选行
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getJob", {
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
            let res = await this.$store.dispatch("getJob", {
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
        // 展示添加页面
        addShowDiag() {
            this.getJobCategory()
            this.addShow = true
        },
        // 获取职位每个类别的信息
        async getJobCategory() {
            let res = await this.$store.dispatch("getJobCategory")
            if (res.code == 200) {
                this.jobCategoryList = res.data
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
        // 添加职位
        async add() {
            if (this.addInfo.value == 1 && this.addInfo.name == "") {
                this.$message({
                    message: "请正确填写",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.addInfo.value == 2 && (this.addInfo.name == "" || this.addInfo.pId == "") ||
                this.addInfo.value == 3 && (this.addInfo.name == "" || this.addInfo.pId == "")) {
                    this.$message({
                    message: "请正确填写",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("getSaveJob", this.addInfo)
            if (res.code == 200) {
                this.$message({
                    message: "添加成功",
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
        // 修改职位
        async update() {
            let res = await this.$store.dispatch("updateJob", {
                name: this.editInfo.name,
                id: this.editInfo.id
            })
            if (res.code == 200) {
                this.$message({
                    message: "修改成功",
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
        // 搜索职位
        async search() {
            let res = await this.$store.dispatch("searchJob", {
                content: this.searchContent,
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
        // 删除职位
        async remove() {
            if (this.multipleSelection.length == 0) {
                this.$message({
                    message: '还未选择',
                    type: 'error',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("removeJob", this.multipleSelection)
            if (res.code == 200) {
                this.$message({
                    message: '删除成功',
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
        // 激活职位
        async activeJob(id) {
            let res = await this.$store.dispatch("activeJob", {
                id: id
            })
            if (res.code == 200) {
                this.$message({
                    message: '激活成功',
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
    background: #6056b7;
}

.moveBtn:active {
    background: rgb(216, 166, 166);
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

.main-table-active {
    padding: 3px 6px;
    background-color: #968ced;
    color: white;
    border-radius: 3px;
}

.main-table-remove {
    padding: 3px 6px;
    background-color: #d72332;
    color: white;
    border-radius: 3px;
}


.tableLine {
    display: flex;
    align-items: center;
    margin-top: 5px;
    margin-bottom: 5px;
}

.tableLine1 {
    width: 35%;
}

.tableLine2 {
    width: 65%;
}

.tableLine2 button {
    margin-left: 20px;
    padding: 7px 15px;
    font-size: 0.9rem;
    background: rgb(146, 145, 74);
    color: white;
    border: 0;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

.tableLine2 button:active {
    background: rgb(131, 129, 66);
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

@media screen and (min-width: 1200px) {
    .jobDiv {
        width: 83%;
        margin-left: 17%;
    }

    .mainContent {
        width: 95%;
        margin: auto;
        margin-top: 120px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }
}

@media screen and (max-width: 1199px) {
    .jobDiv {
        width: 100%;
    }

    .mainContent {
        width: 100%;
        margin: auto;
        margin-top: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }
}
</style>