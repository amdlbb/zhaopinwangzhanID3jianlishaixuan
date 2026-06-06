<template>
    <div class="myInfoDiv">
        <div class="myInfoDivTitle">
            <span>我的信息</span>
        </div>
        <div class="myInfoDivImg">
            <vue-hover-mask>
                <el-upload :http-request="uploadImage" action="" :show-file-list="false" :limit="1"
                    :on-exceed="handleExceed" ref="userAvatar">
                    <img :src="userInfo.avatar" alt="点击修改图片">
                </el-upload>
                <template v-slot:action>
                    <span slot="default" @click="editAvatar">编辑</span>
                </template>
            </vue-hover-mask>

        </div>
        <div class="myInfoDivContentLine">
            <div class="myInfoDivContentItem">
                <div class="myInfoDivContentItemLabel">
                    <span class="iconfont icon-zhanghaoquanxianguanli">账号</span>
                </div>
                <div>
                    <input type="text" v-model="userInfo.username" disabled>
                </div>
            </div>
            <div class="myInfoDivContentItem">
                <div class="myInfoDivContentItemLabel">
                    <span class="iconfont icon-nicheng">昵称</span>
                </div>
                <div>
                    <input type="text" v-model="userInfo.nickname">
                </div>
            </div>
        </div>
        <div class="myInfoDivContentLine">
            <div class="myInfoDivContentItem">
                <div class="myInfoDivContentItemLabel">
                    <span class="iconfont icon-youxiang">邮箱（用于接收招聘信息）</span>
                </div>
                <div>
                    <input type="text" v-model="userInfo.email" disabled>
                </div>
            </div>
            <div class="myInfoDivContentItem" v-show="isMySelf">
                <div class="myInfoDivContentItemLabel">
                    <span class="iconfont icon-mima">密码</span>
                </div>
                <div>
                    <button @click="changPasswordDialogVisible = true">点击修改密码</button>
                </div>
            </div>
        </div>
        <div class="myInfoDivContentLineIntroduce">
            <div>
                <div class="myInfoDivContentItemLabel">
                    <span class="iconfont icon-renyuanjieshao">个人介绍</span>
                </div>
                <div>
                    <el-input type="textarea" autosize placeholder="不超过50个字" v-model="userInfo.introduce">
                    </el-input>
                </div>
            </div>
        </div>
        <div class="myInfoDivSave">
            <button @click="submitSave" v-show="isMySelf">保存</button>
        </div>
        <el-dialog title="修改密码" :visible.sync="changPasswordDialogVisible" width="30%" center  custom-class="dialogDiv">
            <div class="myInfoDivChangePasswordItem">
                <input type="password" placeholder="请输入原密码" v-model="myYuanPassword">
            </div>
            <div class="myInfoDivChangePasswordItem">
                <input type="password" placeholder="请输入修改后的密码" v-model="myNowPassword1">
            </div>
            <div class="myInfoDivChangePasswordItem">
                <input type="password" placeholder="请再次输入修改后的密码" v-model="myNowPassword2">
            </div>
            <div class="myInfoDivChangePasswordItemBtn">
                <button @click="submitChangPassword">修改</button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
