<template>
    <div class="workHomeDiv">
        <div class="workHomeDivContent">
            <WorkSearch></WorkSearch>
            <div class="workHomeDivContentLeft">
                <div class="workHomeDivContentLeftMain">
                    <div class="workHomeDivContentItem" @click="toWorkHome(item.id)" v-for="(item, index) in teamJobInfo"
                        :key="index">
                        <div class="workHomeDivContentItemWorkInfo">
                            <div class="workHomeDivContentItemWorkInfoName">
                                <span>{{ item.name }}</span>
                            </div>
                            <div class="workHomeDivContentItemWorkInfoMoney">
                                <span>{{ item.salary }}</span>
                                <span style="margin-left: 10px;">{{ item.education }} </span>
                                <span style="margin-left: 10px;">{{ item.number }}名</span>
                            </div>
                        </div>
                        <div class="workHomeDivContentItemWorkCompanyInfo">
                            <div class="workHomeDivContentItemWorkCompanyInfoImg">
                                <img :src="item.avatar" alt="" width="50px">
                            </div>
                            <div class="workHomeDivContentItemWorkCompanyInfoName">
                                <span>{{ item.nickname }}</span>
                            </div>
                        </div>
                    </div>
                    <div class="workHomeDivContentLeftPagination">
                        <el-pagination background layout="prev, pager, next" :page-size="5" :total="totalPage"
                            :current-page="page" @current-change="handleCurrentChange" v-show="!isSearch">
                        </el-pagination>
                    </div>
                </div>
            </div>
            <div class="workHomeDivContentRight">
                <div class="workHomeDivContentRightItemTop">
                    <span>推荐职位</span>
                    <span style="margin-left: 10px;">{{ totalNum }}</span>
                </div>
                <div class="workHomeDivContentRightItem" v-for="(item1, index1) in recommendTeamJobInfo.slice(0, 3)" :key="index1"
                    @click="toWorkHome(item1.id)">
                    <div class="workHomeDivContentRightItemName">
                        <span>{{ item1.name }}</span>
                    </div>
                    <div class="workHomeDivContentRightItemMoney">
                        <span>{{ item1.salary }}</span>
                        <span style="margin-left: 10px;">{{ item1.education }}</span>
                        <span style="margin-left: 10px;">{{ item1.number }}</span>
                    </div>
                    <div class="workHomeDivContentRightItemCompany">
                        <div>
                            <img :src="item1.avatar" alt="" width="25px">
                        </div>
                        <div>
                            <span>{{ item1.nickname }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<script>
import WorkSearch from './WorkSearch'
import Footer from '@/components/Footer'
export default {
    name: "WorkHome",
    components: {
        WorkSearch,
        Footer
    },
    data() {
        return {
            // 当前页数
            page: 1,
            // 总页数
            totalPage: 0,
            // 数据
            teamJobInfo: [],
            // 推荐数据
            recommendTeamJobInfo: [],
            // 是否为搜索
            isSearch: 0,
        }
    },
    mounted() {
        this.init()
        this.recommend()
        // 全局总线接收搜索的信息
        this.$bus.$on("getSearchData", (data) => {
            this.teamJobInfo = data
            this.isSearch = 1
        })
        this.getJobNum()
    },
    methods: {
        // 初始化
        async init() {
            let res = await this.$store.dispatch('getNewTeamJob', {
                pageSize: 5,
                page: this.page
            })
            if (res.code == 200) {
                this.teamJobInfo = res.data.list
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
        // 随机推荐团队信息
        async recommend() {
            let res = await this.$store.dispatch('getRecommendTeamJob')
            if (res.code == 200) {
                this.recommendTeamJobInfo = res.data
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
            let res = await this.$store.dispatch("getNewTeamJob", {
                page: val,
                pageSize: 10
            })
            if (res.code == 200) {
                this.teamJobInfo = res.data.list
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
        // 跳转页面
        toWorkHome(id) {
            this.$router.push(`/workhome/${id}`)
        },
        async getJobNum() {
            let res = await this.$store.dispatch("getJobNum")
            if(res.code == 200){
                this.totalNum = res.data
            }
        }
    },
}
</script>

<style>
.workHomeDiv {
    background-color: var(--bg-body);
    position: relative;
}

.workHomeDivContent {
    width: 80%;
    margin: auto;
    min-height: 1000px;
    padding-bottom: 40px;
}

.workHomeDivContentItem {
    cursor: pointer;
    display: flex;
    align-items: center;
    background-color: var(--bg-white);
    border-radius: var(--radius-md);
    transition: all .2s;
    margin: 16px 0;
    padding: 16px 24px;
    border: 1px solid var(--border-light);
}

.workHomeDivContentItem:hover {
    box-shadow: var(--shadow-md);
    border-color: var(--primary-lighter);
    transform: translateY(-2px);
}

.workHomeDivContentItemWorkInfo {
    width: 50%;
}

.workHomeDivContentItemWorkInfoName {
    font-size: 1.1rem;
    font-weight: 600;
    text-align: center;
    color: var(--text-primary);
    margin-bottom: 4px;
}

.workHomeDivContentItemWorkInfoMoney {
    color: var(--accent);
    font-size: 0.95rem;
    text-align: center;
}

.workHomeDivContentItemWorkInfoMoney span {
    margin-right: 8px;
}

.workHomeDivContentItemWorkCompanyInfo {
    width: 50%;
    display: flex;
    align-items: center;
    gap: 12px;
    padding-left: 20px;
    border-left: 1px solid var(--border-light);
}

.workHomeDivContentItemWorkCompanyInfoImg {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    overflow: hidden;
    flex-shrink: 0;
}

.workHomeDivContentItemWorkCompanyInfoImg img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.workHomeDivContentItemWorkCompanyInfoName {
    font-size: 0.95rem;
    color: var(--text-secondary);
}

/* 右侧推荐栏 */
.workHomeDivContentRight {
    width: 30%;
    float: right;
}

.workHomeDivContentRightItemTop {
    margin-top: 24px;
    font-size: 1.05rem;
    font-weight: 600;
    color: var(--text-primary);
    padding-bottom: 8px;
    border-bottom: 2px solid var(--primary);
    display: inline-block;
}

.workHomeDivContentRightItem {
    margin: 16px 0;
    padding: 16px;
    background-color: var(--bg-white);
    border-radius: var(--radius-md);
    border: 1px solid var(--border-light);
    transition: all .2s;
    cursor: pointer;
}

.workHomeDivContentRightItem:hover {
    box-shadow: var(--shadow-sm);
    border-color: var(--primary-lighter);
}

.workHomeDivContentRightItemName {
    font-size: 1rem;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 6px;
}

.workHomeDivContentRightItemMoney {
    font-size: 0.85rem;
    color: var(--accent);
    margin-bottom: 10px;
}

.workHomeDivContentRightItemCompany {
    display: flex;
    align-items: center;
    gap: 10px;
    padding-top: 10px;
    border-top: 1px solid var(--border-light);
}

.workHomeDivContentRightItemCompany div img {
    border-radius: 50%;
    height: 36px;
    width: 36px;
    object-fit: cover;
}

.workHomeDivContentRightItemCompany div:last-child {
    font-size: 0.9rem;
    color: var(--text-secondary);
}

.workHomeDivContentLeft {
    width: 70%;
    float: left;
}

.workHomeDivContentLeftMain {
    width: 95%;
    margin: auto;
}

.workHomeDivContentLeftPagination {
    text-align: center;
    margin-top: 24px;
}

.workHomeDivContentLeftPagination .el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color: var(--primary);
}

@media screen and (min-width: 1200px) {
    .workHomeDivContent {
        width: 80%;
        margin: auto;
    }

    .workHomeDivContentLeft {
        width: 70%;
        float: left;
    }

    .workHomeDivContentRight {
        width: 30%;
        float: right;
    }

    .workHomeDivContentLeftMain {
        width: 95%;
        margin: auto;
    }
}

@media screen and (max-width: 1199px) {
    .workHomeDivContent {
        width: 100%;
        margin: auto;
    }

    .workHomeDivContentLeft {
        width: 100%;
    }

    .workHomeDivContentRight {
        width: 100%;
    }

    .workHomeDivContentLeftMain {
        width: 95%;
        margin: auto;
    }
}
</style>