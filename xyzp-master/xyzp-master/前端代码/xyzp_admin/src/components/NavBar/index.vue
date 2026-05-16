<template>
    <div class="navBarDiv" ref="nav">
        <div class="navBarTitle">
<!--            <img src="@/assets/logo4.jpg" alt="">-->
            <span>后台管理</span>
        </div>
        <ul class="navBarUl">
            <li>
                <div :class="[choiceNum == 1 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(1)">
                    <span class="iconfont icon-shouye"></span>
                    <span>首页</span>
                </div>
            </li>
            <li>
                <div :class="[choiceNum == 2 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(2)">
                    <span class="iconfont icon-jurassic_user"></span>
                    <span>用户管理</span>
                </div>
            </li>
            <li>
                <div :class="[choiceNum == 3 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(3)">
                    <span class="iconfont icon-tuandui"></span>
                    <span>部门管理</span>
                </div>
            </li>
            <li>
                <div :class="[choiceNum == 4 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(4)">
                    <span class="iconfont icon-pipeizhiwei"></span>
                    <span>职位管理</span>
                </div>
            </li>
            <li>
                <div :class="[choiceNum == 5 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(5)">
                    <span class="iconfont icon-shenhe"></span>
                    <span>招聘审核</span>
                </div>
            </li>
          <li>
            <div :class="[choiceNum == 6 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(6)">
              <span class="iconfont icon-yonghu"></span>
              <span>管理员管理</span>
            </div>
          </li>

          <li>
            <div :class="[choiceNum == 7 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(7)">
              <span class="iconfont icon-jianli"></span>
              <span>求职简历</span>
            </div>
          </li>
          <li>
            <div :class="[choiceNum == 8 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(8)">
              <span class="iconfont icon-xingming"></span>
              <span>面试管理</span>
            </div>
          </li>
          <li>
            <div :class="[choiceNum == 9 ? 'navBarItemHover' : 'navBarItem']" @click="toRouter(9)">
              <span class="iconfont icon-zhiwei"></span>
              <span>录取入职</span>
            </div>
          </li>


        </ul>
        <div class="shoujiNav">
            <div class="shoujiNavTitle">
                <img src="@/assets/logo4.jpg" alt="">
            </div>
            <div class="shoujiBtn">
                <span :class="[idx % 2 == 1 ? 'iconfont icon-zhankaicaidan' : 'iconfont icon-shouqicaidan']"
                    @click="idx++"></span>
            </div>
        </div>
        <div :class="[idx % 2 == 0 ? 'shoujiNavShow' : 'shoujiNavNoShow']">
            <ul v-show="idx % 2 == 0">
              <li @click="$router.push('/main/index'), choiceNum = 1">首页</li>
              <li @click="$router.push('/main/userManager'), choiceNum = 2">用户管理</li>
              <li @click="$router.push('/main/teamManager'), choiceNum = 3">团队管理</li>
              <li @click="$router.push('/main/teamManager'), choiceNum = 4">职位管理</li>
              <li @click="$router.push('/main/teamJobManager'), choiceNum = 5">团队招聘信息审核</li>
              <li @click="$router.push('/main/adminManager'), choiceNum = 6">管理员管理</li>
              <li @click="$router.push('/main/resume'), choiceNum = 7">求职简历</li>
              <li @click="$router.push('/main/interview'), choiceNum = 8">面试管理</li>
              <li @click="$router.push('/main/admission'), choiceNum = 9">录取入职</li>
                <!-- <li v-show="isLogin"><img :src="userInfo.avatar" alt="" slot="reference"></li>
                <li v-show="isLogin"><span @click="tohome(userInfo.id)" class="iconfont icon-home-g">{{ myChoice == 0 ?
                    '用户中心' :
                    '团队中心' }}</span></li>
                <li v-show="isLogin"><span @click="logout" class="iconfont icon-tuichu">退出登录</span></li> -->
            </ul>
        </div>
    </div>
</template>

