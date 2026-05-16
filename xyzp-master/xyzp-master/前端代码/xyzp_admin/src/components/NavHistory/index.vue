<template>
  <div class="navHistoryDiv">
    <ul>
      <li>
        <div :class="getClassName('/main/index')">
          <span @click="toPage(`/main/index`)">首页</span>
        </div>
      </li>
      <li v-for="(item, index) in getAllHistory" :key="index">
        <div :class="getClassName(item)">
          <span @click="toPage(item.url)">{{ item.name }}</span>
          <span class="iconfont icon-fork" @click="removeHistory(item, index)"></span>
        </div>
      </li>
    </ul>

  </div>
</template>

<script>
export default {
  name: "NavHistory",
  data() {
    return {
      allHistory: JSON.parse(sessionStorage.getItem("HISTORY")),
    }
  },
  methods: {
    removeHistory(item, index) {
      // 删除历史导航
      this.allHistory.splice(index, 1)
      sessionStorage.setItem("HISTORY", JSON.stringify(this.allHistory))
      // 判断删除后跳转到那个地方
      // 若删除的不是打开的页面就不用跳转
      if (this.judegeRemovePage(item) == 0) return
      // 否则就需要跳转
      else if (index != 0) this.$router.push(this.allHistory[index - 1].url)
      // 当删除后只剩一个的时候就返回首页
      else this.$router.push("/main/index")
    },
    toPage(url) {
      // 点击跳转导航历史
      this.$router.push(url)
    },
    // 判断是否为当前页面，背景颜色发送变化
    getClassName(item) {
      if (this.$route.path == item) return "navHistoryDivItem2"
      else if (item.url == this.$route.path) return "navHistoryDivItem2"
      else return "navHistoryDivItem"
    },
    // 判断删除的是否是当前页面
    judegeRemovePage(item) {
      if (this.getClassName(item) == 'navHistoryDivItem2') return 1
      else return 0
    }
  },
  computed: {
    getAllHistory() {
      return this.allHistory
    },

  },
}
</script>

<style>
*{
  transition: all .3s cubic-bezier(.645,.045,.355,1);
}

.navHistoryDivItem {
  width: 94.5px;
  text-align: center;
  font-size: 14px;
  color: #7d7a7a;
  border-right: 0.1px solid rgb(210, 214, 221);
  border-bottom: 0.1px solid rgb(210, 214, 221);
}

.navHistoryDivItem span {
  cursor: pointer;
}

.navHistoryDivItem .iconfont {
  color: gray;
  margin-left: 5px;
  cursor: pointer;
}

.navHistoryDivItem .iconfont:hover {
  background-color: rgb(161, 155, 155);
  border-radius: 50%;
}

.navHistoryDivItem2 {
  width: 94.5px;
  text-align: center;
  float: left;
  font-size: 14px;
  color: rgb(64, 158, 255);
  border-right: 0.1px solid rgb(210, 214, 221);
  border-bottom-color: white;
  background-color: white;
}

.navHistoryDivItem2 span {
  cursor: pointer;
}

.navHistoryDivItem2 .iconfont {
  color: gray;
  margin-left: 5px;
  cursor: pointer;
}

.navHistoryDivItem2 .iconfont:hover {
  background-color: rgb(161, 155, 155);
  border-radius: 50%;
}

/* 超出部分滚动 */
/* 设置父元素 */
.navHistoryDiv ul {
  list-style: none;
  padding-left: 0;
  overflow-x: auto;
  width: 100%;
  display: -webkit-box;
}

/* 隐藏滚动条 */
.navHistoryDiv ul::-webkit-scrollbar {
  display: none;
}

.navHistoryDiv ul li {
  float: left;
}


@media screen and (min-width: 1200px) {
  .navHistoryDiv {
    width: 83%;
    right: 0;
    height: 39px;
    line-height: 39px;
    top: 60px;
    position: fixed;
    background-color: rgb(245, 247, 250);
    border-top: 0.1px solid rgb(210, 214, 221);
    box-shadow: 0 2px 4px 0 rgb(0 0 0 / 12%), 0 0 6px 0 rgb(0 0 0 / 4%);
    z-index: 20;
  }
}

@media screen and (max-width: 1199px) {
  .navHistoryDiv {
    height: 39px;
    line-height: 39px;
    background-color: rgb(245, 247, 250);
    border-top: 0.1px solid rgb(210, 214, 221);
    box-shadow: 0 2px 4px 0 rgb(0 0 0 / 12%), 0 0 6px 0 rgb(0 0 0 / 4%);
    z-index: 20;
  }
}
</style>