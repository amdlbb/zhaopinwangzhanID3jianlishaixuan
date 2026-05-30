<template>
  <div class="adminDiv">
    <NavTop></NavTop>
    <NavHistory></NavHistory>
    <div class="mainContent">
      <div class="mainContentSearch">
        <span class="iconfont icon-sousuo"></span>
        <input type="text" placeholder="搜索" @keydown.enter="search" v-model="searchContent">
      </div>
      <div class="mainContentTable">
        <div class="mainContentTableBtn">
          <ul>
            <li><button class="moveBtn" @click="remove">取消面试</button></li>
          </ul>
        </div>
        <el-table ref="multipleTable" :data="tableData" style="width: 100%"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="70">
          </el-table-column>
          <el-table-column prop="username" label="账号" width="150">
          </el-table-column>
          <el-table-column prop="teamname" label="部门" width="150">
          </el-table-column>
          <el-table-column prop="address" label="地点" width="150">
          </el-table-column>
          <el-table-column prop="endTime" label="开始时间" width="250">
          </el-table-column>
          <el-table-column prop="beginTime" label="结束时间" width="250">
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <span v-show="scope.row.status == 1" class="main-table-active">待面</span>
              <span v-show="scope.row.status == 2" class="main-table-remove">淘汰</span>
              <span v-show="scope.row.status == 3" class="main-table-success">录取</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button  v-show="scope.row.status == 1" type="text" size="small" @click="admissionBtn(scope.row)">录取</el-button>
              <el-button  v-show="scope.row.status == 1" type="text" size="small" @click="submitLookOk(scope.row.userId, scope.row.id)">淘汰</el-button>
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

    <el-dialog title="添加" :visible.sync="addShow" width="30%" custom-class="dialogDiv">
      <div class="tableLine">
        <div class="tableLine1">
          <span>账号</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="addUsername" class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>密码</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="addPassword" class="tableInput">
        </div>
      </div>
      <div class="tableLine">
        <div class="tableLine1">
          <span>邮箱</span>
        </div>
        <div class="tableLine2">
          <input type="text" v-model="addEmail" class="tableInput">
        </div>
      </div>
      <div class="tableBtnLine">
        <button class="addBtn" @click="save">保存</button>
      </div>
    </el-dialog>

    <el-dialog title="提示" :visible.sync="showReset" width="30%" custom-class="dialogDiv">
      <span style="font-size: 1rem;">确认重置密码为：</span>
      <span style="color: red;">12345678</span>
      <span slot="footer" class="dialog-footer">
                <el-button @click="showReset = false">取 消</el-button>
                <el-button type="primary" @click="reset">确 定</el-button>
            </span>
    </el-dialog>

    <el-dialog title="录取设置" :visible.sync="admissionDialogVisible" width="40%" custom-class="dialogDiv">
      <div class="admissionDialog">
        <input type="text" v-model="admissionInfo.workAddress" placeholder="报道地点">
      </div>
      <div class="admissionTime">
        <el-date-picker v-model="admissionInfo.workTime" type="datetime" value-format="timestamp"
                        placeholder="报到时间">
        </el-date-picker>
      </div>
      <div class="admissionSubmitBtn">
        <button @click="submitAdmission">确定</button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
