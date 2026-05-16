<template>
  <div class="teamDiv">
    <NavTop></NavTop>
    <NavHistory></NavHistory>
    <div class="mainContent">
      <div class="mainContentSearch">
        <span class="iconfont icon-sousuo"></span>
        <input type="text" placeholder="账号搜索" @keydown.enter="search" v-model="searchContent">
      </div>
      <div class="mainContentTable">
        <div class="mainContentTableBtn">
          <ul>
            <li>
              <el-button type="primary" style="width: 100px; height: 35px;" @click="showAddTeam = true">新增部门
              </el-button>
            </li>
            <li>
              <button class="addBtn" @click="showReset = true">重置密码</button>
            </li>
            <li>
              <button class="moveBtn" @click="remove">删除</button>
            </li>
            <li v-show="multipleSelection.length > 0" class="showChoiceNum"><span>已经选择{{
                multipleSelection.length
              }}个</span></li>
          </ul>
        </div>
        <el-table ref="multipleTable" :data="tableData" style="width: 100%"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="username" label="账号" width="160">
          </el-table-column>
          <el-table-column prop="nickname" label="团队名称" width="250">
          </el-table-column>
          <el-table-column label="团队头像" width="150">
            <template slot-scope="scope">
              <img :src="scope.row.avatar" alt="" width="50px" height="50px">
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址" width="350">
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <span v-show="scope.row.status == 1" class="main-table-active">激活</span>
              <span v-show="scope.row.status == 0" class="main-table-remove">禁用</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="150">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="showMore(scope.row)">查看更多</el-button>
              <el-button type="text" size="small" @click="changeTeam(scope.row)">修改</el-button>

              <el-button type="text" size="small" @click="changeStatus(scope.row.id)" v-show="scope.row.status == 0">
                激活
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="mainPag">
          <el-pagination background layout="prev, pager, next" :total="totalNum"
                         @current-change="handleCurrentChange" :current-page="page" :page-size="10">
          </el-pagination>
        </div>
      </div>
    </div>

    <el-dialog title="更多信息" :visible.sync="teamMoreInfo" width="30%" custom-class="dialogDiv">
      <div class="tableLine">
        <div class="tableLine1">
          <span>ID</span>
        </div>
        <div class="tableLine2">
          <span>{{ showInfo.id }}</span>
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>邮箱</span>
        </div>
        <div class="tableLine2">
          <span>{{ showInfo.email }}</span>
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>团队介绍</span>
        </div>
        <div class="tableLine2">
          <span>{{ showInfo.introduce }}</span>
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>创建时间</span>
        </div>
        <div class="tableLine2">
          <span>{{ showInfo.createTime }}</span>
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>更新时间</span>
        </div>
        <div class="tableLine2">
          <span>{{ showInfo.updateTime }}</span>
        </div>
      </div>
    </el-dialog>
    <!--      注册组件-->
    <el-dialog :visible.sync="showAddTeam" :append-to-body="true" custom-class="navDialog">
      <div class="navBarDivRegDivTop">
        <span>{{ "新增部门" }}</span>
      </div>
      <div class="navBarDivLoginRegDivContent">
        <div class="navBarDivLoginRegDivContentItem">
          <input type="text" placeholder="名称" v-model="username1" @keyup.enter="submitRegister">
        </div>
        <div class="navBarDivLoginRegDivContentItem">
          <input type="password" placeholder="密码长度不少于8位" v-model="password1" @keyup.enter="submitRegister">
        </div>
        <div class="navBarDivLoginRegDivContentItem">
          <input type="password" placeholder="再次输入密码" v-model="password2" @keyup.enter="submitRegister">
        </div>
        <div class="navBarDivLoginRegDivContentItem">
          <input type="text" placeholder="邮箱" v-model="email" @keyup.enter="submitRegister">
        </div>
        <div class="navBarDivLoginRegDivContentItem">
          <input type="text" placeholder="为确保邮箱可用，请输入验证码" v-model="code" @keyup.enter="submitRegister">
          <div class="navBarDivLoginRegDivContentItemYouEmail">
            <span v-show="!isGetCode" @click="sendEmail(), getTimeRun()">点击发送验证码</span>
            <span v-show="isGetCode">{{ getTime }}秒后重试</span>
          </div>
        </div>
        <div class="navBarDivLoginRegDivContentItem">
          <button @click="submitRegister">注册</button>
        </div>
      </div>
    </el-dialog>

   <!--修改部门信息-->
    <el-dialog title="编辑信息" :visible.sync="showChangeTeam" width="30%" custom-class="dialogDiv">
      <div class="tableLine">
        <div class="tableLine1">
          <span>ID</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.id" disabled class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>账号</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.username" disabled class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>团队名称</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.nickname" class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>邮箱</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.email" class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>地址</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.address" class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>部门简介</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="editInfo.introduce" class="tableInput">
        </div>
      </div>
      <div class="tableBtnLine">
        <button class="addBtn" @click="update">修改</button>
      </div>
    </el-dialog>



    <el-dialog title="提示" :visible.sync="showReset" width="30%">
      <span style="font-size: 1rem;">确认重置密码为：</span>
      <span style="color: red;">12345678</span>
      <span slot="footer" class="dialog-footer">
                <el-button @click="showReset = false">取 消</el-button>
                <el-button type="primary" @click="reset">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import NavTop from '@/components/NavTop'
