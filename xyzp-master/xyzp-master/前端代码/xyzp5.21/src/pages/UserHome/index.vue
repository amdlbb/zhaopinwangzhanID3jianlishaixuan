<template>
    <div class="userHomeDiv">
        <div class="userHomeDivMain">
            <div class="userHomeDivMainItem1">
                <div @click="chocieFun(1)" :class="[myChoice == 1 ? 'userHomeDivMainItemA' : 'userHomeDivMainItemB']">
                    <span>我的信息</span>
                </div>
                <div @click="chocieFun(2)" :class="[myChoice == 2 ? 'userHomeDivMainItemA' : 'userHomeDivMainItemB']">
                    <span>我的简历</span>
                </div>
                <div @click="chocieFun(3)" :class="[myChoice == 3 ? 'userHomeDivMainItemA' : 'userHomeDivMainItemB']">
                    <span>我的投递</span>
                </div>
                <div @click="chocieFun(4)" :class="[myChoice == 4 ? 'userHomeDivMainItemA' : 'userHomeDivMainItemB']">
                    <span>我的录取</span>
                </div>
            </div>
            <div class="userHomeDivMainItem2">
                <div class="userHomeDivMainItem2Item" v-show="myChoice == 1">
                    <MyInfo></MyInfo>
                </div>
                <div class="userHomeDivMainItem2Item" v-show="myChoice == 2">
                    <MyCv></MyCv>
                </div>
                <div class="userHomeDivMainItem2Item" v-show="myChoice == 3">
                    <MyDeliver></MyDeliver>
                </div>
                <div class="userHomeDivMainItem2Item" v-show="myChoice == 4">
                    <MyAdmission></MyAdmission>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MyCv from './MyCv'
import MyInfo from './MyInfo'
import MyDeliver from './MyDeliver'
import MyAdmission from "./MyAdmission"

export default {
    name: "UserHome",
    components: {
        MyCv,
        MyInfo,
        MyDeliver,
        MyAdmission
    },
    data() {
        return {
            myChoice: 1,
        }
    },
    mounted() {
        // 首次进入，请求用户信息
        this.chocieFun(this.myChoice)
    },
    methods: {
        // 左侧栏选择
        chocieFun(idx){
            this.myChoice = idx
            // 发送请求
            if(idx == 1) this.$bus.$emit("getUserInfo")
            else if(idx == 2) this.$bus.$emit("getUserResume")
            else if(idx == 3) this.$bus.$emit("getUserDeliver")
            else if(idx == 4) this.$bus.$emit("getUserAdmission")
        }
    },
}
</script>

<style>
.userHomeDivMainItemA {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: rgb(248, 246, 246);
    color: #61687c;
}

.userHomeDivMainItemB {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: white;
    color: #61687c;
}

.userHomeDivMainItemC {
    height: 50px;
    line-height: 50px;
    text-align: center;
    cursor: pointer;
    background-color: white;
    color: #61687c;
}

.userHomeDivMainItem2Item {
    width: 90%;
    margin: auto;
    padding-top: 20px;
}

@media screen and (min-width: 1200px) {
    .userHomeDivMain {
        height: 100%;
        width: 90%;
        margin: auto;
        margin-top: 20px;
        display: flex;
        align-items: flex-start;
    }

    .userHomeDivMainItem1 {
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
        width: 19%;
        background-color: white;
        font-size: 1rem;
        margin-right: 1%;
    }

    .userHomeDivMainItem2 {
        background-color: white;
        width: 80%;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }
}

@media screen and (max-width: 1200px) {
    .userHomeDivMain {
        height: 100%;
        width: 100%;
        margin: auto;
        margin-top: 20px;
    }

    .userHomeDivMainItem1 {
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
        width: 100%;
        background-color: white;
        font-size: 1rem;
        margin-bottom: 10px;
    }

    .userHomeDivMainItem2 {
        background-color: white;
        width: 100%;
        box-shadow: 0 6px 9px rgba(50, 50, 93, .1), 0 5px 7px rgb(0 0 0/7%);
    }
}
</style>