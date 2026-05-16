<template>
    <div class="loginDiv">
        <div class="loginDivMain">
            <img src="@/assets/logo4.jpg" alt=""><br>
            <div class="loginDivMainTitle">
                <span>云才科技人事平台</span>
            </div>
            <div class="loginDivInput">
                <i class="iconfont icon-yonghu"></i>
                <input type="text" v-model="username">
            </div>
            <div class="loginDivInput">
                <i class="iconfont icon-icon2"></i>
                <input type="password" v-model="password" @keydown.enter="loginBtn">
            </div>
            <div class="loginDivBtn">
                <button @click="loginBtn">登录</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            // 登录表单
            username: "",
            password: "",
        }
    },
    methods: {
        // 登录
        async loginBtn() {
            if (!this.username.trim() || !this.password.trim()) {
                this.$message({
                    message: '请正确填写',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("getLogin", {
                username: this.username,
                password: this.password,
                myChoice: 2
            })
            if (res.code == 200) {
                this.$message({
                    message: '登录成功',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 获取管理员信息
                localStorage.setItem("token", res.data.token)
                console.log(res.data.token)
                this.adminInfo()
                // 跳转主页
                this.$router.push("/main/index")
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
        // 获取管理员信息
        async adminInfo() {
            let res = await this.$store.dispatch("getAdminInfo")
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
                // 无效token或者token出错
                localStorage.removeItem("token")
            }
        }
    },
}
</script>

<style>
* {
    margin: 0;
}

.loginDiv {
    height: 100%;
    width: 100%;
    position: fixed;
    background: #f0f2f5;
    background-image: url(./images/bg.svg);
    background-repeat: no-repeat;
    background-position: 50%;
    background-size: 100%;
    padding: 20px 0;
}

.loginDivMain {
    padding-top: 180px;
    padding-bottom: 50px;
    max-width: 350px;
    margin: auto;
    text-align: center;
}

.loginDivMain img {
    max-width: 300px;
    max-height: 100px;
}

.loginDivMainTitle {
    font-size: 26px;
    color: #666;
    margin-bottom: 20px;
    text-align: center;
    font-weight: 700;
}

.loginDivInput {
    position: relative;
    width: 350px;
    height: 45px;
    line-height: 45px;
}

.loginDivInput i {
    position: absolute;
    color: #939090;
    left: 27px;
}

.loginDivInput input {
    padding-top: 5px;
    padding-bottom: 5px;
    padding-left: 27px;
    height: 23px;
    width: 80%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.loginDivBtn {
    height: 50px;
    line-height: 50px;
}

.loginDivBtn button {
    padding: 5px 10px;
    height: 35px;
    font-size: 0.9rem;
    background: #1a3a5c;
    color: white;
    border: 0;
    width: 312px;
    text-align: center;
    cursor: pointer;
}

.loginDivBtn button:active {
    background-color: #534afa;
}
</style>