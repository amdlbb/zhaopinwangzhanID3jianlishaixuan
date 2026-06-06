<template>
  <footer class="footerDiv">
    <div class="footer-inner page-container">
      <div class="footer-grid">
        <div class="footer-col footer-brand">
          <img src="@/assets/logo4.jpg" alt="云才科技logo图片" class="footer-logo">
          <p class="footer-desc">云才科技致力于为企业提供智能化的招聘管理解决方案，基于决策树算法实现简历智能筛选，让招聘更高效、更精准。</p>
        </div>
        <div class="footer-col">
          <h4 class="footer-title">快速链接</h4>
          <ul class="footer-links">
            <li><a href="/index">首页</a></li>
            <li><a href="/workhome">招聘信息</a></li>
            <li><a href="/about">关于我们</a></li>
            <li><a href="/product">产品服务</a></li>
            <li><a href="/news">新闻动态</a></li>
          </ul>
        </div>
        <div class="footer-col">
          <h4 class="footer-title">产品服务</h4>
          <ul class="footer-links">
            <li><a href="/product">智能简历筛选</a></li>
            <li><a href="/product">招聘流程管理</a></li>
            <li><a href="/product">面试管理系统</a></li>
            <li><a href="/product">数据分析看板</a></li>
          </ul>
        </div>
        <div class="footer-col">
          <h4 class="footer-title">联系我们</h4>
          <ul class="footer-links contact-info">
            <li><span class="iconfont icon-dizhi"></span> 地址：湖南省衡阳市南华大学</li>
            <li><span class="iconfont icon-youxiang"></span> 邮箱：contact@yuncaihr.com</li>
            <li><span class="iconfont icon-dianhua"></span> 电话：0734-8888888</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <div class="footer-bottom-left">
          Copyright &copy; 2026 云才科技 YuncaiHR. All rights reserved.
        </div>
        <div class="footer-bottom-right">
          湘ICP备xxxxxx号-x
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {

}
</script>

<style>
@media screen and (min-width: 1200px) {
  .footerDiv {
    background: #0f2640;
    color: #cbd5e0;
    padding: 48px 0 0;
  }

  .footer-grid {
    display: flex;
    justify-content: space-between;
    gap: 40px;
    flex-wrap: wrap;
  }

  .footer-col {
    flex: 1;
    min-width: 200px;
  }

  .footer-brand {
    flex: 1.5;
  }
}

@media screen and (max-width: 1199px) {
  .footerDiv {
    background: #0f2640;
    color: #cbd5e0;
    padding: 32px 16px 0;
  }

  .footer-grid {
    display: flex;
    flex-direction: column;
    gap: 28px;
  }

  .footer-col {
    width: 100%;
  }
}

.footer-logo {
  height: 32px;
  margin-bottom: 12px;
}

.footer-desc {
  font-size: 14px;
  line-height: 1.7;
  color: #94a3b8;
  max-width: 300px;
}

.footer-title {
  font-size: 16px;
  font-weight: 600;
  color: #e2e8f0;
  margin: 0 0 16px;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 10px;
  font-size: 14px;
}

.footer-links a {
  color: #94a3b8;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: #e2e8f0;
}

.contact-info li {
  color: #94a3b8;
  display: flex;
  align-items: center;
  gap: 6px;
}

.contact-info li .iconfont {
  font-size: 14px;
  color: #64748b;
}

.footer-bottom {
  border-top: 1px solid #1e3a5f;
  padding: 20px 0;
  margin-top: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #64748b;
  flex-wrap: wrap;
  gap: 8px;
}

.footer-bottom-right {
  color: #475569;
}
</style>