import NavHistory from "@/components/NavHistory"

export default {
  name: "TeamManager",
  components: {
    NavTop,
    NavHistory
  },
  data() {
    return {
      // 表格数据
      tableData: [],
      // 多选的数据
      multipleSelection: [],
      // 页数
      page: 1,
      pageSize: 10,
      // 总页数
      totalNum: 0,
      // 是否展示更多团队信息
      teamMoreInfo: false,
      // 展示的信息
      showInfo: {},
      // 搜索内容
      searchContent: "",
      // 展示重置页面
      showReset: false,


      // 展示新增团队表单
      showAddTeam: false,
      uuid: crypto.randomUUID(),
      username: "",
      password: "",
      username1: "",
      password1: "12345678",
      password2: "12345678",
      email: "783129571@qq.com",
      // 倒计时
      getTime: 60,
      // 是否发送验证码
      isGetCode: false,
      myChoice: 1,
      code: "",
      // 修改团队相关
      changeTeamId: null,
      showChangeTeam: false,
      editInfo: {},
    }
  },
  mounted() {
    // 初始化
    this.init()
  },
  methods: {
    // 勾选行
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 初始化
    async init() {
      let res = await this.$store.dispatch("getTeamInfo", {
        page: this.page,
        pageSize: this.pageSize
      })
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      } else {
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
      }
    },

    // 修改团队信息
    async update() {
      if (!this.editInfo.email  ) {
        this.$message({
          message: '邮箱不能为空',
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      }
      let res = await this.$store.dispatch("saveTeamInfo", this.editInfo)
      if (res.code == 200) {
        this.$message({
          message: '更新成功',
          type: 'success',
          center: true,
          duration: 1500
        })
        this.showChangeTeam = false
        this.init()
      } else {
        this.$message({
          message: res.msg,
          type: 'warning',
          center: true,
          duration: 1500
        })
      }
    },


    changeTeam(data){
      console.log(data)
      this.changeTeamId = data.id
      this.showChangeTeam = true
      this.editInfo = data
    },

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
      } else if (this.username1.length < 0 || this.username1.length > 12) {
        this.$message({
          message: '账号长度有误',
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      } else if (this.password1.length < 8) {
        this.$message({
          message: '密码长度有误',
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      } else if (this.password1.trim() != this.password2.trim()) {
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
      } else {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }
      this.init()
      this.showAddTeam = false
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
    // 判断邮箱格式
    judgeEmail() {
      if (this.email.match(/^\w+@\w+\.\w+$/i)) return 1
      else return 0
    },
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
      try {
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
      } catch (error) {
        // 静默处理所有错误，不显示任何错误提示
        console.log('邮件发送请求完成（忽略错误）');
      }
    },

    // 更换页数
    async handleCurrentChange(val) {
      let res = await this.$store.dispatch("getTeamInfo", {
        page: val,
        pageSize: 10
      })
      if (res.code == 200) {
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
      } else {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }
    },
    // 展示更多
    showMore(data) {
      this.showInfo = data
      this.teamMoreInfo = true
    },
    // 搜索
    async search() {
      if (!this.searchContent.trim()) {
        this.$message({
          message: "搜索内容不能为空",
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      }
      let res = await this.$store.dispatch("getSearch", {
        content: this.searchContent
      })
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      } else {
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
      }
    },
    // 添加
    async add() {

    },
    // 删除
    async remove() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "还未选择",
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      }
      // 发送请求
      const idxList = this.multipleSelection.map(item => item.id)
      let res = await this.$store.dispatch("adminRemoveTeam", idxList)
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      } else {
        this.$message({
          message: "删除成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.init()
      }
    },
    // 恢复状态
    async changeStatus(id) {
      let res = await this.$store.dispatch("getAdminReturnTeam", [id])
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      } else {
        this.$message({
          message: "激活成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.init()
        // 关闭弹窗
        this.teamMoreInfo = false
      }
    },
    // 重置密码
    async reset() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "还未选择",
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      }
      // 发送请求
      const idxList = this.multipleSelection.map(item => item.id)
      let res = await this.$store.dispatch("getAdminResetTeam", idxList)
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      } else {
        this.$message({
          message: "重置密码成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.showReset = false
        this.init()
      }
    }
  },
}
</script>

<style>

/* 对话框遮罩背景 */
.thisblack-bgc {
  background-color: rgba(0, 0, 0, 0.5);
}

/* 对话框自定义类 */
.navDialog {
  width: 30%; /* 在大屏幕下的宽度 */
}

/* 对话框顶部标题区域 */
.navBarDivRegDivTop {
  font-size: 1.2rem;
  text-align: center;
  font-size: 600;
  height: 30px;
  margin-bottom: 20px;
}

/* 对话框内容容器 */
.navBarDivLoginRegDivContent {
  margin: auto;
}

/* 对话框中的每个表单项 */
.navBarDivLoginRegDivContent .navBarDivLoginRegDivContentItem {
  width: 100%;
  height: 57px;
  line-height: 57px;
  text-align: center;
  margin-bottom: 10px; /* 添加一些间距 */
}

/* 输入框样式 */
.navBarDivLoginRegDivContentItem input {
  caret-color: #4e6ef2;
  box-sizing: border-box;
  padding: 10px 20px;
  width: 80%;
  border: 1px solid #b8b8b8;
  font-size: 1.1rem;
  color: #1f1f1f;
  transition: .3s;
  font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
  border-radius: 8px;
  outline: none; /* 移除聚焦时的默认轮廓 */
}

/* 输入框聚焦状态 */
.navBarDivLoginRegDivContentItem input:focus {
  border-color: #4e6ef2;
  box-shadow: 0 0 0 2px rgba(78, 110, 242, 0.1);
}

/* 按钮样式 */
.navBarDivLoginRegDivContentItem button {
  width: 80%;
  background: linear-gradient(90deg, #00bebd, #01a7a7);
  border-radius: 8px;
  line-height: 48px;
  font-size: 1.1rem;
  font-weight: 500;
  border: none;
  cursor: pointer;
  color: #fff;
  padding: 0;
  transition: all .2s linear;
  margin-top: 10px;
}

/* 按钮悬停状态 */
.navBarDivLoginRegDivContentItem button:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 190, 189, 0.2);
}

/* 按钮激活状态 */
.navBarDivLoginRegDivContentItem button:active {
  background: linear-gradient(90deg, #03cfcf, #05b5b5);
  transform: translateY(0);
  box-shadow: 0 2px 6px rgba(0, 190, 189, 0.2);
}

/* 验证码部分样式 */
.navBarDivLoginRegDivContentItemYouEmail {
  position: absolute;
  top: 313px; /* 注意：这个值是固定的，可能需要根据实际情况调整 */
  right: 18%; /* 这个百分比值也需要适配 */
  cursor: pointer;
  color: rgb(0, 34, 253);
  font-size: 0.9rem;
  z-index: 1;
}

/* 验证码发送按钮状态 */
.navBarDivLoginRegDivContentItemYouEmail span:hover {
  text-decoration: underline;
  color: #3a56c7;
}

/* 顶部标签切换样式（用户注册/团队注册） */
.navBarDivLoginRegDivTop {
  display: flex;
  align-items: center;
  padding: 0 6px 0 6px;
  height: 50px;
  margin-bottom: 20px;
  background-color: rgb(248, 248, 248);
  border-radius: 8px 8px 0 0;
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
  border-color: .1px solid #5e6262;
  border-radius: 2px;
}

.navBarDivLoginRegDivItem1 {
  cursor: pointer;
  transition: all .2s linear;
  width: 50%;
  text-align: center;
  height: 40px;
  font-size: 1.1rem;
  line-height: 40px;
  border-radius: 2px;
  background-color: rgb(248, 248, 248);
}


/*原有样式*/


.mainContentSearch {
  position: relative;
  height: 50px;
  line-height: 50px;
  padding: 5px 25px;
  background-color: rgb(248, 248, 248);
}

.mainContentSearch .iconfont {
  position: absolute;
  left: 31px;
}

.mainContentSearch input {
  padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 27px;
  height: 23px;
  border: 1px solid #b8b8b8;
  font-size: 1rem;
  color: #1f1f1f;
  transition: .3s;
  font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
}

.mainContentTableBtn {
  padding: 5px 25px;
  height: 40px;
  line-height: 40px;
}

.mainContentTableBtn ul {
  list-style: none;
  padding-left: 0;
}

.mainContentTableBtn ul li {
  float: left;
  margin-right: 15px;
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

.addBtn:active {
  background: #857aee;
}

.moveBtn {
  padding: 7px 15px;
  font-size: 0.9rem;
  background: rgb(211, 66, 66);
  color: white;
  border: 0;
  border-radius: 3px;
  text-align: center;
  cursor: pointer;
}

.moveBtn:active {
  background: rgb(253, 11, 11);
}

.showChoiceNum {
  font-size: 0.9rem;
  color: gray;
}

.mainPag {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
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

.tableLine2 button {
  margin-left: 20px;
  padding: 7px 15px;
  font-size: 0.9rem;
  background: rgb(146, 145, 74);
  color: white;
  border: 0;
  border-radius: 3px;
  text-align: center;
  cursor: pointer;
}

.tableLine2 button:active {
  background: rgb(131, 129, 66);
}

@media screen and (min-width: 1200px) {
  .teamDiv {
    width: 83%;
    margin-left: 17%;
  }

  .mainContent {
    width: 95%;
    margin: auto;
    margin-top: 120px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    margin-bottom: 15px;
  }

  .dialogDiv {
    width: 30% !important;
  }
}

@media screen and (max-width: 1199px) {
  .teamDiv {
    width: 100%;
  }

  .mainContent {
    width: 100%;
    margin: auto;
    margin-top: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    margin-bottom: 15px;
  }

  .dialogDiv {
    width: 90% !important;
  }
}
</style>