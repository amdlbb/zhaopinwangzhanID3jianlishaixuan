<template>
    <div class="myAdmissionDiv">
        <div class="myAdmissionDivTitle">
            <div class="myAdmissionDivTitleFirst">
                <span>我的录取</span>
            </div>
        </div>
        <div class="myDeliverMain">
            <div class="myDeliverMainTop">
                <i class="iconfont icon-sousuo myDeliverMainTopIcon"></i>
                <input type="text" placeholder="按职位搜索" v-model="searchContent" @keyup.enter="searchAdmission">
            </div>
            <el-table :data="tableData" border style="width: 100%">
                <el-table-column prop="jobName" label="录取职位名称" width="200">
                </el-table-column>
                <el-table-column prop="teamName" label="公司名称" width="200">
                </el-table-column>
                <el-table-column prop="salary" label="薪资" width="200">
                </el-table-column>
                <el-table-column prop="workTime" label="报到时间" width="200">
                </el-table-column>
                <el-table-column prop="address" label="工作地点" width="250">
                </el-table-column>
            </el-table>
            <div class="myDeliverMainPag">
                <el-pagination background layout="prev, pager, next" :page-size="10" :total="totalPage" :current-page="page"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "MyAdmission",
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
        this.$bus.$on("getUserAdmission", () => {
            this.init()
        })
    },
    methods: {
        // 初始化
        async init() {
            // 获取用户投递
            let res = await this.$store.dispatch("getUserAdmission", {
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
            let res = await this.$store.dispatch("getUserAdmission", {
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
        async searchAdmission() {
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
            let res = await this.$store.dispatch("getSearchUserAllAdmisson", {
                content: this.searchContent
            })
            if (res.code == 200) {
                this.tableData = res.data.data
                this.totalPage = res.data.totalNum
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
        this.$bus.$off("getUserAdmission")
    }

}
</script>

<style>
.myAdmissionDivTitle {
    height: 40px;
    border-bottom: 0.1px solid rgb(223, 219, 219);
    display: flex;
}

.myAdmissionDivTitleFirst {
    width: 80%;
    color: rgb(69, 67, 67);
    font-size: 1.2rem;
}

.myAdmissionDivTitleSecond {
    width: 20%;
}

.myAdmissionDivTitleSecond button {
    background-color: rgb(28, 137, 227);
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    width: 70%;
    border-radius: 2px;
    cursor: pointer;
}

.myAdmissionDivTitleSecond button:active {
    background-color: rgb(95, 95, 227);
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

.myAdmissionDivCancelItemBtn {
    text-align: center;
}

.myAdmissionDivCancelItemBtn button {
    margin-left: 14%;
    margin-right: 14%;
}

.myAdmissionDivCancelItemBtn1 {
    background-color: rgb(195, 22, 22);
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    border-radius: 2px;
    cursor: pointer;
    width: 80px;
}

.myAdmissionDivCancelItemBtn1:active {
    background-color: rgb(236, 64, 64);
}

.myAdmissionDivCancelItemBtn2 {
    background-color: rgb(61, 171, 211);
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    border-radius: 2px;
    width: 80px;
    cursor: pointer;
}

.myAdmissionDivCancelItemBtn2:active {
    background-color: rgb(130, 205, 232);
}

.myDeliverMainPag {
    text-align: center;
    margin-top: 20px;
    margin-bottom: 20px;
}
</style>