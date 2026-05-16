<template>
    <div class="navTopDiv">
        <div class="navTopDivRoute">
            <span>云才科技人事平台 </span>
            <span>/ {{ ItemTitle }} </span>
        </div>
        <div class="navTopDivUser">
            <el-dropdown placement="bottom" @command="handleCommand">
                <span class="el-dropdown-link">
                    <span>{{ adminInfo.username }}</span>
                    <span class="iconfont icon-xiangxia"></span>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="a">个人信息</el-dropdown-item>
                    <el-dropdown-item command="c">修改信息</el-dropdown-item>
                    <el-dropdown-item command="b">退出后台</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <el-dialog title="管理员信息" :visible.sync="showMoreInfo" width="30%" custom-class="dialogDiv" append-to-body>
            <div class="showMoreLine">
                <div class="showMoreLine1">
                    <span>账号</span>
                </div>
                <div class="showMoreLine2">
                    <span>{{ adminInfo.username }}</span>
                </div>
            </div>
            <div class="showMoreLine">
                <div class="showMoreLine1">
                    <span>邮箱</span>
                </div>
                <div class="showMoreLine2">
                    <span>{{ adminInfo.email }}</span>
                </div>
            </div>
        </el-dialog>

        <el-dialog title="修改信息" :visible.sync="isUpdate" width="30%" custom-class="dialogDiv" append-to-body>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>账号</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="username" disabled class="tableInput">
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>邮箱</span>
                </div>
                <div class="tableLine2">
                    <input type="text" v-model="email" disabled class="tableInput">
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>原密码</span>
                </div>
                <div class="tableLine2">
                    <input type="password" v-model="beforPassword" class="tableInput">
                </div>
            </div>
            <div class="tableLine">
                <div class="tableLine1">
                    <span>修改后的密码</span>
                </div>
                <div class="tableLine2">
                    <input type="password" v-model="password" class="tableInput">
                </div>
            </div>
            <div class="tableBtnLine">
                <button class="addBtn" @click="updateAdminInfo">修改</button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "NavTop",
    mounted() {
        this.isLogin()
    },
    data() {
        return {
            // 是否展示管理员信息
            showMoreInfo: false,
            // 修改信息
            username: "",
            password: "",
            email: "",
            beforPassword: "",
            // 是否展示修改信息
            isUpdate: false
        }
    },
    methods: {
        // 判断是否登录
        async isLogin() {
            var token = localStorage.getItem("token")
            if (token != null) {
                let res = await this.$store.dispatch("getAdminInfo")
                if (res.code != 200) {
                    this.$message({
                        message: res.msg,
                        type: 'error',
                        center: true,
                        duration: 1500
                    })
                    // 无效token
                    localStorage.removeItem("token")
                    this.$router.push("/login")
                }
            }
            else this.$router.push("/login")
        },
        // 退出
        async logout() {
            console.log(11)
            let res = await this.$store.dispatch("logout")
            if (res.code == 200) {
                this.$message({
                    message: "退出成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 删除token
                localStorage.removeItem("token")
                this.$router.push("/login")
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
        // 处理顶部的下拉菜单事件
        handleCommand(command) {
            if (command == 'a') {
                this.showMoreInfo
                this.showMoreInfo = true
            }
            else if (command == 'b') {
                this.logout()
            }
            else if (command == 'c') {
                var updateAdminInfo = this.$store.state.login.adminInfo
                this.username = updateAdminInfo.username
                this.email = updateAdminInfo.email
                this.isUpdate = true
            }
        },
        // 更新管理员信息
        async updateAdminInfo() {
            if (!this.username.trim() || !this.password.trim() || !this.email.trim() || !this.beforPassword.trim()) {
                this.$message({
                    message: "请正确填写",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.password.length < 8) {
                this.$message({
                    message: "密码长度不少于8位",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.password != this.beforPassword) {
                this.$message({
                    message: "两次密码不一致",
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            let res = await this.$store.dispatch("getUpdateAdminInfo", {
                username: this.username,
                password: this.$md5(this.password),
                email: this.email
            })
            if (res.code == 200) {
                this.$message({
                    message: "修改成功",
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                this.isLogin()
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
        ItemTitle() {
            if (this.$route.path == '/main/userManager') return "用户管理"
            else if (this.$route.path == '/main/teamManager') return "团队管理"
            else if (this.$route.path == '/main/jobManager') return "职位管理"
            else if (this.$route.path == '/main/teamJobManager') return "团队招聘信息审核管理"
            else if (this.$route.path == '/main/adminManager') return "管理员管理"
            else if (this.$route.path == '/main/resume') return "求职简历"
            else if (this.$route.path == '/main/interview') return "面试管理"
            else if (this.$route.path == '/main/admission') return "录取入职"
            else return "首页"
        },
        adminInfo() {
            return this.$store.state.login.adminInfo || {}
        }
    }

}
</script>

<style>
.navTopDivRoute {
    text-align: left;
    color: rgb(72, 87, 106);
    margin-left: 30px;
}

.navTopDivUser {
    text-align: center;
    margin-right: 30px;
    cursor: pointer;
}

.el-dropdown-link {
    color: rgb(64, 158, 255);
}

.showMoreLine {
    display: flex;
    margin-top: 5px;
    margin-bottom: 5px;
}

.showMoreLine1 {
    width: 35%;
}

.showMoreLine2 {
    width: 65%;
}

.tableLine {
    display: flex;
    align-items: center;
    margin-top: 5px;
    margin-bottom: 5px;
}

.tableLine1 {
    width: 35%;
}

.tableLine2 {
    width: 65%;
}

.tableBtnLine {
    text-align: center;
}

.tableInput {
    padding-top: 5px;
    padding-bottom: 5px;
    height: 23px;
    width: 80%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.addBtn {
    padding: 7px 15px;
    font-size: 0.9rem;
    background: #968ced;
    color: white;
    border: 0;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

@media screen and (min-width: 1200px) {
    .navTopDiv {
        width: 83%;
        right: 0;
        height: 60px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        position: fixed;
        background: #fff;
        box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
        z-index: 30;
    }

    .dialogDiv {
        width: 35% !important;
    }


}


@media screen and (max-width: 1199px) {
    .navTopDiv {
        height: 60px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        background: #fff;
        box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
        z-index: 1;
    }

    .dialogDiv {
        width: 95% !important;
    }

}
</style>
