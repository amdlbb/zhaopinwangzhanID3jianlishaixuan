<template>
    <div class="workItemDiv">
        <div class="workItemDivTop">
            <div class="workItemDivTopMain">
                <div class="workItemDivTopPart">
                    <div class="workItemDivTopName">
                        <span>{{ workhomeData.name }}</span>
                    </div>
                    <div class="workItemDivTopInfo">
                        <div>
                            <span>薪资：{{ workhomeData.salary }}</span>
                        </div>
                        <div>
                            <span>学历：{{ workhomeData.education }}</span>
                        </div>
                        <div>
                            <span>工作地点：{{ workhomeData.address }}</span>
                        </div>
                    </div>

                </div>
                <div class="workItemDivTopPart">
                    <!-- <div class="workItemDivTopPartMore">
                        <div><span>收藏</span></div>
                        <div><span>完善简历</span></div>
                    </div> -->
                    <div class="workItemDivTopPartBtn">
                        <ChatItem class="workItemDivTopPartBtnItem"></ChatItem>
                        <div class="workItemDivTopPartBtnItem">
                            <button class="workItemDivTopPartBtnItemBtn" @click="submitDeliver">投递简历</button>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="workItemDivContent">
            <div class="workItemDivContentMain">
                <div class="workItemDivContentMainDiv">
                    <div class="workItemDivContentMainDivJianli">
                        <div class="workItemDivContentMainTop">
                            <span>
                                职业描述
                            </span>
                        </div>
                        <div class="workItemDivContentMainDivContent">
                            <span>
                                {{ workhomeData.content }}
                            </span>
                        </div>
                    </div>
                    <div class="workItemDivContentMainCompany">
                        <div class="workItemDivContentMainTop">
                            <span>
                                公司基本信息
                            </span>
                        </div>
                        <div class="workItemDivContentMainCompanyImg">
                            <div class="workItemDivContentMainCompanyImgImg">
                                <img :src="workhomeData.avatar" alt="" width="50px">
                            </div>
                            <div class="workItemDivContentMainCompanyImgName">
                                <span>{{ workhomeData.nickname }}</span>
                            </div>
                        </div>
                        <div :class="[isIntroduceShow % 2 == 1 ? 'workItemDivContentMainCompanyIntroduce' : 'workItemDivContentMainCompanyIntroduce1']"
                            ref="comapnyIntroduce">
                            <span>
                                {{ workhomeData.introduce }}
                            </span>
                        </div>
                        <div class="workItemDivContentMainCompanyZankai">
                            <span @click="isIntroduceShow++">{{ isIntroduceShow % 2 == 1 ? "展开" : "收起" }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <el-dialog title="我的简历" :visible.sync="deliverVisable" width="35%" custom-class="dialogDiv">
            <div>
                <div class="workItemDivContentLine">
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-xingming">姓名</span>
                        </div>
                        <div>
                            <input type="text" v-model="workHomeUserResumeInfo.name" disabled>
                        </div>
                    </div>
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-xingbie">性别</span>
                        </div>
                        <div class="workItemDivContentItemChoice">
                            <div
                                :class="[workHomeUserResumeInfo.sex == '男' ? 'workItemDivContentItemChoice1' : 'workItemDivContentItemChoice2']">
                                <span>男</span>
                            </div>
                            <div
                                :class="[workHomeUserResumeInfo.sex == '女' ? 'workItemDivContentItemChoice1' : 'workItemDivContentItemChoice2']">
                                <span>女</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="workItemDivContentLine">
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-nianling">年龄</span>
                        </div>
                        <div>
                            <input type="number" v-model="workHomeUserResumeInfo.age" disabled>
                        </div>
                    </div>
                </div>
                <div class="workItemDivContentLine">
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-xuexiao">毕业院校</span>
                        </div>
                        <div>
                            <input type="text" v-model="workHomeUserResumeInfo.school" disabled>
                        </div>
                    </div>
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-xueli">学历</span>
                        </div>
                        <div>
                            <input type="text" v-model="workHomeUserResumeInfo.education" disabled>
                        </div>
                    </div>
                </div>
                <div class="workItemDivContentLine">
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-zhiwei">期望职位</span>
                        </div>
                        <div>
                            <input type="text" v-model="workHomeUserResumeInfo.exceptionJob" disabled>
                        </div>
                    </div>
                    <div class="workItemDivContentItem">
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-xinzi">期望薪资</span>
                        </div>
                        <div>
                            <input type="text" v-model="workHomeUserResumeInfo.exceptionSalary" disabled>
                        </div>
                    </div>
                </div>
                <div class="workItemDivContentLine1">
                    <div>
                        <div class="workItemDivContentItemLabel">
                            <span class="iconfont icon-jianli">详细介绍</span>
                        </div>
                        <div>
                            <el-input type="textarea" autosize placeholder="" v-model="workHomeUserResumeInfo.content"
                                disabled>
                            </el-input>
                        </div>
                    </div>
                </div>
                <div class="workItemDivDiagBtn">
                    <button @click="saveDeliver">确认投递</button>
                </div>
                <div class="workItemDivDiagTips">
                    <span @click="toUserHome">完善简历</span>
                </div>
            </div>

        </el-dialog>
    </div>
</template>

<script>
import ChatItem from '../ChatItem'
export default {
    name: "WorkItem",
    components: {
        ChatItem
    },
    data() {
        return {
            // 简历信息
            workhomeData: {
                content: "",
                introduce: ""
            },
            // 是否展示介绍更多界面            
            isIntroduceShow: 1,
            // 是否展示投递界面
            deliverVisable: false,
            sex: 1,
            // 展示的用户简历信息
            workHomeUserResumeInfo: {}
        }
    },
    mounted() {
        // 获取招聘信息
        this.getWrokhomData()
    },
    methods: {
        // 提交简历
        async submitDeliver() {
            if (localStorage.getItem("token") == undefined) {
                this.$message({
                    message: "请先登录",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("getUserResume")
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (res.data == null) {
                this.$message({
                    message: "团队不能投递",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            this.workHomeUserResumeInfo = res.data
            this.deliverVisable = true
        },
        // 初始化获取数据
        async getWrokhomData() {
            let res = await this.$store.dispatch('getTeamJobInfo', {
                id: this.$route.params.id
            })
            if (res.code == 200) {
                this.workhomeData = res.data
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
        // 完善简历
        toUserHome() {
            var userId = this.$store.state.nav.userInfo.id || 0
            this.$router.push(`/userhome/${userId}`)
        },
        // 投递简历
        async saveDeliver() {
            let res = await this.$store.dispatch("saveUserDeliver", {
                teamJobId: this.$route.params.id
            })
            if (res.code == 200) {
                this.$message({
                    message: "投递成功",
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
        }

    },

}
</script>

<style>
.workItemDivTop {
    height: 250px;
    background-color: rgb(58, 83, 106);
}

.workItemDivTopPart {
    width: 50%;
}

.workItemDivTopMain {
    width: 80%;
    margin: auto;
    display: flex;
    align-items: center;
    height: 250px;
}

.workItemDivTopInfo {
    color: white;
}

.workItemDivDiagTips {
    font-size: 0.9rem;
    color: gray;
    cursor: pointer;
}


.workItemDivContentMainTop {
    font-size: 1.2rem;
    font-weight: 600;
    height: 40px;
    line-height: 40px;
    border-bottom: 2px solid var(--primary);
    display: inline-block;
    margin-bottom: 12px;
}

.workItemDivTopPartMore {
    display: flex;
    align-items: center;
    margin: 10px 0 10px 0;
}

.workItemDivTopPartMore div {
    width: 50%;
    text-align: center;
}

.workItemDivContentMainDivContent {
    color: rgb(51, 51, 51);
    white-space: pre-line;
}

.workItemDivContentMainCompanyImg {
    display: flex;
    align-items: center;
}

.workItemDivContentMainCompanyImg img {
    width: 60px;
    border-radius: 50%;
}

.workItemDivContentMainCompanyImg .workItemDivContentMainCompanyImgImg {
    width: 25%;
    margin: auto;
}

.workItemDivContentMainCompanyImgName {
    font-size: 1rem;
    color: rgb(51, 51, 51);
    width: 75%;
}

.workItemDivContentMainCompanyMore {
    text-align: center;
    margin-top: 20px;
}

.workItemDivContentMainCompanyMore button {
    width: 80%;
    background: #1a3a5c;
    height: 30px;
    border-radius: 3px;
    font-size: 1rem;
    font-weight: 500;
    border: none;
    cursor: pointer;
    color: #fff;
    padding: 0;
    transition: all .2s linear;
}

.workItemDivContentMainCompanyMore button:active {
    background-color: rgb(49, 73, 234);
}

.workItemDivContentMainCompanyIntroduce {
    color: rgb(51, 51, 51);
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    overflow: hidden;
}

.workItemDivContentMainCompanyIntroduce1 {
    white-space: pre-line;
}

.workItemDivContentMainCompanyZankai {
    cursor: pointer;
    float: right;
    color: rgb(4, 100, 203);
}

.workItemDivContentLine {
    display: flex;
    align-items: center;
    height: 70px;
}

.workItemDivContentLine1 {
    margin-top: 7px;
    margin-bottom: 20px;
}

.workItemDivContentLine1 div {
    width: 100%;
}

.workItemDivContentLine1 textarea {
    padding: 5px 10px;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .1;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.workItemDivContentLine1 textarea:focus {
    border: 2px solid black;
    overflow-y: hidden;
}

.workItemDivContentItem {
    width: 50%;
}

.workItemDivContentItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.workItemDivContentItemLabel {
    border-left: 4px solid rgb(189, 189, 251);
    padding-left: 5px;
    font-size: 1rem;
    color: rgb(112, 80, 121);
    margin-bottom: 2px;
}

.workItemDivContentItemChoice2 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(255, 255, 255);
    height: 40px;
    cursor: pointer;
}

.workItemDivContentItemChoice1 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(185, 190, 194);
    height: 40px;
    cursor: pointer;
}

.workItemDivContentItemChoice {
    display: flex;
    align-items: center;
}

.workItemDivDiagBtn {
    height: 50px;
    text-align: center;
    line-height: 50px;
    font-size: 1rem;
}

.workItemDivDiagBtn button {
    color: white;
    background-color: #1a3a5c;
    font-weight: 500;
    border: none;
    border-radius: 3px;
    height: 37px;
    padding: 10px;
    cursor: pointer;
}

.workItemDivDiagBtn button:active {
    background: rgb(49, 73, 234);
}


.workItemDivTopPartBtnItemBtn {
    width: 60%;
    background: #1a3a5c;
    height: 50px;
    border-radius: 8px;
    font-size: 1.1rem;
    font-weight: 500;
    border: none;
    cursor: pointer;
    color: #fff;
    padding: 0;
    transition: all .2s linear;
}

@media screen and (min-width: 1200px) {
    .workItemDivContentMain {
        width: 80%;
        margin: auto;
    }

    .workItemDivContentMainDiv {
        margin-top: 20px;
        border-radius: 10px;
        display: flex;
        align-items: flex-start;
    }

    .workItemDivContentMainDivJianli {
        width: 69%;
        padding: 20px;
        box-shadow: 0 2px 6px rgb(0 0 0 / 28%);
    }

    .workItemDivContentMainCompany {
        padding: 20px;
        margin-left: 1%;
        box-shadow: 0 2px 6px rgb(0 0 0 / 28%);
        width: 30%;
        transition: all 2s linear;
    }

    .workItemDivTopPartBtn {
        text-align: center;
        display: flex;
        align-items: center;
    }

    .workItemDivTopPartBtnItem {
        width: 50%;
    }

    .dialogDiv {
        width: 35% !important;
    }

    .workItemDivTopName {
        font-size: 1.8rem;
        font-weight: 600;
        color: #fff;
        height: 70px;
        line-height: 70px;
    }

}

@media screen and (max-width: 1200px) {
    .workItemDivContentMain {
        width: 100%;
        margin: auto;
    }

    .workItemDivContentMainDiv {
        margin-top: 20px;
        border-radius: 10px;
    }

    .workItemDivContentMainDivJianli {
        padding: 20px;
        box-shadow: 0 2px 6px rgb(0 0 0 / 28%);
    }


    .workItemDivContentMainCompany {
        padding: 20px;
        box-shadow: 0 2px 6px rgb(0 0 0 / 28%);
        transition: all 2s linear;
    }

    .workItemDivTopPartBtn {
        text-align: center;
    }

    .workItemDivTopPartBtnItem {
        width: 100%;
        margin-bottom: 5px;
        margin-top: 5px;
    }

    .dialogDiv {
        width: 95% !important;
    }

    .workItemDivTopName {
        font-size: 1.4rem;
        font-weight: 600;
        color: #fff;
        height: 70px;
        line-height: 70px;
    }

}
</style>