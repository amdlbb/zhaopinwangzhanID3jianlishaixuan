<template>
  <div class="product-page">
    <!-- Hero 区 -->
    <section class="product-hero">
      <div class="page-container">
        <h1 class="product-hero-title">产品与服务</h1>
        <p class="product-hero-desc">基于决策树算法的智能招聘管理解决方案</p>
      </div>
    </section>

    <!-- 核心产品 -->
    <section class="product-section">
      <div class="page-container">
        <h2 class="section-title">智能简历OA筛选系统</h2>
        <p class="section-subtitle">从海量简历中精准定位优秀人才</p>

        <div class="product-cards">
          <div class="product-card card">
            <div class="product-card-icon">
              <img src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=120&q=80" alt="智能筛选图标图片">
            </div>
            <h3 class="product-card-title">智能简历筛选</h3>
            <p class="product-card-desc">基于ID3决策树算法，对学历、工作经验、技能匹配度等关键特征进行信息增益计算，自动判定简历匹配度。通过的简历优先展示，大幅缩短HR初筛时间。</p>
          </div>

          <div class="product-card card">
            <div class="product-card-icon">
              <img src="https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=120&q=80" alt="招聘管理图标图片">
            </div>
            <h3 class="product-card-title">招聘流程管理</h3>
            <p class="product-card-desc">覆盖从职位发布、简历收集、面试安排到录取入职的全流程管理。支持部门独立管理招聘，管理员统一审核，流程清晰可控。</p>
          </div>

          <div class="product-card card">
            <div class="product-card-icon">
              <img src="https://images.unsplash.com/photo-1520607162513-77705c0f0dbf?w=120&q=80" alt="面试管理图标图片">
            </div>
            <h3 class="product-card-title">面试管理系统</h3>
            <p class="product-card-desc">自动发送面试通知邮件，支持面试时间、地点管理。候选人状态实时跟进，从待面试到已录取全程可视化管理。</p>
          </div>

          <div class="product-card card">
            <div class="product-card-icon">
              <img src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=120&q=80" alt="数据看板图标图片">
            </div>
            <h3 class="product-card-title">数据统计分析</h3>
            <p class="product-card-desc">提供多维度的数据看板，包括简历处理量、面试转化率、录取率等关键指标。用数据驱动招聘决策，持续优化招聘策略。</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 技术优势 -->
    <section class="tech-section">
      <div class="page-container">
        <h2 class="section-title">技术优势</h2>
        <p class="section-subtitle">为什么选择基于决策树的智能筛选方案</p>

        <div class="tech-grid">
          <div class="tech-item">
            <h4>可解释性强</h4>
            <p>决策树的树形结构直观展示筛选规则，HR可清晰了解每份简历的判定依据，非技术背景也能轻松理解。</p>
          </div>
          <div class="tech-item">
            <h4>计算效率高</h4>
            <p>ID3算法时间复杂度低，可在毫秒级完成上万份简历的分类预测，满足高并发场景需求。</p>
          </div>
          <div class="tech-item">
            <h4>客观公正</h4>
            <p>基于信息增益的数学建模方式，消除人工筛选的主观偏见，确保每位候选人获得公平评估。</p>
          </div>
          <div class="tech-item">
            <h4>持续优化</h4>
            <p>支持模型定期重训练，随着招聘数据积累，筛选准确率可以持续提升，实现自我迭代。</p>
          </div>
        </div>
      </div>
    </section>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer'
export default {
  name: "ProductService",
  components: {
    Footer
  }
}
</script>

<style scoped>
.product-hero {
  background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
  padding: 80px 0 60px;
  text-align: center;
}

.product-hero-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 12px;
}

.product-hero-desc {
  font-size: 18px;
  color: #94a3b8;
  margin: 0;
}

.product-section {
  padding: 60px 0;
  background: #fff;
}

.product-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 24px;
}

.product-card {
  padding: 32px 24px;
  text-align: center;
  border: 1px solid var(--border-light);
}

.product-card-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 16px;
  border-radius: 12px;
  overflow: hidden;
}

.product-card-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px;
}

.product-card-desc {
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-secondary);
  margin: 0;
}

.tech-section {
  padding: 60px 0;
  background: var(--bg-light);
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
}

.tech-item {
  background: #fff;
  padding: 28px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  border-left: 4px solid var(--primary);
}

.tech-item h4 {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
}

.tech-item p {
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-secondary);
  margin: 0;
}

@media (max-width: 768px) {
  .product-hero {
    padding: 60px 0 40px;
  }
  .product-hero-title {
    font-size: 28px;
  }
  .product-hero-desc {
    font-size: 16px;
  }
  .product-cards {
    grid-template-columns: 1fr;
  }
  .tech-grid {
    grid-template-columns: 1fr;
  }
}
</style>
