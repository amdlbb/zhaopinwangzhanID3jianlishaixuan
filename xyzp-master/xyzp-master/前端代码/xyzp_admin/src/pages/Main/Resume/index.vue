<template>
  <div class="adminDiv">
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
            <li><button class="moveBtn" @click="batchAdmit">面试</button></li>
            <li><button class="moveBtn" style="background: rgb(211, 66, 66);" @click="batchEliminate">淘汰</button></li>
          </ul>
        </div>
        <el-table ref="multipleTable" :data="pagedTableData" style="width: 100%"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="70" align="center">
          </el-table-column>
          <el-table-column prop="username" label="账号" width="150">
            <template slot-scope="scope">
              <span v-html="scope.row.username"></span>
            </template>
          </el-table-column>
          <el-table-column prop="nickname" label="姓名" width="180">
            <template slot-scope="scope">
              <span v-html="scope.row.nickname"></span>
            </template>
          </el-table-column>
          <el-table-column prop="jobname" label="职位" width="180">
            <template slot-scope="scope">
              <span v-html="scope.row.jobname"></span>
            </template>
          </el-table-column>
          <el-table-column prop="teamname" label="部门" width="180">
            <template slot-scope="scope">
              <span v-html="scope.row.teamname"></span>
            </template>
          </el-table-column>
          <el-table-column label="是否推荐" width="120">
            <template slot-scope="scope">
              <span v-show="scope.row.predictResult === true" class="main-table-recommend">推荐</span>
              <span v-show="scope.row.predictResult === false" class="main-table-notrecommend">不推荐</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="350">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="showResumeDetail(scope.row.userResume)">查看简历</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="mainPag">
          <el-pagination background layout="prev, pager, next" :total="filteredData.length"
                         @current-change="handleCurrentChange" :current-page="page" :page-size="pageSize">
          </el-pagination>
        </div>
      </div>
    </div>

    <!-- 简历详情对话框 -->
    <el-dialog title="简历详情" :visible.sync="resumeDialogVisible" width="50%" custom-class="dialogDiv"  :lazy="true">
      <div class="tableLine" v-if="currentResume">
        <div class="tableLine1"><span>姓名</span></div>
        <div class="tableLine2"><span v-html="currentResume.name"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>性别</span></div>
        <div class="tableLine2"><span v-html="currentResume.sex"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>年龄</span></div>
        <div class="tableLine2"><span v-html="currentResume.age"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>手机</span></div>
        <div class="tableLine2"><span v-html="currentResume.phone"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>学历</span></div>
        <div class="tableLine2"><span v-html="currentResume.education"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>专业</span></div>
        <div class="tableLine2"><span v-html="currentResume.major"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>学校</span></div>
        <div class="tableLine2"><span v-html="currentResume.school"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>期望职位</span></div>
        <div class="tableLine2"><span v-html="currentResume.exceptionJob"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>期望薪资</span></div>
        <div class="tableLine2"><span v-html="currentResume.exceptionSalary"></span></div>
      </div>
      <div class="tableLine">
        <div class="tableLine1"><span>详细内容</span></div>
        <div class="tableLine2"><p style="margin:0; white-space: pre-wrap;" v-html="currentResume.content || '暂无描述'"></p></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import NavTop from "@/components/NavTop/index.vue";
import NavHistory from "@/components/NavHistory/index.vue";

