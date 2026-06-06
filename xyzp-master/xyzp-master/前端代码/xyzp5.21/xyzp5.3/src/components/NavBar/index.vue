<template>
    <div class="navBarDiv">
        <div class="navBarDivLogo">
            <img src="@/assets/logo4.jpg" alt="网站logo图片">
        </div>
        <div class="navBarContent">
            <div :class="[navChoice == 0 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/index'), navChoice = 0">
                <span>首页</span>
            </div>
            <div :class="[navChoice == 1 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/workhome'), navChoice = 1">
                <span>招聘信息</span>
            </div>
            <div :class="[navChoice == 2 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/about'), navChoice = 2">
                <span>关于我们</span>
            </div>
            <div :class="[navChoice == 3 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/product'), navChoice = 3">
                <span>产品服务</span>
            </div>
            <div :class="[navChoice == 4 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/news'), navChoice = 4">
                <span>新闻动态</span>
            </div>
            <div :class="[navChoice == 5 ? 'navBarContentItem1' : 'navBarContentItem']"
                @click="$router.push('/contact'), navChoice = 5">
                <span>联系我们</span>
            </div>
        </div>
        <div class="navBarDivEnd">
            <div class="navBarDivEndMore">
                <span :class="[showMoreList % 2 == 1 ? 'iconfont icon-zhankaicaidan' : 'iconfont icon-shouqicaidan']"
                    @click="showMoreList++"></span>
            </div>
            <div class="navBarDivEndBlack">
                <button class="nav-login-btn" @click="loginRegBtnVisable = true" v-show="!isLogin">登录 / 人才中心</button>
                <el-popover placement="top" width="1" trigger="hover" class="navBarDivEndBlackPop">
                    <div class="navBarDivEndBlackPop">
                        <span @click="tohome(userInfo.id)" class="iconfont icon-home-g">{{ myChoice == 0 ? '个人中心' :
                            '部门中心' }}</span>
                    </div>
                    <div class="navBarDivEndBlackPop">
                        <span @click="logout" class="iconfont icon-tuichu">退出登录</span>
                    </div>
                    <img :src="userInfo.avatar" alt="用户头像" slot="reference" v-show="isLogin">
                </el-popover>
            </div>
        </div>
        <el-dialog :visible.sync="loginRegBtnVisable" :append-to-body="true" custom-class="navDialog">
            <div class="navBarDivLoginRegDivContent">
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="text" placeholder="账号" v-model="username" @keyup.enter="submitLogin">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="password" placeholder="密码" v-model="password" @keyup.enter="submitLogin">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <button @click="submitLogin">登 录</button>
                </div>
            </div>
            <div class="navBarDivLoginRegDivContentBotom">
                <span @click="regBtnVisable = true">没有账号？立即注册</span>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="regBtnVisable" :append-to-body="true" custom-class="navDialog">
            <div class="navBarDivRegDivTop">
                <span>{{ myChoice == 0 ? "用户注册" : "部门注册" }}</span>
            </div>
            <div class="navBarDivLoginRegDivContent">
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="text" placeholder="账号，长度大于等于8位小于等于12位" v-model="username1" @keyup.enter="submitRegister">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="password" placeholder="密码，长度不少于8位" v-model="password1" @keyup.enter="submitRegister">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="password" placeholder="再次输入密码" v-model="password2" @keyup.enter="submitRegister">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="text" placeholder="邮箱" v-model="email" @keyup.enter="submitRegister">
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <input type="text" placeholder="验证码" v-model="code" @keyup.enter="submitRegister">
                    <div class="navBarDivLoginRegDivContentItemYouEmail">
                        <span v-show="!isGetCode" @click="sendEmail(), getTimeRun()">点击发送验证码</span>
                        <span v-show="isGetCode">{{ getTime }}秒后重试</span>
                    </div>
                </div>
                <div class="navBarDivLoginRegDivContentItem">
                    <button @click="submitRegister">注 册</button>
                </div>
            </div>
        </el-dialog>
        <!-- 移动端弹窗 -->
        <div :class="[showMoreList % 2 == 0 ? 'navBarDivEndMoreList' : 'navBarDivEndMoreList1']">
            <ul v-show="showMoreList % 2 == 0">
                <li @click="$router.push('/index'), navChoice = 0">首页</li>
                <li @click="$router.push('/workhome'), navChoice = 1">招聘信息</li>
                <li @click="$router.push('/about'), navChoice = 2">关于我们</li>
                <li @click="$router.push('/product'), navChoice = 3">产品服务</li>
                <li @click="$router.push('/news'), navChoice = 4">新闻动态</li>
                <li @click="$router.push('/contact'), navChoice = 5">联系我们</li>
                <li @click="loginRegBtnVisable = true" v-show="!isLogin">登录 / 人才中心</li>
                <div class="navBarDivEndMoreListUserLine" v-show="isLogin"></div>
                <li v-show="isLogin"><img :src="userInfo.avatar" alt="用户头像" slot="reference"></li>
                <li v-show="isLogin"><span @click="tohome(userInfo.id)" class="iconfont icon-home-g">{{ myChoice == 0 ?
                    '个人中心' :
                    '部门中心' }}</span></li>
                <li v-show="isLogin"><span @click="logout" class="iconfont icon-tuichu">退出登录</span></li>
            </ul>
        </div>
    </div>
