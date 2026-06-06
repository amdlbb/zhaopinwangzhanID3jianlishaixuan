<template>
    <div class="chatItem">
        <button @click="submitChat" class="chatItemButton">立即沟通</button>
        <el-dialog title="聊天" :visible.sync="chatDialog" width="35%" :modal="false" custom-class="dialogDiv">
            <div style="overflow:auto; border: 1px solid #ccc" class="chatItemDialogItemContent" v-html="content">
            </div>
            <div class="chatItemDialogItem">
                <el-input type="textarea" placeholder="点击开始沟通" v-model="text" rows="6">
                </el-input>
            </div>
            <div class="chatItemDialogItem">
                <el-button type="primary" @click="send">发送</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
let socket
export default {
    name: "ChatItem",
    data() {
        return {
            showPop: false,
            messages: [],
            content: "",
            chatUser: this.$route.params.id,
            text: '',
            username: localStorage.getItem('username'),
            chatDialog: false
        }
    },
    created() {
        this.init()
    },
    methods: {
        // 提交简历
        submitChat() {
            this.chatDialog = true
        },
        // 初始化
        init() {
            let _this = this
            // 检查历览器是否支持websocket
            if (typeof (WebSocket) == "undefined") {
                console.log("您的浏览器不支持WebSocket");
            }
            else {
                let socketUrl = "ws://localhost:8081/imserver/" + this.username
                // 先检查socket是否已经有开启，若开启先关闭
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
                // 开启一个websocket服务
                socket = new WebSocket(socketUrl);
                //打开事件
                socket.onopen = function () {
                    console.log("websocket已打开")
                }
                //  浏览器端收消息，获得从服务端发送过来的文本消息
                socket.onmessage = function (msg) {
                    console.log("收到数据====" + msg.data)
                    let data = JSON.parse(msg.data)
                    if (data.users) {

                    }
                    else {
                        // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
                        //  // {"from": "zhang", "text": "hello"}
                        if (data.from === _this.chatUser) {
                            _this.messages.push(data)
                            // 构建消息内容
                            _this.createContent(data.from, null, data.text)
                        }
                    }

                }

            }
        },
        // 创建连接
        createContent(remoteUser, nowUser, text) {
            let html
            if (nowUser) {
                html = ` <div class="chatItemDialogItemMe">
                    <div>${text}</div>
                    <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="chatItemDialogItemMeAvatar">
                    
                </div>`
            }
            else if (remoteUser) {
                html = `<div class="chatItemDialogItemOther">
                    <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="chatItemDialogItemMeAvatar">
                    <div>${text}</div>
                </div>`
            }
            this.content += html + '<br>'
            
        },
        // 发送数据
        send() {
            if (!this.chatUser) {
                this.$message({ type: 'warning', message: "请选择聊天对象" })
                return;
            }
            if (!this.text) {
                this.$message({ type: 'warning', message: "请输入内容" })
            } else {
                if (typeof (WebSocket) == "undefined") {
                    console.log("您的浏览器不支持WebSocket");
                } else {
                    console.log("您的浏览器支持WebSocket");
                    // 组装待发送的消息 json
                    // {"from": "zhang", "to": "admin", "text": "聊天文本"}
                    let message = { from: this.username, to: this.chatUser, text: this.text }
                    socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
                    this.messages.push({ user: this.username, text: this.text })
                    // 构建消息内容，本人消息
                    this.createContent(null, this.username, this.text)
                    this.text = '';
                }
            }
        },
    }
}
</script>

<style>
.chatItemButton {
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

.chatItemDialogItem {
    margin: 10px 0 10px 0;
}

.chatItemDialogItemMe {
    display: flex;
    align-items: center;
    justify-content: end;
}

.chatItemDialogItemMe div {
    margin-right: 2%;
    background: rgb(109, 109, 109);
    color: white;
    padding: 6px;
}

.chatItemDialogItemOther div {
    margin-left: 2%;
    background: rgb(106, 73, 73);
    color: white;
    padding: 6px;

}

.chatItemDialogItemOther {
    display: flex;
    align-items: center;
    justify-content: start;
}

.chatItemDialogItemMeAvatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
}

.chatItemDialogItemContent {
    margin: 0 0 10px 0;
    height: 250px;
}

.chatItemDialog{
    overflow-y: hidden;
}

/* 设置滚动条样式 */
.chatItemDialogItemContent::-webkit-scrollbar {
    width: 3px;
    height: 3px;
}

.chatItemDialogItemContent::-webkit-scrollbar-track {
    background-color: transparent;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

.chatItemDialogItemContent::-webkit-scrollbar-thumb {
    background-color: rgb(147, 147, 153, 0.5);
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
}

@media screen and (min-width: 1200px) {
    .dialogDiv{
        width: 35% !important;
    }

}

@media screen and (max-width: 1200px) {
    .dialogDiv{
        width: 95% !important;
    }

}
</style>