export default {
  name: "ResumeManager",
  components: { NavHistory, NavTop },
  data() {
    return {
      // 所有投递数据（原始）
      allData: [],
      // 搜索内容
      searchContent: "",
      // 多选
      multipleSelection: [],
      // 前端分页
      page: 1,
      pageSize: 10,
      // 简历详情对话框
      resumeDialogVisible: false,
      currentResume: {},
    };
  },
  computed: {
    // 按预测结果排序，并支持搜索过滤
    filteredData() {
      let data = this.allData;
      // 排序：true在前，false在后
      return data.slice().sort((a, b) => (b.predictResult === true ? 1 : -1) - (a.predictResult === true ? 1 : -1));
    },
    // 当前页数据
    pagedTableData() {
      const start = (this.page - 1) * this.pageSize;
      return this.filteredData.slice(start, start + this.pageSize);
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      try {
        const res = await this.$store.dispatch("getDeliverList");
        if (res.code == 200) {
          this.allData = res.data || [];
        } else {
          this.$message({
            message: res.message || res.msg || "获取数据失败",
            type: "error",
            center: true,
            duration: 1500,
          });
        }
      } catch (error) {
        this.$message({
          message: "请求异常",
          type: "error",
          center: true,
          duration: 1500,
        });
      }
    },
    // 搜索（重置页码）
    async search() {
      // 无需额外处理，过滤和分页由计算属性自动完成
      if (!this.searchContent.trim()) {
        this.$message({
          message: "搜索内容不能为空",
          type: "warning",
          center: true,
          duration: 1500
        })
        return
      }
      try {
        const res = await this.$store.dispatch("searchResume", {
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
          this.allData = res.data || []
          this.page = 1
        }
      } catch (error) {
        this.$message({
          message: "请求异常",
          type: "error",
          center: true,
          duration: 1500
        })
      }
    },
    // 分页切换
    handleCurrentChange(val) {
      this.page = val;
    },
    // 多选改变
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 查看简历详情
    showResumeDetail(userResume) {
      if (userResume) {
        this.currentResume = {
          name: userResume.name || '-',
          sex: userResume.sex || '-',
          age: userResume.age || '-',
          phone: userResume.phone || '-',
          education: userResume.education || '-',
          major: userResume.major || '-',
          school: userResume.school || '-',
          exceptionJob: userResume.exceptionJob || '-',
          exceptionSalary: userResume.exceptionSalary || '-',
          content: userResume.content || '暂无描述',
        };
        this.resumeDialogVisible = true;
      } else {
        this.$message({
          message: '简历信息不完整',
          type: 'warning',
          center: true,
          duration: 1500,
        });
      }
    },
    // 批量录取（接口未就绪）
    batchAdmit() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          message: "请先选择要录取的候选人",
          type: "warning",
          center: true,
          duration: 1500,
        });
        return;
      }
      // TODO: 调用录取接口
      this.$message({
        message: "录取功能暂未开放",
        type: "info",
        center: true,
        duration: 1500,
      });
    },
    // 批量淘汰（接口未就绪）
    batchEliminate() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          message: "请先选择要淘汰的候选人",
          type: "warning",
          center: true,
          duration: 1500,
        });
        return;
      }
      // TODO: 调用淘汰接口
      this.$message({
        message: "淘汰功能暂未开放",
        type: "info",
        center: true,
        duration: 1500,
      });
    },
  },
};
</script>

<style scoped>
/* 复用面试管理的大部分样式，并补充预测结果状态样式 */
.main-table-recommend {
  padding: 3px 6px;
  background-color: #44ca35;
  color: white;
  border-radius: 3px;
}

.main-table-notrecommend {
  padding: 3px 6px;
  background-color: #d72332;
  color: white;
  border-radius: 3px;
}

/* 以下直接从面试管理页面继承，确保布局统一 */
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

.moveBtn {
  padding: 7px 15px;
  font-size: 0.9rem;
  background: #44ca35;
  color: white;
  border: 0;
  border-radius: 3px;
  text-align: center;
  cursor: pointer;
}

.moveBtn:active {
  background: #26c018;
}

.tableLine {
  display: flex;
  align-items: center;
  margin-top: 5px;
  margin-bottom: 5px;
}

.tableLine1 {
  width: 30%;
  text-align: right;
  padding-right: 15px;
  font-weight: bold;
}

.tableLine2 {
  width: 70%;
}

.mainPag {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
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
    width: 50% !important;
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
    width: 95% !important;
  }
}
</style>