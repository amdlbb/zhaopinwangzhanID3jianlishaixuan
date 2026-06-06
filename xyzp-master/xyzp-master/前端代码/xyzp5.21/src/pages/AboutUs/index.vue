<template>
  <div class="about-page">
    <!-- Hero 区 -->
    <section class="about-hero">
      <div class="page-container">
        <h1 class="about-hero-title">关于云才科技</h1>
        <p class="about-hero-desc">
          以技术驱动招聘革新，用智能算法赋能人才决策
        </p>
      </div>
    </section>

    <!-- 公司简介 -->
    <section class="about-section">
      <div class="page-container">
        <div class="about-intro">
          <div class="about-intro-text">
            <h2 class="section-title">公司简介</h2>
            <p>云才科技成立于2023年，是一家专注于企业智能招聘管理领域的技术型公司。我们深度融合人工智能与人力资源管理，致力于为企业提供高效、客观、可解释的简历筛选解决方案。</p>
            <p>公司核心产品——智能简历OA筛选系统，基于经典决策树算法（ID3/C4.5/CART），通过对简历关键特征进行信息增益计算，实现自动化简历分类与智能推荐，帮助HR将初筛时间缩短80%以上。</p>
            <p>截至目前，云才科技已服务超过200家企业客户，覆盖互联网、金融、制造、医疗等多个行业，累计处理简历超过50万份。</p>
          </div>
          <div class="about-intro-img">
            <img src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600&q=80" alt="公司团队合影图片">
          </div>
        </div>
      </div>
    </section>

    <!-- 核心数据 -->
    <section class="about-stats">
      <div class="page-container">
        <div class="stats-grid">
          <div class="stat-item">
            <span class="stat-number">200+</span>
            <span class="stat-label">服务企业</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">50万+</span>
            <span class="stat-label">处理简历</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">80%</span>
            <span class="stat-label">筛选效率提升</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">98.5%</span>
            <span class="stat-label">匹配准确率</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 发展历程 -->
    <section class="about-section">
      <div class="page-container">
        <h2 class="section-title">发展历程</h2>
        <p class="section-subtitle">从构想到落地，每一步都扎实前行</p>
        <div class="timeline">
          <div class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h4>2025年11月</h4>
              <p>项目立项，完成需求分析与技术调研</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h4>2026年1月</h4>
              <p>系统架构设计完成，前后端同步开发启动</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h4>2026年3月</h4>
              <p>核心决策树算法模块开发完成，模型训练通过验证</p>
            </div>
          </div>
          <div class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h4>2026年5月</h4>
              <p>系统集成测试完成，正式上线运行</p>
            </div>
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
  name: "AboutUs",
  components: {
    Footer
  }
}
</script>

<style scoped>
.about-hero {
  background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
  padding: 80px 0 60px;
  text-align: center;
}

.about-hero-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 12px;
}

.about-hero-desc {
  font-size: 18px;
  color: #94a3b8;
  margin: 0;
}

.about-section {
  padding: 60px 0;
  background: #fff;
}

.about-intro {
  display: flex;
  align-items: center;
  gap: 48px;
}

.about-intro-text {
  flex: 1;
}

.about-intro-text .section-title {
  text-align: left;
}

.about-intro-text p {
  font-size: 16px;
  line-height: 1.8;
  color: var(--text-secondary);
  margin: 16px 0;
}

.about-intro-img {
  flex: 1;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-md);
}

.about-intro-img img {
  width: 100%;
  height: auto;
  display: block;
}

.about-stats {
  padding: 48px 0;
  background: var(--bg-light);
}

.stats-grid {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 24px;
}

.stat-item {
  text-align: center;
  padding: 24px;
}

.stat-number {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: var(--primary);
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  color: var(--text-secondary);
}

.timeline {
  max-width: 600px;
  margin: 0 auto;
  position: relative;
  padding-left: 30px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: var(--border-light);
}

.timeline-item {
  position: relative;
  padding-bottom: 32px;
}

.timeline-dot {
  position: absolute;
  left: -26px;
  top: 4px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: var(--primary);
  border: 3px solid #fff;
  box-shadow: 0 0 0 2px var(--primary);
}

.timeline-content h4 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 4px;
}

.timeline-content p {
  font-size: 15px;
  color: var(--text-secondary);
  margin: 0;
}

@media (max-width: 768px) {
  .about-hero {
    padding: 60px 0 40px;
  }
  .about-hero-title {
    font-size: 28px;
  }
  .about-hero-desc {
    font-size: 16px;
  }
  .about-intro {
    flex-direction: column;
    gap: 24px;
  }
  .about-intro-text .section-title {
    text-align: center;
  }
  .stat-number {
    font-size: 26px;
  }
}
</style>
