<template>
    <div class="homeDiv">
        <NavTop></NavTop>
        <NavHistory></NavHistory>
        <div class="mainContent">
            <div class="mainContentTitle">
                <span>概览</span>
            </div>
            <div class="countDiv">
                <div class="countAllDiv">
                    <div class="countAllDivTitle">
                        <span>用户数</span>
                    </div>
                    <div class="countAllDivNum">
                        <span>{{ homeData.userNum }}</span>
                    </div>
                </div>

                <div class="countAllDiv">
                    <div class="countAllDivTitle">
                        <span>团队数</span>
                    </div>
                    <div class="countAllDivNum">
                        <span>{{ homeData.teamNum }}</span>
                    </div>
                </div>

                <div class="countAllDiv">
                    <div class="countAllDivTitle">
                        <span>职位数</span>
                    </div>
                    <div class="countAllDivNum">
                        <span>{{ homeData.jobNum }}</span>
                    </div>
                </div>

                <div class="countAllDiv">
                    <div class="countAllDivTitle">
                        <span>招聘审核数</span>
                    </div>
                    <div class="countAllDivNum">
                        <span>{{ homeData.examineNum }}</span>
                    </div>
                </div>
            </div>
            <div class="echartsDiv">
                <div class="mychart" id="mychart"></div>
                <div class="mychart1" id="mychart1"></div>
            </div>
        </div>
    </div>
</template>

<script>
import NavTop from '@/components/NavTop'
import NavHistory from "@/components/NavHistory"
import * as echarts from "echarts"
export default {
    name: "Home",
    components: {
        NavTop,
        NavHistory
    },
    data() {
        return {
            // 数据
            homeData: {}
        }
    },
    mounted() {
        this.init()
    },
    methods: {
        // 初始化
        async init() {
            let res = await this.$store.dispatch("getIndexInfo")
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.homeData = res.data
                this.drawLine(res.data)
            }
        },
        // 图表
        drawLine(data) {
            // 基于准备好的dom，初始化echarts实例
            let mychart = echarts.init(document.getElementById('mychart'))
            let mychart1 = echarts.init(document.getElementById('mychart1'))

            // 绘制图表
            mychart.setOption({
                title: { text: '数据饼状图', left: "center" },
                series: [{
                    name: '数量',
                    type: 'pie',
                    data: [
                        {
                            value: data.userNum,
                            name: "用户数量"
                        },
                        {
                            value: data.teamNum,
                            name: "团队数量"
                        },
                        {
                            value: data.jobNum,
                            name: "职位数量"
                        },
                        {
                            value: data.examineNum,
                            name: "审核数量"
                        }
                    ]
                }]
            });

            // 绘制图表
            mychart1.setOption({
                title: { text: '数据柱状图', left: "center" },
                xAxis: {
                    type: '',
                    data: ['用户数量', '团队数量', '职位数量', '审核数量']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: [data.userNum, data.teamNum, data.jobNum, data.examineNum],
                    type: 'bar'
                }]
            })
        }
    },
}
</script>

<style>
.countAllDiv {
    width: 200px;
    height: 100px;
    background-color: #f9f9f9;
    border: #f0f0f0 1px solid;
    border-radius: 3px;
}

.countAllDivTitle {
    font-size: 1rem;
    font-weight: 600;
    color: rgb(153, 153, 153);
    text-align: center;
    height: 40px;
    line-height: 40px;
}

.countAllDivNum {
    text-align: center;
    font-size: 1.5rem;
    color: rgb(32, 165, 58);
}

.mainContentTitle {
    font-size: 1rem;
    color: #666;
    height: 40px;
    line-height: 40px;
    margin-left: 10px;
}

.countDiv {
    display: flex;
    justify-content: space-around;
}

@media screen and (min-width: 1200px) {
    .homeDiv {
        width: 83%;
        margin-left: 17%;
    }

    .mainContent {
        width: 95%;
        margin: auto;
        height: 100vh;
        margin-top: 120px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }

    .echartsDiv {
        margin-top: 50px;
        display: flex;
    }


    .mychart {
        height: 400px;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 50%;

    }

    .mychart1 {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 400px;
        width: 50%;
    }


}

@media screen and (max-width: 1199px) {
    .homeDiv {
        width: 100%;
    }

    .mainContent {
        width: 100%;
        margin: auto;
        margin-top: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    }

    .countAllDiv {
        width: 200px;
        height: 100px;
        background-color: #f9f9f9;
        border: #f0f0f0 1px solid;
        border-radius: 3px;
    }

    .echartsDiv {
        margin-top: 30px;
    }


    .mychart {
        height: 400px;
        display: flex;
        justify-content: center;
        align-items: center;

    }

    .mychart1 {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 400px;
    }

}
</style>