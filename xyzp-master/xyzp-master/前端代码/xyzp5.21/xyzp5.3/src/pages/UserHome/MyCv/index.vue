<template>
    <div class="myCvDiv">
        <div class="myCvDivTitle">
            <div class="myCvDivTitleFirst">
                <span>我的简历</span>
            </div>
        </div>
        <div class="myCvDivMain">
            <div>
                <div class="myCvDivContentLine">
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-xingming">姓名</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.name">
                        </div>
                    </div>
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-xingbie">性别</span>
                        </div>
                        <div class="myCvDivContentItemChoice">
                            <div @click="myCvInfo.sex = '男'"
                                :class="[myCvInfo.sex == '男' ? 'myCvDivContentItemChoice1' : 'myCvDivContentItemChoice2']">
                                <span>男</span>
                            </div>
                            <div @click="myCvInfo.sex = '女'"
                                :class="[myCvInfo.sex == '女' ? 'myCvDivContentItemChoice1' : 'myCvDivContentItemChoice2']">
                                <span>女</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="myCvDivContentLine">
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-nianling">年龄</span>
                        </div>
                        <div>
                            <input type="number" v-model="myCvInfo.age">
                        </div>
                    </div>
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-dianhua">电话</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.phone">
                        </div>
                    </div>
                </div>
                <div class="myCvDivContentLine">
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-xuexiao">毕业院校</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.school">
                        </div>
                    </div>
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-xueli">学历</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.education">
                        </div>
                    </div>
                </div>
                <div class="myCvDivContentLine">
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-zhuanyezhuanyeke">专业</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.major">
                        </div>
                    </div>
                </div>
                <div class="myCvDivContentLine">
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-zhiwei">期望职位</span>
                        </div>
                        <div>
                            <input type="text" v-model="myCvInfo.exceptionJob">
                        </div>
                    </div>
                    <div class="myCvDivContentItem">
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-xinzi">期望薪资</span>
                        </div>
                        <div>
                            <input v-model="myCvInfo.exceptionSalary">
                        </div>
                    </div>
                </div>
                <div class="myCvDivContentLine1">
                    <div>
                        <div class="myCvDivContentItemLabel">
                            <span class="iconfont icon-jianli">详细介绍</span>
                        </div>
                        <div>
                            <el-input type="textarea" autosize placeholder="" v-model="myCvInfo.content">
                            </el-input>
                        </div>
                    </div>
                </div>
                <div class="myCvDivSave">
                    <button @click=submitSaveUserResume>保存</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "MyCv",
    data() {
        return {
            name: "",
            textarea1: "",
            myCvInfo: {
                id: "",
                name: "",
                content: "",
                education: "",
                exceptionJob: "",
                exceptionSalary: "",
                major: "",
                age: 0,
                phone: "",
                school: "",
                sex: "",
            }
        }
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getUserResume", () => {
            this.init()
        })
    },
    methods: {
        async init() {
            let res = await this.$store.dispatch("getUserResume")
            if (res.code == 200) {
                this.myCvInfo = res.data
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
        // 更新用户简历
        async submitSaveUserResume() {
            if(!this.myCvInfo.phone || this.myCvInfo.phone.length != 11){
                this.$message({
                    message: '请正确填写电话号码',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if(this.myCvInfo.age < 0){
                this.$message({
                    message: '请正确填写年龄',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("getUpdateUserResume", this.myCvInfo)
            if (res.code == 200) {
                this.$message({
                    message: '修改用户简历成功',
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
        // 撤销总线
        this.$bus.$off("getUserResume")
    }
}
</script>

<style>
.myCvDivTitle {
    height: 40px;
    border-bottom: 0.1px solid rgb(223, 219, 219);
    display: flex;
}

.myCvDivTitleFirst {
    width: 80%;
    color: rgb(69, 67, 67);
    font-size: 1.2rem;
}

.myCvDivTitleSecond {
    width: 20%;
}

.myCvDivTitleSecond button {
    background-color: #1a3a5c;
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    width: 70%;
    border-radius: 2px;
    cursor: pointer;
}

.myCvDivTitleSecond button:active {
    background-color: rgb(49, 73, 234);
}

.myCvDivContentLine {
    display: flex;
    align-items: center;
    height: 70px;
}

.myCvDivContentLine1 {
    margin-top: 7px;
    margin-bottom: 20px;
}

.myCvDivContentLine1 div {
    width: 100%;
}

.myCvDivContentLine1 textarea {
    padding: 5px 10px;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .1;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myCvDivContentLine1 textarea:focus {
    border: 2px solid black;
    overflow-y: hidden;
}

.myCvDivContentItem {
    width: 50%;
}

.myCvDivContentItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myCvDivContentItemLabel {
    border-left: 4px solid rgb(189, 189, 251);
    padding-left: 5px;
    font-size: 1rem;
    color: rgb(112, 80, 121);
    margin-bottom: 2px;
}

.myCvDivContentItemChoice {
    display: flex;
    align-items: center;
}

.myCvDivContentItemChoice2 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(255, 255, 255);
    height: 40px;
    cursor: pointer;
}

.myCvDivContentItemChoice1 {
    width: 50%;
    text-align: center;
    line-height: 40px;
    background-color: rgb(185, 190, 194);
    height: 40px;
    cursor: pointer;
}

.myCvDivMain li {
    display: inline-block;
    white-space: nowrap;
    font-size: 1rem;
    color: #333;
    width: 25%;
}

.myCvDivMainItem {
    height: 100px;
    background-color: #f3eded;
    width: 90%;
    margin: auto;
    margin-top: 10px;
    margin-bottom: 10px;
    border-radius: 2px;
}

.myCvDivMainItemTitle {
    font-size: 1.1rem;
    font-weight: 500;
    height: 100px;
    line-height: 100px;
    color: #333;
    text-align: center;
}

.createCvVisableDiv {
    text-align: center;
    margin: 20px;
}

.createCvVisableDiv input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.createCvVisableDiv button {
    background-color: #1a3a5c;
    color: white;
    font-size: 0.8rem;
    height: 35px;
    border: 0;
    width: 70%;
    border-radius: 2px;
    cursor: pointer;
}

.createCvVisableDiv button:active {
    background-color: rgb(49, 73, 234);
}

.myCvDivMainItem .vue-hover-mask {
    display: block;
    background-color: #f3eded;
}

.myCvDivMainItemHoverBottom {
    margin-top: 70px;
    display: flex;
    align-items: center;
}

.myCvDivMainItemHoverBottomItem {
    width: 50%;
    margin: auto;
}

.myCvDivMainItemHoverBottomItem:hover {
    color: #b8b8b8;
}

.myCvDivSave {
    text-align: center;
    height: 50px;
    margin: 10px 0 10px 0;
}

.myCvDivSave button {
    padding: 5px;
    height: 30px;
    font-size: 0.9rem;
    background: #1a3a5c;
    color: white;
    border: 0;
    width: 20%;
    text-align: center;
    cursor: pointer;
}

.myCvDivSave button:active {
    background: rgb(88, 88, 241);
}
</style>