<template>
    <div class="myDeliverDiv">
        <div class="myDeliverDivTitle">
            <div class="myDeliverDivTitleFirst">
                <span>我的投递</span>
            </div>
        </div>
        <div class="myDeliverMain">
            <div class="myDeliverMainTop">
                <i class="iconfont icon-sousuo myDeliverMainTopIcon"></i>
                <input type="text" placeholder="按职位搜索" v-model="searchContent" @keyup.enter="searchDeliver">
            </div>
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="jobName" label="职位名称" width="200">
                </el-table-column>
                <el-table-column prop="teamName" label="公司名称" width="200">
                </el-table-column>
                <el-table-column prop="salary" label="薪资" width="200">
                </el-table-column>
                <el-table-column prop="address" label="工作地点" width="250">
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="160">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" v-show="scope.row.status == 1"
                            @click=" choiceDeliverId = scope.row.id, cancelDeliverDialogVisible = true">取消投递</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="myDeliverMainPag">
                <el-pagination background layout="prev, pager, next" :page-size="10" :total="totalPage" :current-page="page"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>

        </div>
        <el-dialog title="提示" :visible.sync="cancelDeliverDialogVisible" width="30%" center  custom-class="dialogDiv">
            <div class="myDeliverDivCancelItemBtn">
                <button @click="submitCancel" class="myDeliverDivCancelItemBtn1">确定</button>
                <button @click="cancelDeliverDialogVisible = false" class="myDeliverDivCancelItemBtn2">取消</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "MyDeliver",
    data() {
        return {
            tableData: [],
            cancelDeliverDialogVisible: false,
            // 选择取消的投递的id
            choiceDeliverId: -1,
            // 搜索内容
            searchContent: "",
            // 当前页数
            page: 1,
            // 总页数
            totalPage: 0,

        }
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getUserDeliver", () => {
            this.init()
        })
    },
    methods: {
        // 初始化
        async init() {
            // 获取用户投递
            let res = await this.$store.dispatch("getUserDeliver", {
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
        // 确定投递取消
        async submitCancel() {
            if (this.choiceDeliverId == -1) {
                this.$message({
                    message: "请重试",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 请求取消投递
            let res = await this.$store.dispatch("getCancelDeliver", {
                id: this.choiceDeliverId
            })
            if (res.code == 200) {
                this.$message({
                    message: "取消投递成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求数据
                this.init()
                this.cancelDeliverDialogVisible = false
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
        async searchDeliver() {
            if (!this.searchContent.trim()) {
                this.$message({
                    message: "请正确输入",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发生请求
            let res = await this.$store.dispatch("getSearchDeliver", {
                content: this.searchContent
            })
            if (res.code == 200) {
                this.tableData = res.data
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
            let res = await this.$store.dispatch("getUserDeliver", {
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

    },
    beforeDestroy() {
        // 撤销总线
        this.$bus.$off("getUserDeliver")
    }
}
</script>

<style>
.myDeliverDivTitle {
    height: 40px;
    border-bottom: 0.1px solid rgb(223, 219, 219);
    display: flex;
}

.myDeliverDivTitleFirst {
    width: 80%;
    color: rgb(69, 67, 67);
    font-size: 1.2rem;
}

.myDeliverDivTitleSecond {
    width: 20%;
}

.myDeliverDivTitleSecond button {
    background-color: #1a3a5c;
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    width: 70%;
    border-radius: 2px;
    cursor: pointer;
}

.myDeliverDivTitleSecond button:active {
    background-color: rgb(49, 73, 234);
}

.myDeliverMainTop {
    height: 58px;
    line-height: 58px;
    margin: 0 10px 0 10px;
    position: relative;
}

.myDeliverMainTop input {
    padding: 5px 30px;
    border: 1px solid #b8b8b8;
    font-size: 0.9rem;
    color: #1f1f1f;
    transition: .3s;
    height: 25px;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    border-radius: 3px;
}

.myDeliverMainTopIcon {
    left: 10px;
    position: absolute;
}

.myDeliverDivCancelItemBtn {
    text-align: center;
}

.myDeliverDivCancelItemBtn button {
    margin-left: 14%;
    margin-right: 14%;
}

.myDeliverDivCancelItemBtn1 {
    background-color: #1a3a5c;
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    border-radius: 2px;
    cursor: pointer;
    width: 80px;
}

.myDeliverDivCancelItemBtn1:active {
    background-color: rgb(49, 73, 234);
}

.myDeliverDivCancelItemBtn2 {
    background-color: #1a3a5c;
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    border-radius: 2px;
    width: 80px;
    cursor: pointer;
}

.myDeliverDivCancelItemBtn2:active {
    background-color: rgb(49, 73, 234));
}

.myDeliverMainPag {
    text-align: center;
    margin-top: 20px;
    margin-bottom: 20px;
}

@media screen and (min-width: 1200px) {
    .dialogDiv {
        width: 30% !important;
    }

}

@media screen and (max-width: 1200px) {
    .dialogDiv {
        width: 95% !important;
    }
}
</style>