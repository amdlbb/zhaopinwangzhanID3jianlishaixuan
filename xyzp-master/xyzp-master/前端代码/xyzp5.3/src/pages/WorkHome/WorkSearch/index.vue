<template>
    <div class="workSearchDiv">
        <div class="workSearchDivTop">
            <div class="workSearchDivContentSearchInput">
                <input type="text" placeholder="搜索职位" slot="reference" v-model="search.content" @keydown.enter="searchFun">
                <button class="iconfont icon-sousuo" @click="searchFun">搜索</button>
            </div>
        </div>
        <div class="workSearchDivContentSearchTiaojian">
            <div class="workSearchDivContentSearchTiaojianLeft">
                <div v-for="item, index in data" :key="index"
                    :class="[myChoice == index ? 'workSearchDivContentSearchTiaojianLeftItem1' : 'workSearchDivContentSearchTiaojianLeftItem']"
                    @click="myChoice = index, myChoice2 = 0">
                    {{ item.name }}
                </div>
            </div>
            <div class="workSearchDivContentSearchTiaojianRight">
                <div class="workSearchDivContentSearchTiaojianRightLeft">
                    <div v-for="item1, index1 in data[myChoice].list" :key="index1"
                        class="workSearchDivContentSearchTiaojianRightLeftItem"
                        :class="[myChoice2 == index1 ? 'workSearchDivContentSearchTiaojianRightLeftItem1' : 'workSearchDivContentSearchTiaojianRightLeftItem']"
                        @click="myChoice2 = index1">
                        {{ item1.name }}
                    </div>
                </div>
                <div class="workSearchDivContentSearchTiaojianRightRight">
                    <ul>
                        <li v-for="(item2, index2) in data[myChoice].list[myChoice2].list" :key="index2"
                            @click="choiceJob(item2.id, item2.name)">{{ item2.name }}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'WorkSearch',
    data() {
        return {
            // 第一类选择
            myChoice: 0,
            // 第二类选择
            myChoice2: 0,
            // 搜索内容
            search: {
                content: "",
                id: ""
            },
        }
    },
    mounted() {
        this.getJobs()
        // 全局总线拿到信息
        this.$bus.$on("getJobId", () => {
            this.$bus.$emit("getJobData", this.search.id)
        })
    },
    methods: {
        // 获取职位表
        async getJobs() {
            let res = await this.$store.dispatch("getJobs")
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
        },
        // 选择职位
        choiceJob(id, name) {
            this.search.content = name
            this.search.id = id
            // 发送请求
        },
        // 搜索
        async searchFun() {
            /*if (!this.search.content.trim()) {
                this.$message({
                    message: "搜索不能空",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }*/
            // 发送请求
            let res = await this.$store.dispatch("getTeamJobSearch", {
                content: this.search.content
            })
            if (res.code == 200) {
                this.$bus.$emit("getSearchData", res.data)
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
    computed: {
        data() {
            return this.$store.state.workhome.jobsInfo || []
        }
    },
    beforeDestroy() {
        this.$bus.$off("getJobId")
    }


}
</script>

<style>
.workSearchDivContentSearch {
    height: 180px;
}

.workSearchDivContentSearchInput {
    text-align: center;
    height: 80px;
    line-height: 80px;
}

.workSearchDivContentSearchInput input:focus {
    outline: 0;
}

.workSearchDivContentSearchTiaojian {
    height: 250px;
    overflow: hidden;
    color: #2d2e2e;
    border-radius: 3px;
    font-size: 0.9rem;
}

.workSearchDivContentSearchInput button:active {
    background: var(--primary-dark);
}

.workSearchDivContentSearchTiaojian {
    background-color: white;
    display: flex;
}

.workSearchDivContentSearchTiaojianLeftItem {
    height: 35px;
    line-height: 35px;
    cursor: pointer;
}

.workSearchDivContentSearchTiaojianLeftItem1 {
    height: 35px;
    line-height: 35px;
    background-color: #e4dcdc;
    cursor: pointer;
}

.workSearchDivContentSearchTiaojianRightLeftItem {
    height: 35px;
    line-height: 35px;
    cursor: pointer;
}

.workSearchDivContentSearchTiaojianRightLeftItem1 {
    height: 35px;
    line-height: 35px;
    background-color: rgb(241, 241, 236);
    cursor: pointer;
}

/* 设置滚动条样式 */
.workSearchDivContentSearchTiaojianLeft::-webkit-scrollbar {
    width: 3px;
    height: 3px;
}

.workSearchDivContentSearchTiaojianLeft::-webkit-scrollbar-track {
    background-color: transparent;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.workSearchDivContentSearchTiaojianLeft::-webkit-scrollbar-thumb {
    background-color: rgb(147, 147, 153, 0.5);
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.workSearchDivContentSearchTiaojianRightLeft::-webkit-scrollbar {
    width: 30px;
    height: 3px;
}

.workSearchDivContentSearchTiaojianRightLeft::-webkit-scrollbar-track {
    background-color: transparent;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.workSearchDivContentSearchTiaojianRightLeft::-webkit-scrollbar-thumb {
    background-color: rgb(147, 147, 153, 0.5);
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.workSearchDivSearchPopTitle {
    display: flex;
    align-items: center;
    height: 20px;
    font-size: 0.7rem;
    color: gray;
}

.workSearchDivSearchPopTitleItem1 {
    width: 80%;
}

.workSearchDivSearchPopTitleItem2 {
    width: 20%;
    text-align: center;
}

.workSearchDivSearchPopContentItem {
    display: inline-block;
    padding: 2px 4px;
    margin: 7px;
    border-radius: 3px;
    cursor: pointer;
    background: #f8f8f8;
    color: #666;
    word-break: break-all;
    transition: all .2s linear;
}

.workSearchDivSearchPopContentItem:hover {
    background-color: #00bebd;
    color: white;
}

@media screen and (min-width: 1200px) {
    .workSearchDivContentSearchInput input {
        padding: 10px 20px;
        width: 35%;
        border: 1px solid #b8b8b8;
        font-size: 1rem;
        color: #343434;
        transition: .3s;
        font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    }


    .workSearchDivContentSearchInput button {
        width: 10%;
        background: var(--primary);
        box-sizing: border-box;
        height: 40.8px;
        font-size: 1rem;
        font-weight: 500;
        border: 0;
        cursor: pointer;
        color: #fff;
        padding: 0;
        transition: .3s;
    }

    .workSearchDivContentSearchInput button:hover {
        background: var(--primary-light);
    }

    .workSearchDivContentSearchTiaojianLeft {
        width: 20%;
        text-align: center;
        overflow-y: auto;
        border-right: 0.1px solid rgb(207, 204, 204);
    }

    .workSearchDivContentSearchTiaojianRight {
        width: 80%;
        display: flex;
    }

    .workSearchDivContentSearchTiaojianRightLeft {
        width: 25%;
        text-align: center;
        overflow-y: auto;
        border-right: 0.1px solid rgb(207, 204, 204);
    }

    .workSearchDivContentSearchTiaojianRightRight {
        width: 85%;
    }

    .workSearchDivContentSearchTiaojianRightRight li {
        display: inline-block;
        white-space: nowrap;
        font-size: 1rem;
        color: #333;
        line-height: 20px;
        cursor: pointer;
        margin: 10px;
    }
}

@media screen and (max-width: 1200px) {
    .workSearchDivContentSearchInput input {
        padding: 10px 20px;
        border: 1px solid #b8b8b8;
        font-size: 1rem;
        color: #343434;
        transition: .3s;
        font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
    }


    .workSearchDivContentSearchInput button {
        background: var(--primary);
        box-sizing: border-box;
        height: 40.8px;
        font-size: 1rem;
        font-weight: 500;
        border: 0;
        cursor: pointer;
        color: #fff;
        padding: 2px 10px;
        transition: .3s;
    }

    .workSearchDivContentSearchInput button:hover {
        background: var(--primary-light);
    }

    .workSearchDivContentSearchTiaojianLeft {
        width: 33%;
        text-align: center;
        overflow-y: auto;
        border-right: 0.1px solid rgb(207, 204, 204);
    }


    .workSearchDivContentSearchTiaojianRight {
        width: 67%;
        display: flex;
    }

    .workSearchDivContentSearchTiaojianRightLeft {
        width: 50%;
        text-align: center;
        overflow-y: auto;
        border-right: 0.1px solid rgb(207, 204, 204);
    }

    .workSearchDivContentSearchTiaojianRightRight {
        width: 50%;
    }


    .workSearchDivContentSearchTiaojianRightRight ul {
        padding-left: 0;
    }

    .workSearchDivContentSearchTiaojianRightRight li {
        display: inline-block;
        white-space: nowrap;
        font-size: 1rem;
        color: #333;
        line-height: 20px;
        cursor: pointer;
        margin: 10px;
    }
}
</style>