</template>

<script>
export default {
    name: "NavBar",
    data() {
        return {
            // 是否展示更多列表
            showMoreList: 1,
            // 用来记录鼠标的位置
            cot: 0,
            search: "",
            // 导航栏的选择
            navChoice: 0,
            // 倒计时
            getTime: 60,
            // 是否发送验证码
            isGetCode: false,
            // 是否展示登陆框
            loginRegBtnVisable: false,
            // 是否展示注册框
            regBtnVisable: false,
            // 身份
            myChoice: 0,
            uuid: crypto.randomUUID(),
            username: "",
            password: "",
            username1: "",
            password1: "12345678",
            password2: "12345678",
            email: "783129571@qq.com",
            code: "",
            // 是否登录
            isLogin: 0,
        }
    },
    mounted() {
        /*this.judgeMyChoice()*/
        this.$bus.$on("getUserInfo", () => {
            this.getUserInfo()
        })
        this.judgeUserExit()
    },
    methods: {
        // 判断是否有登录对象
        /*judgeMyChoice() {
            if (localStorage.getItem("myChoice") == null) localStorage.setItem("myChoice", 0)
        },*/
        // 退出登录
        async logout() {
            // 发送请求
            let res = await this.$store.dispatch("logout")
            if (res.code == 200) {
                localStorage.removeItem("token")
                this.isLogin = false
                this.$message({
                    message: '退出成功',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.$router.push("/index")
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
        tohome(id) {
            if (this.myChoice == 0) this.$router.push(`/userhome/${id}`)
            else this.$router.push(`/teamhome/${id}`)
        },
        // 判断用户是否存在
        judgeUserExit() {
            var token = localStorage.getItem("token")
            if (token != null) {
              var savedMyChoice = localStorage.getItem("myChoice")
              if (savedMyChoice != null) {
                this.myChoice = savedMyChoice
              }
              this.getUserInfo()
              this.isLogin = 1
            }
            else {
                localStorage.removeItem('myChoice')
            }
        },
        // 判断邮箱格式
        judgeEmail() {
            if (this.email.match(/^\w+@\w+\.\w+$/i)) return 1
            else return 0
        },
        // 倒计时
        getTimeRun() {
            // 判断邮箱格式是否正确
            if (this.judgeEmail() != 1) {        
                return
            }
            this.isGetCode = true
            // 设置倒计时
            var timeCut = setInterval(() => {
                this.getTime--;
                if (this.getTime <= 0) {
                    this.getTime = 60
                    this.isGetCode = false
                    clearInterval(timeCut);
                }
            }, 1000);
        },
        // 发送邮件
        async sendEmail() {
            if (!this.email.trim()) {
                this.$message({
                    message: '邮箱不能为空',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 判断邮箱格式是否正确
            else if (this.judgeEmail() != 1) {    
                this.$message({
                    message: '邮箱格式不正确',
                    type: 'warning',
                    center: true,
                    duration: 1500
                }) 
                return
            }
            this.$message({
                message: '邮箱正在发送中',
                type: 'success',
                center: true,
                duration: 1500
            })
            let res = await this.$store.dispatch("getEmailCode", {
                email: this.email,
                uuid: this.uuid
            })
            if (res.code == 200) {
                this.$message({
                    message: '邮箱发送成功',
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
        },
        // 注册
        async submitRegister() {
            if (!this.username1.trim() || !this.password1.trim() || !this.password2.trim() || !this.email.trim() ||
                !this.code.trim()) {
                this.$message({
                    message: '请正确输入',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.username1.length < 8 || this.username1.length > 12) {
                this.$message({
                    message: '账号长度有误',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.password1.length < 8) {
                this.$message({
                    message: '密码长度有误',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.password1.trim() != this.password2.trim()) {
                this.$message({
                    message: '两次密码输入不一致',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("getRegister", {
                user: {
                    username: this.username1,
                    password: this.$md5(this.password1),
                    email: this.email,
                },
                uuid: this.uuid,
                myChoice: this.myChoice,
                code: this.code
            })
            if (res.code == 200) {
                this.$message({
                    message: "注册成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.regBtnVisable = false
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
        // 登录
        async submitLogin() {
            if (!this.username.trim() || !this.password.trim()) {
                this.$message({
                    message: '请正确输入',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch('getLogin', {
                username: this.username,
                password: this.$md5(this.password),
            })
            if (res.code == 200) {
                this.$message({
                    message: '登陆成功',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // token保存在本地
                localStorage.setItem("token", res.data.token)
                localStorage.setItem("myChoice", res.data.myChoice)
                this.myChoice = res.data.myChoice
                // 获取用户信息
                this.getUserInfo()
                this.isLogin = 1
                this.loginRegBtnVisable = false
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
        // 获取用户信息
        async getUserInfo() {
            let res = await this.$store.dispatch("getUserInfo", this.myChoice)
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
                if (res.msg == '身份失效，请重新登录') {
                    localStorage.removeItem("token")
                    this.isLogin = 0
                }
            }
        }

    },
    // 监听，改变导航栏的选中
    watch: {
        $route(to, from) {
            if (this.$route.path.match("/index")) this.navChoice = 0
            else if (this.$route.path.match("/workhome")) this.navChoice = 1
            else if (this.$route.path.match("/about")) this.navChoice = 2
            else if (this.$route.path.match("/product")) this.navChoice = 3
            else if (this.$route.path.match("/news")) this.navChoice = 4
            else if (this.$route.path.match("/contact")) this.navChoice = 5
            else this.navChoice = 0
        }
    },
    computed: {
        userInfo() {
            return this.$store.state.nav.userInfo || {}
        }
    },
    beforeDestroy() {
        this.$bus.$off("getUserInfo")
    }
}
</script>

<style>
* {
    margin: 0;
}

.thisblack-bgc {
    background-color: rgba(0, 0, 0, 0.5);
}

.navBarDiv {
    z-index: 100;
    position: fixed;
    height: var(--nav-height);
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    background: var(--bg-white);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
    backdrop-filter: blur(8px);
}

.navBarDivLogo {
    text-align: center;
    width: 12%;
    min-width: 120px;
}

.navBarDivLogo img {
    height: 38px;
}

.navBarContent {
    width: 55%;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 2px;
}

.navBarContentItem {
    text-align: center;
    height: var(--nav-height);
    line-height: var(--nav-height);
    cursor: pointer;
    padding: 0 12px;
    font-size: 15px;
    color: var(--text-secondary);
    transition: all 0.2s;
    position: relative;
    white-space: nowrap;
}

.navBarContentItem:hover {
    color: var(--primary-light);
}

.navBarContentItem1 {
    text-align: center;
    height: var(--nav-height);
    line-height: var(--nav-height);
    cursor: pointer;
    padding: 0 12px;
    font-size: 15px;
    font-weight: 600;
    color: var(--primary);
    position: relative;
    white-space: nowrap;
}

.navBarContentItem1::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 12px;
    right: 12px;
    height: 3px;
    background: var(--primary);
    border-radius: 2px;
}

.navBarDivEnd {
    width: 33%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 20px;
    gap: 10px;
}

.navBarDivEndBlack {
    display: flex;
    align-items: center;
    gap: 8px;
}

.navBarDivEndBlack img {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    cursor: pointer;
    border: 2px solid var(--border-light);
    transition: all 0.2s;
    object-fit: cover;
}

.navBarDivEndBlack img:hover {
    border-color: var(--primary-light);
    transform: scale(1.05);
}

.navBarDivEndBlackPop {
    text-align: center;
    font-size: 14px;
    line-height: 36px;
    font-weight: 500;
    cursor: pointer;
    height: 36px;
    padding: 0 12px;
    transition: background 0.15s;
}

.navBarDivEndBlackPop:hover {
    background-color: var(--bg-light);
}

.nav-login-btn {
    padding: 8px 20px;
    background: var(--primary);
    color: #fff;
    border: none;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: background 0.2s;
    white-space: nowrap;
}

.nav-login-btn:hover {
    background: var(--primary-light);
}

.navBarDivEndMore .iconfont {
    color: var(--text-secondary);
    cursor: pointer;
    font-size: 26px;
}

.navBarDivEndMore {
    display: none;
    transition: 0.3s;
}

/* 登录/注册弹窗样式 */
.navBarDivLoginRegDivTop {
    display: flex;
    align-items: center;
    padding: 0 6px 0 6px;
    height: 50px;
    margin-bottom: 20px;
    background-color: var(--bg-light);
}

.navBarDivLoginRegDivItem {
    cursor: pointer;
    transition: all .2s linear;
    width: 50%;
    text-align: center;
    height: 40px;
    font-size: 1.1rem;
    line-height: 40px;
    background-color: #ebdfdf;
    border-radius: 4px;
}

.navBarDivLoginRegDivItem1 {
    cursor: pointer;
    transition: all .2s linear;
    width: 50%;
    text-align: center;
    height: 40px;
    font-size: 1.1rem;
    line-height: 40px;
    border-radius: 4px;
    background-color: var(--bg-light);
}

.navBarDivLoginRegDivContent {
    margin: auto;
}

.navBarDivLoginRegDivContent .navBarDivLoginRegDivContentItem {
    width: 100%;
    height: 57px;
    line-height: 57px;
    text-align: center;
}

.navBarDivLoginRegDivContentItem input {
    caret-color: var(--primary);
    box-sizing: border-box;
    padding: 10px 20px;
    width: 80%;
    border: 1px solid #d1d5db;
    font-size: 1rem;
    color: var(--text-primary);
    transition: border 0.2s;
    font-family: var(--font-family);
    border-radius: 8px;
    outline: none;
}

.navBarDivLoginRegDivContentItem input:focus {
    border-color: var(--primary-light);
    box-shadow: 0 0 0 3px rgba(44, 82, 130, 0.1);
}

.navBarDivLoginRegDivContentItem button {
    width: 80%;
    background: var(--primary);
    border-radius: 8px;
    line-height: 46px;
    font-size: 1rem;
    font-weight: 500;
    border: none;
    cursor: pointer;
    color: #fff;
    padding: 0;
    transition: all .2s linear;
    letter-spacing: 2px;
}

.navBarDivLoginRegDivContentItem button:active {
    background: var(--primary-dark);
}

.navBarDivLoginRegDivContentBotom {
    text-align: center;
    color: var(--primary-light);
    cursor: pointer;
    font-size: 14px;
    padding: 8px 0 16px;
}

.navBarDivLoginRegDivContentItemYouEmail {
    position: absolute;
    top: 258px;
    right: 15%;
    cursor: pointer;
    font-size: 13px;
    color: var(--primary-light);
}

.navBarDivRegDivTop {
    font-size: 1.2rem;
    text-align: center;
    font-weight: 600;
    height: 40px;
    line-height: 40px;
    color: var(--text-primary);
}

.navBarDivEndBlack .el-input__prefix {
    line-height: 40px;
    padding: 0 1px 0 3px;
}

@media screen and (min-width: 1200px) {
    .navBarDivEndMore {
        display: none;
    }

    .navBarDivEndMoreList {
        display: none;
    }

    .navDialog {
        width: 30%;
    }
}

@media screen and (max-width: 1199px) {
    .navBarContent {
        display: none;
    }

    .navBarDivEndMore {
        display: block;
        margin-right: 20px;
        text-align: center;
        position: relative;
    }

    .navBarDivEndBlack {
        display: none;
    }

    .navBarDivEndMoreList {
        top: var(--nav-height);
        width: 100%;
        position: absolute;
        left: 0;
        background-color: var(--bg-white);
        box-shadow: var(--shadow-md);
        transition: 0.3s;
    }

    .navBarDivEndMoreList1 {
        position: absolute;
        transform: translateY(-10px);
        transition: 0.3s;
    }

    .navBarDivEndMoreList ul {
        padding-left: 0;
    }

    .navBarDivEndMoreList li {
        height: 44px;
        padding: 5px 24px;
        list-style: none;
        color: var(--text-secondary);
        line-height: 44px;
        cursor: pointer;
        font-size: 15px;
        border-bottom: 1px solid var(--border-light);
    }

    .navBarDivEndMoreList li:hover {
        color: var(--primary);
        background-color: var(--bg-light);
    }

    .navBarDivEndMoreList img {
        height: 40px;
        width: 40px;
        border-radius: 50%;
    }

    .navBarDivEndMoreListUserLine {
        height: 8px;
        background-color: var(--bg-light);
    }

    .navDialog {
        width: 92%;
    }
}
</style>