import NavTop from "@/components/NavTop/index.vue";
import NavHistory from "@/components/NavHistory/index.vue";
export default {
  name: "TeamManager",
  components: {NavHistory, NavTop},
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
      // 搜索内容
      searchContent: "",
      // 展示重置页面
      showReset: false,
      // 是否编辑信息
      isEditInfo: false,
      // 是否添加信息
      addShow: false,
      // 添加的管理员信息
      addUsername: "",
      addPassword: "",
      addEmail: "",

      admissionDialogVisible: false,
      // 录取信息设置
      admissionInfo: {
        teamJobId: 0,
        userId: 0,
        interviewId: 0,
        jobName: "",
        workAddress: "",
        workTime: "",
      },
    }
  },
  mounted() {
    // 初始化
    this.init()
  },
  methods: {

    async handleCurrentChange(val) {
      let res = await this.$store.dispatch("getInterviewList", {
        page: val,
        pageSize: 10
      })
      if (res.code == 200) {
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
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
    //录取
    async submitAdmission() {

      let res = await this.$store.dispatch('getSubmitSave', this.admissionInfo)
      if (res.code == 200) {
        this.init()
        this.admissionDialogVisible = false
        // 发送邮件
        this.sendInterviewEmail({
          userId: this.admissionInfo.userId,
          workTime: this.admissionInfo.workTime,
          jobName: this.admissionInfo.jobName,
          workAddress: this.admissionInfo.workAddress
        })
      } else {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }

    },
    // 发送邮件
    async sendInterviewEmail(data) {
      this.$message({
        message: "设置成功，正在发送邮件通知用户报道",
        type: 'success',
        center: true,
        duration: 1500
      })
      let res = await this.$store.dispatch("getAdmissionEmail", data)
      if (res.code == 200) {
        this.$message({
          message: "发送成功",
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
    async submitLookOk(userId, interviewId) {
      let res = await this.$store.dispatch("getChangAdmissionStatus", {
        userId: userId,
        interviewId: interviewId
      })
      if (res.code == 200) {
        this.$message({
          message: "设置成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.init()
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

    // 勾选行
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    admissionBtn(data){
      console.log("点击了")
      console.log(data)
      this.admissionInfo.interviewId = data.id
      this.admissionInfo.teamJobId = data.teamJobId
      this.admissionInfo.userId = data.userId
      this.admissionInfo.jobName = data.jobname
      this.admissionDialogVisible = true
    },

    async init() {
      let res = await this.$store.dispatch("getInterviewList", {
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

      }
      else {
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
      }
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
      let res = await this.$store.dispatch("getSearchAdmin", {
        content: this.searchContent
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
        this.tableData = res.data.data
        this.totalNum = parseInt(res.data.totalNum)
      }
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
      let res = await this.$store.dispatch("adminRemoveAdmin", idxList)
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }
      else {
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
    async changeStatus() {
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
      let res = await this.$store.dispatch("adminReturnAdmin", idxList)
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }
      else {
        this.$message({
          message: "激活成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.init()
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
      let res = await this.$store.dispatch("adminResetAdmin", idxList)
      if (res.code != 200) {
        this.$message({
          message: res.msg,
          type: 'error',
          center: true,
          duration: 1500
        })
      }
      else {
        this.$message({
          message: "重置密码成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.showReset = false
        this.init()
      }
    },
    // 编辑管理员信息
    editInfo() {
      this.isEditInfo = true
    },
    // 添加
    async save() {
      if (!this.addUsername.trim() || !this.addPassword.trim() || !this.addEmail.trim()) {
        this.$message({
          message: "请正确填写",
          type: 'warning',
          center: true,
          duration: 1500
        })
        return
      }
      // 发送请求
      let res = await this.$store.dispatch("saveAdmin", {
        username: this.addUsername,
        password: this.$md5(this.addPassword),
        email: this.addEmail
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
        this.$message({
          message: "添加成功",
          type: 'success',
          center: true,
          duration: 1500
        })
        this.showReset = false
        this.init()
      }
    }
  }

}

</script>


<style>

/* 大屏幕 */
@media screen and (min-width: 1200px) {
  .dialogDiv {
    width: 40% !important;
  }
}

/* 小屏幕 */
@media screen and (max-width: 1200px) {
  .dialogDiv {
    width: 95% !important;
  }
}
.admissionSubmitBtn {
  height: 60px;
  text-align: center;
  line-height: 60px;
}

.admissionSubmitBtn button {
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

.admissionSubmitBtn button:active {
  background: rgb(145, 145, 234);
}

.admissionTime {
  text-align: center;
  height: 100px;
}

.admissionDialog {
  height: 60px;
  line-height: 60px;
  text-align: center;
}

.admissionDialog input {
  padding: 5px 10px;
  width: 60%;
  border: 1px solid #b8b8b8;
  font-size: 1rem;
  color: #1f1f1f;
  transition: .3s;
  font-family: PingFangSC-Regular, Tahoma, Helvetica, "Microsoft Yahei", "微软雅黑", Arial, STHeiti;
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

.main-table-active {
  padding: 3px 6px;
  background-color: #968ced;
  color: white;
  border-radius: 3px;
}

.main-table-remove {
  padding: 3px 6px;
  background-color: #d72332;
  color: white;
  border-radius: 3px;
}

.main-table-success {
  padding: 3px 6px;
  background-color: #44ca35;
  color: white;
  border-radius: 3px;
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

.resetBtn {
  padding: 7px 15px;
  font-size: 0.9rem;
  background: #44ca35;
  color: white;
  border: 0;
  border-radius: 3px;
  text-align: center;
  cursor: pointer;
}

.resetBtn:active {
  background: #26c018;
}

.returnBtn {
  padding: 7px 15px;
  font-size: 0.9rem;
  background: #869585;
  color: white;
  border: 0;
  border-radius: 3px;
  text-align: center;
  cursor: pointer;
}

.returnBtn:active {
  background: rgb(108, 125, 108);
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

.tableBtnLine {
  text-align: center;
  height: 60px;
  line-height: 60px;
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
  .adminDiv {
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
  .adminDiv {
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