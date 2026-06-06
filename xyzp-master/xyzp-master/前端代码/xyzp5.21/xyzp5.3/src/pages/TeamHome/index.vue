<template>
    <div class="teamHomeDiv">
        <div class="teamHomeDivMain">
            <div class="teamHomeDivMainItem1">
                <div @click="choiceFun(1)" :class="[myChoice == 1 ? 'teamHomeDivMainItemA' : 'teamHomeDivMainItemB']">
                    <span>部门信息</span>
                </div>
                <div @click="choiceFun(2)" :class="[myChoice == 2 ? 'teamHomeDivMainItemA' : 'teamHomeDivMainItemB']">
                    <span>在招职位</span>
                </div>
                <div @click="choiceFun(3)" :class="[myChoice == 3 ? 'teamHomeDivMainItemA' : 'teamHomeDivMainItemB']">
                    <span>简历管理</span>
                </div>
                <div @click="choiceFun(4)" :class="[myChoice == 4 ? 'teamHomeDivMainItemA' : 'teamHomeDivMainItemB']">
                    <span>面试安排</span>
                </div>
                <div @click="choiceFun(5)" :class="[myChoice == 5 ? 'teamHomeDivMainItemA' : 'teamHomeDivMainItemB']">
                    <span>录用管理</span>
                </div>
            </div>
            <div class="teamHomeDivMainItem2">
                <div class="teamHomeDivMainItem2MyInfo" v-show="myChoice == 1">
                    <TeamInfo></TeamInfo>
                </div>
                <div class="teamHomeDivMainItem2MyJob" v-show="myChoice == 2">
                    <JobItem></JobItem>
                </div>
                <div class="teamHomeDivMainItem2MyJianli" v-show="myChoice == 3">
                    <CvItem></CvItem>
                </div>
                <div class="teamHomeDivMainItem2MyJianli" v-show="myChoice == 4">
                    <InterviewItem></InterviewItem>
                </div>
                <div class="teamHomeDivMainItem2MyJianli" v-show="myChoice == 5">
                    <AdmissionItem></AdmissionItem>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import CvItem from './CvItem'
import JobItem from './JobItem'
import TeamInfo from './TeamInfo'
import AdmissionItem from './AdmissionItem'
import InterviewItem from './InterviewItem'

export default {
    name: "TeamHome",
    components: {
        CvItem,
        JobItem,
        TeamInfo,
        AdmissionItem,
        InterviewItem
    },
    data() {
        return {
            myChoice: 1,
        }
    },
    mounted() {
        // 首次进入，请求团队信息
        this.choiceFun(this.myChoice)
    },
    methods: {
        // 左侧栏选择
        choiceFun(idx) {
            this.myChoice = idx
            // 发送请求
            if(idx == 1) this.$bus.$emit("getTeamInfo")
            else if(idx == 2) this.$bus.$emit("getTeamJobInfo")
            else if(idx == 3) this.$bus.$emit("getTeamUserResumeInfo")
            else if(idx == 4) this.$bus.$emit("getInterviewInfo")
            else if(idx == 5) this.$bus.$emit("getTeamUserAdmission")
        }
    },
}
</script>

<style>
.teamHomeDivMainItemA {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: rgb(248, 246, 246);
    color: #61687c;
}

.teamHomeDivMainItemB {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: white;
    color: #61687c;
}

.teamHomeDivMainItemC {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: white;
    color: #61687c;
}


.teamHomeDivMainItem2MyJianli {
    width: 90%;
    margin: auto;
    padding-top: 20px;
}

.teamHomeDivMainItem2MyJob {
    width: 90%;
    margin: auto;
    padding-top: 20px;
}

.teamHomeDivMainItem2MyInfo {
    width: 90%;
    margin: auto;
    padding-top: 20px;
}

@media screen and (min-width: 1200px) {

    .teamHomeDivMain {
        width: 90%;
        margin: auto;
        margin-top: 20px;
        display: flex;
        align-items: flex-start;
    }

    .teamHomeDivMainItem1 {
        width: 19%;
        background-color: white;
        font-size: 1rem;
        margin-right: 1%;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }

    .teamHomeDivMainItem2 {
        width: 80%;
        background-color: white;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }
}

@media screen and (min-width: 1200px) {
    .teamHomeDivMain {
        width: 90%;
        margin: auto;
        margin-top: 20px;
        display: flex;
        align-items: flex-start;
    }

    .teamHomeDivMainItem1 {
        width: 19%;
        background-color: white;
        font-size: 1rem;
        margin-right: 1%;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }

    .teamHomeDivMainItem2 {
        width: 100%;
        background-color: white;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }
}

@media screen and (max-width: 1200px) {
    .teamHomeDivMain {
        width: 100%;
        margin: auto;
        margin-top: 20px;
    }

    .teamHomeDivMainItem1 {
        width: 100%;
        background-color: white;
        font-size: 1rem;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }
}
</style>