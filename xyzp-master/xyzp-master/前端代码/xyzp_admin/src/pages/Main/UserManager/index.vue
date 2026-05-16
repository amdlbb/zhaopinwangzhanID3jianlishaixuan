<template>
    <div class="userDiv">
        <NavTop></NavTop>
        <NavHistory></NavHistory>
        <div class="mainContent">
            <div class="mainContentSearch">
                <span class="iconfont icon-sousuo"></span>
                <input type="text" placeholder="账号搜索" @keydown.enter="search" v-model="searchContent">
            </div>
            <div class="mainContentTable">
                <div class="mainContentTableBtn">
                    <ul>
                        <li><button class="addBtn" @click="showReset = true">重置密码</button></li>
                        <li><button class="moveBtn" @click="remove">删除</button></li>
                        <li v-show="multipleSelection.length > 0" class="showChoiceNum"><span>已经选择{{
                            multipleSelection.length }}个</span></li>
                    </ul>
                </div>
                <el-table ref="multipleTable" :data="tableData" style="width: 100%"
                    @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55">
                    </el-table-column>
                    <el-table-column prop="username" label="账号" width="160">
                    </el-table-column>
                    <el-table-column prop="nickname" label="用户昵称" width="250">
                    </el-table-column>
                    <el-table-column label="用户头像" width="150">
                        <template slot-scope="scope">
                            <img :src="scope.row.avatar" alt="" width="50px" height="50px">
                        </template>
                    </el-table-column>
                    <el-table-column prop="introduce" label="介绍" width="350">
                    </el-table-column>
                    <el-table-column prop="status" label="状态" width="80">
                        <template slot-scope="scope">
                            <span v-show="scope.row.status == 1" class="main-table-active">激活</span>
                            <span v-show="scope.row.status == 0" class="main-table-remove">禁用</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" width="150">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="showMore(scope.row)">查看更多</el-button>
                            <el-button type="text" size="small" @click="changeStatus(scope.row.id)" v-show="scope.row.status == 0">激活</el-button>
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

        <el-dialog title="更多信息" :visible.sync="teamMoreInfo" width="30%" custom-class="dialogDiv">
            <div class="tableLine">
                <div class="tableLine1">
                    <span>ID</span>
                </div>
                <div class="tableLine2">
                    <span>{{ showInfo.id }}</span>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>邮箱</span>
                </div>
                <div class="tableLine2">
                    <span>{{ showInfo.email }}</span>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>创建时间</span>
                </div>
                <div class="tableLine2">
                    <span>{{ showInfo.createTime }}</span>
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>更新时间</span>
                </div>
                <div class="tableLine2">
                    <span>{{ showInfo.updateTime }}</span>
                </div>
            </div>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="showReset" width="30%">
            <span style="font-size: 1rem;">确认重置密码为：</span>
            <span style="color: red;">12345678</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="showReset = false">取 消</el-button>
                <el-button type="primary" @click="reset">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import NavTop from '@/components/NavTop'
import NavHistory from "@/components/NavHistory"
export default {
    name: "TeamManager",
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
            // 是否展示更多用户信息
            teamMoreInfo: false,
            // 展示的信息
            showInfo: {},
            // 搜索内容
            searchContent: "",
            // 展示重置页面
            showReset: false
        }
    },
    mounted() {
        // 初始化
        this.init()
    },
    methods: {
        // 勾选行
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getUserAllInfo", {
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
            let res = await this.$store.dispatch("getUserAllInfo", {
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
            this.teamMoreInfo = true
        },
        // 搜索
        async search() {
            if(!this.searchContent.trim()){
                this.$message({
                    message: "搜索内容不能为空",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("getSearchUser", {
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
        // 删除
        async remove() {
            if (this.multipleSelection.length == 0) {
                this.$message({
                    message: "还未选择",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            const idxList = this.multipleSelection.map(item => item.id)
            let res = await this.$store.dispatch("adminRemoveUser", idxList)
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
                    message: "删除成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.init()
            }
        },
        // 恢复状态
        async changeStatus(id) {
            let res = await this.$store.dispatch("adminReturnUser", [id])
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
                    message: "激活成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.init()
                // 关闭弹窗
                this.teamMoreInfo = false
            }
        },
        // 重置密码
        async reset() {
            if (this.multipleSelection.length == 0) {
                this.$message({
                    message: "还未选择",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            const idxList = this.multipleSelection.map(item => item.id)
            let res = await this.$store.dispatch("adminResetUser", idxList)
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
                    message: "重置密码成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.showReset = false
                this.init()
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
    background: #857aee;
}

.moveBtn {
    padding: 7px 15px;
    font-size: 0.9rem;
    background: rgb(211, 66, 66);
    color: white;
    border: 0;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

.moveBtn:active {
    background: rgb(253, 11, 11);
}

.showChoiceNum {
    font-size: 0.9rem;
    color: gray;
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

@media screen and (min-width: 1200px) {
    .userDiv {
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
        width: 30% !important;
    }
}

@media screen and (max-width: 1199px) {
    .userDiv {
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