export default {
    name: "MyInfo",
    data() {
        return {
            userInfo: {},
            isMySelf: false,
            changPasswordDialogVisible: false,
            myYuanPassword: "",
            myNowPassword1: "",
            myNowPassword2: "",
        }
    },
    mounted() {
        // 总线接收
        this.$bus.$on("getUserInfo", () => {
            this.init()
        })
    },
    methods: {
        // 点击上传图片事件
        editAvatar() {
            this.$refs["userAvatar"].$refs["upload-inner"].handleClick();
        },
        // 判断用户是否为本人
        init() {
            var pathId = this.$route.params.id
            var userInfo = this.$store.state.nav.userInfo
            if (pathId != userInfo.id) {
                this.isMySelf = false
                this.getUserInfo(pathId)
            }
            else{
                this.getUserInfo2(0)
                this.isMySelf = true
            } 
        },
        // 获取用户信息，非本人
        async getUserInfo(id) {
            let res = await this.$store.dispatch("getUserInfo2", {
                id: id
            })
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.userInfo = res.data
            }
        },
        // 获取用户信息，本人
        async getUserInfo2(id) {
            let res = await this.$store.dispatch("getUserInfo", id)
            if (res.code != 200) {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            else {
                this.userInfo = res.data
            }
        },
        // 修改用户资料
        async submitSave() {
            if (!this.userInfo.email || !this.userInfo.nickname || !this.userInfo.introduce) {
                this.$message({
                    message: '请正确填写',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("saveUserInfo", this.userInfo)
            if (res.code == 200) {
                this.$message({
                    message: '更新成功',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 重新请求用户信息
                res = await this.$store.dispatch("getUserInfo", 0)
                if (res.code != 200) {
                    this.$message({
                        message: res.msg,
                        type: 'error',
                        center: true,
                        duration: 1500
                    })
                }
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
        // 超出限制
        handleExceed(files, fileList) {
            this.$message({
                message: '只能选择一张图片',
                type: 'warning',
                center: true,
                duration: 1500
            })
        },
        // 上传成功
        async uploadImage(param) {
            const formData = new FormData()
            formData.append('images', param.file)
            formData.append('flag', 0)
            let res = await this.$store.dispatch("getUpload", formData);
            if (res.code == 200) {
                this.$message({
                    message: '修改头像成功',
                    type: 'success',
                    center: true,
                    duration: 1500
                })
                // 调用全局总线改变用户信息
                this.$bus.$emit("getUserInfo")
                setTimeout(() => {
                    // 重新渲染信息
                    this.init()
                }, 500)
            }
            else {
                this.$message({
                    message: res.msg,
                    type: 'error',
                    center: true,
                    duration: 1500
                })
            }
            this.avatar = res.data
        },
        // 修改密码
        async submitChangPassword() {
            if (this.myNowPassword1.trim() != this.myNowPassword2.trim()) {
                this.$message({
                    message: '两次输入的密码不一致',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if (this.myNowPassword1.length < 8) {
                this.$message({
                    message: '密码长度不少于8位',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            else if(this.myNowPassword1 == this.myYuanPassword){
                this.$message({
                    message: '新旧密码未发生改变',
                    type: 'warning',
                    center: true,
                    duration: 1500
                })
                return
            }
            // 发送请求
            let res = await this.$store.dispatch("getChangUserPassword", {
                beforePassword: this.$md5(this.myYuanPassword),
                nowPassword:  this.$md5(this.myNowPassword1)
            })
            if (res.code == 200) {
                this.$message({
                    message: '修改密码成功',
                    type: 'warning',
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
    beforeDestroy(){
        // 撤销总线
        this.$bus.$off("getUserInfo")
    }
}
</script>

<style>
.myInfoDivTitle {
    font-size: 1.2rem;
    height: 40px;
    color: rgb(69, 67, 67);
    border-bottom: 0.1px solid rgb(223, 219, 219);
}

.myInfoDivImg {
    height: 150px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.myInfoDivImg img {
    height: 100px;
    border-radius: 50%;
    cursor: pointer;
}

.myInfoDivImg .vue-hover-mask_action {
    border-radius: 50%;
}

.myInfoDivContentLine {
    display: flex;
    align-items: center;
    height: 70px;
}

.myInfoDivContentItem button {
    padding: 5px;
    font-size: 0.9rem;
    border-radius: 3px;
    background: #1a3a5c;
    color: white;
    border: 0;
    width: 67%;
    text-align: center;
    cursor: pointer;
}

.myInfoDivContentItem button:active {
    background: rgb(49, 73, 234);
}

.myInfoDivContentLineIntroduce {
    width: 100%;
    margin-top: 7px;
}

.myInfoDivContentLineIntroduce div {
    width: 100%;
}

.myInfoDivContentLineIntroduce textarea {
    padding: 5px 10px;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myInfoDivContentLineIntroduce textarea:focus {
    border: 2px solid black;
    overflow-y: hidden;
}

.myInfoDivContentItem {
    width: 50%;
}

.myInfoDivContentItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myInfoDivContentItem textarea {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myInfoDivContentItemLabel {
    border-left: 4px solid rgb(189, 189, 251);
    padding-left: 5px;
    font-size: 1rem;
    color: rgb(112, 80, 121);
    margin-bottom: 2px;
}

.myInfoDivSave {
    text-align: center;
    height: 50px;
    margin: 10px 0 10px 0;
}

.myInfoDivSave button {
    padding: 5px;
    height: 30px;
    font-size: 0.9rem;
    background: #1a3a5c;
    color: white;
    border: 0;
    width: 20%;
    border-radius: 3px;
    text-align: center;
    cursor: pointer;
}

.myInfoDivSave button:active {
    background: rgb(49, 73, 234);
}

.myInfoDivChangePasswordItem {
    height: 50px;
    line-height: 50px;
    text-align: center;
}

.myInfoDivChangePasswordItem input {
    padding: 5px 10px;
    width: 60%;
    border: 1px solid #b8b8b8;
    font-size: 1rem;
    color: #1f1f1f;
    transition: .3s;
    font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.myInfoDivChangePasswordItemBtn {
    text-align: center;
}

.myInfoDivChangePasswordItemBtn button {
    padding: 5px;
    height: 30px;
    font-size: 0.9rem;
    border-radius: 3px;
    background: #a6a0d9;
    color: white;
    border: 0;
    width: 20%;
    text-align: center;
    cursor: pointer;
}

.myInfoDivChangePasswordItemBtn button:active {
    background: rgb(145, 145, 234);
}

@media screen and (min-width: 1200px) {
    .dialogDiv {
        width: 30% !important;
    }
}

@media screen and (max-width: 1200px) {
    .dialogDiv {
        width: 95% !important;
    }
}
</style>