<script>
export default {
    name: "NavBar",
    data() {
        return {
            // 选择的页面标号
            choiceNum: 1,
            // 导航栏栏计数
            idx: 1,
            // 是否登录
            isLogin: true,
        };
    },
    mounted() {
    },
    methods: {
        // 跳转页面
        toRouter(idx) {
            // 若选择的与当前
            if (idx == this.choiceNum) return
            this.choiceNum = idx
            if (this.choiceNum == 1) this.$router.push("/main/index")
            else if (this.choiceNum == 2) this.$router.push("/main/userManager")
            else if (this.choiceNum == 3) this.$router.push("/main/teamManager")
            else if (this.choiceNum == 4) this.$router.push("/main/jobManager")
            else if (this.choiceNum == 5) this.$router.push("/main/teamJobManager")
            else if (this.choiceNum == 7) this.$router.push("/main/resume")
            else if (this.choiceNum == 8) this.$router.push("/main/interview")
            else if (this.choiceNum == 9) this.$router.push("/main/admission")
            else this.$router.push("/main/adminManager")
        },
    },
    // 监听，改变导航栏的选中
    watch: {
        $route(to, from) {
            if (this.$route.path == "/main/index") this.choiceNum = 1
            else if (this.$route.path == "/main/userManager") this.choiceNum = 2
            else if (this.$route.path == "/main/teamManager") this.choiceNum = 3
            else if (this.$route.path == "/main/jobManager") this.choiceNum = 4
            else if (this.$route.path == "/main/teamJobManager") this.choiceNum = 5
            else if (this.$route.path == "/main/adminManager") this.choiceNum = 6
            else if (this.$route.path == "/main/resume") this.choiceNum = 7
            else if (this.$route.path == "/main/interview") this.choiceNum = 8
            else if (this.$route.path == "/main/admission") this.choiceNum = 9
        }
    },
}
</script>

<style>
* {
    margin: 0;
}

.navBarUl {
    list-style: none;
    padding-left: 0;
}

.navBarTitle {
    height: 50px;
    color: white;
    font-size: 1.1rem;
    font-weight: 600;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
}

.navBarTitle img {
    height: 40px;
}

.navBarItem {
    display: flex;
    align-items: center;
    height: 50px;
    padding: 10px 20px;
    color: rgb(241, 231, 231);
    font-size: 1rem;
    cursor: pointer;
}

.navBarItemHover {
    display: flex;
    cursor: pointer;
    align-items: center;
    height: 50px;
    padding: 10px 20px;
    color: rgb(241, 231, 231);
    font-size: 1rem;
    border-left: 4px solid rgb(166, 166, 35);
    background-color: rgb(0, 21, 40);
}

.navBarDiv .iconfont {
    color: rgb(137, 139, 147);
    font-size: 25px;
    margin-right: 10px;
}

.navBarItem:hover {
    background-color: rgb(36, 46, 56);
}

.shoujiNavNoShow {
    position: absolute;
    transition: 0.8s;
}


@media screen and (min-width: 1200px) {
    .navBarDiv {
        width: 17%;
        height: 100vh;
        position: fixed;
        top: 0;
        background-color: rgb(48, 65, 86);
    }

    .shoujiNav {
        display: none;
    }

    .shoujiNavShow {
        display: none;
    }
}

@media screen and (max-width: 1199px) {
    .navBarTitle {
        display: none;
    }

    .navBarUl {
        display: none;
    }

    .shoujiNav {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 50px;
        box-shadow: 0 2px 6px rgba(50, 50, 93, .1);
    }

    .shoujiNavTitle {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.1rem;
        font-weight: 600;
        box-shadow: 0 2px 6px rgba(50, 50, 93, );
    }

    .shoujiNavTitle img {
        height: 50px;
    }

    .shoujiNavShow {
        top: 50px;
        width: 100%;
        position: absolute;
        background-color: #ffff;
        box-shadow: 0 2px 6px rgba(50, 50, 93, .1);
        transition: 0.8s;
        z-index: 30;
        transform: translateY(10px);
    }

    .shoujiNavShow ul {
        list-style: none;
        padding-left: 0;
    }

    .shoujiNavShow ul li {
        height: 40px;
        padding: 5px 20px;
        color: #353030;
        line-height: 40px;
        cursor: pointer;
    }

    .shoujiNavShow ul li:hover {
        color: #818686;
        background-color: #f1f6f6;
    }

    .shoujiBtn {
        transition: 0.3s;
        cursor: pointer;
    }

    .shoujiBtn .iconfont {
        font-size: 28px;
    }

    .shoujiNavLine {
        height: 8px;
        background-color: #edf0f0;
    }

}
</style>