<template>
  <div class="news-detail-page">
    <!-- Hero 区 -->
    <section class="detail-hero">
      <div class="page-container">
        <div class="detail-breadcrumb">
          <a href="/news">← 返回新闻列表</a>
        </div>
        <div class="detail-meta">
          <span class="detail-tag">{{ article.tag }}</span>
          <span class="detail-date">{{ article.date }}</span>
        </div>
        <h1 class="detail-title">{{ article.title }}</h1>
      </div>
    </section>

    <!-- 正文 -->
    <section class="detail-section">
      <div class="page-container">
        <div class="detail-card card">
          <div class="detail-image" v-if="article.img">
            <img :src="article.img" :alt="article.title">
          </div>
          <div class="detail-body">
            <p class="detail-desc">{{ article.desc }}</p>
            <div class="detail-content" v-html="article.content"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- 相关新闻 -->
    <section class="detail-related" v-if="relatedNews.length > 0">
      <div class="page-container">
        <h2 class="related-title">相关新闻</h2>
        <div class="related-grid">
          <div class="related-item card" v-for="(item, idx) in relatedNews" :key="idx" @click="goNews(item.id)">
            <div class="related-item-img" v-if="item.img">
              <img :src="item.img" :alt="item.title">
            </div>
            <div class="related-item-info">
              <span class="related-item-tag">{{ item.tag }}</span>
              <h4 class="related-item-title">{{ item.title }}</h4>
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

// 所有新闻数据（与列表页共享）
const ALL_NEWS = [
  {
    id: 1,
    img: "https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=600&q=80",
    tag: "公司新闻",
    date: "2026-04-28",
    title: "云才科技智能简历筛选系统正式上线",
    desc: "经过数月的研发与测试，云才科技智能简历OA筛选系统正式上线运行。系统基于ID3决策树算法，为企业提供高效、客观、可解释的简历筛选解决方案。",
    content: "<p>经过数月的研发与测试，云才科技智能简历OA筛选系统正式上线运行。系统基于ID3决策树算法，为企业提供高效、客观、可解释的简历筛选解决方案。</p><p>该系统能够自动分析求职者简历中的关键特征，包括年龄、学历、专业和期望薪资等维度，通过信息增益计算选择最优划分属性，递归构建决策树模型。相比传统人工筛选方式，系统将简历处理效率提升约70%。</p><p>云才科技CTO表示：'我们致力于将学术界成熟的机器学习算法与实际业务场景深度结合，打造真正可用、好用的智能招聘工具。本次上线的智能筛选模块只是第一步，未来还将引入更多AI能力。'</p><p>系统已在多家企业完成试点部署，得到了HR团队的一致好评。企业用户反馈，系统不仅节省了大量重复性工作时间，更重要的是提升了筛选的客观性和一致性，减少了因主观判断差异导致的人才错漏。</p>"
  },
  {
    id: 2,
    img: "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=600&q=80",
    tag: "行业资讯",
    date: "2026-04-15",
    title: "2026年企业招聘趋势报告：智能化成为标配",
    desc: "最新行业报告显示，超过65%的企业已经开始或计划引入AI辅助招聘工具。其中，简历智能筛选是最受关注的应用场景之一。",
    content: "<p>最新行业报告显示，超过65%的企业已经开始或计划引入AI辅助招聘工具。其中，简历智能筛选是最受关注的应用场景之一。</p><p>报告指出，随着数字化转型深入，企业对招聘效率和质量的追求日益提升。传统的简历筛选方式已难以满足海量候选人的快速处理需求，智能化招聘工具正从'可选项'变身为'必选项'。</p><p>值得关注的是，在众多AI技术中，可解释性强的算法（如决策树、随机森林等）在HR领域获得了更多青睐。HR人员表示，相比深度学习的'黑盒'模型，他们更信任能够清晰展示推理过程的决策树算法。</p><p>预计到2027年，智能招聘市场规模将突破200亿元，年均复合增长率超过30%。</p>"
  },
  {
    id: 3,
    img: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&q=80",
    tag: "产品更新",
    date: "2026-04-10",
    title: "V2.0版本更新：新增面试管理模块与数据看板",
    desc: "本次更新重点推出了面试管理系统和数据统计分析看板两大模块，进一步完善了招聘全流程管理能力。",
    content: "<p>本次更新重点推出了面试管理系统和数据统计分析看板两大模块，进一步完善了招聘全流程管理能力。</p><p>面试管理系统支持在线安排面试时间、地点，自动发送面试通知邮件，并支持面试结果的一键录入与统计分析。HR无需在多个系统间切换，即可完成从简历筛选到面试管理的完整工作流。</p><p>数据看板模块提供多维度统计图表，包括各职位投递趋势、部门招聘进度、录取转化率等关键指标的可视化展示，帮助管理层快速掌握招聘全局状态。</p><p>此外，本次更新还优化了系统性能，页面加载速度提升40%，并修复了若干已知问题。</p>"
  },
  {
    id: 4,
    img: "https://images.unsplash.com/photo-1542744173-8e7e53415bb0?w=600&q=80",
    tag: "媒体报道",
    date: "2026-03-20",
    title: "南华大学优秀毕业设计项目获企业高度评价",
    desc: "基于决策树算法的智能简历OA筛选系统作为南华大学计算机学院优秀毕业设计项目，获得了多家企业HR的一致好评。",
    content: "<p>基于决策树算法的智能简历OA筛选系统作为南华大学计算机学院优秀毕业设计项目，获得了多家企业HR的一致好评。</p><p>该项目由南华大学计算机学院学生完成，指导教师为计算机学院资深教授。项目将ID3决策树算法应用于校园招聘场景，构建了一套完整的智能化简历筛选系统。</p><p>评审专家认为，该项目在算法应用、系统架构和工程实践方面均达到了较高水平。多家与会企业代表表示，该系统具有实际应用价值，已有多家企业表达了合作意向。</p><p>南华大学计算机学院副院长表示：'我们将继续鼓励学生将理论知识与实际应用相结合，培养更多具有创新能力和工程实践能力的优秀人才。'</p>"
  },
  {
    id: 5,
    img: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600&q=80",
    tag: "公司新闻",
    date: "2026-03-08",
    title: "云才科技与多家企业达成战略合作协议",
    desc: "云才科技宣布与多家企业达成战略合作，将智能招聘管理系统推广至更广泛的行业领域，助力企业数字化招聘转型。",
    content: "<p>云才科技宣布与多家企业达成战略合作，将智能招聘管理系统推广至更广泛的行业领域，助力企业数字化招聘转型。</p><p>签约企业涵盖互联网、金融、制造、医疗等多个行业，均为各自领域的头部企业。根据协议，各方将在智能招聘、人才评估、人力资源数据分析等领域展开深度合作。</p><p>云才科技CEO在签约仪式上表示：'我们非常荣幸能与这么多优秀的企业建立合作关系。这既是对我们技术实力的认可，也为我们持续创新提供了宝贵的业务场景支持。'</p><p>据悉，云才科技计划于年内完成A轮融资，进一步加大研发投入，拓展全国市场。</p>"
  },
  {
    id: 6,
    img: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=600&q=80",
    tag: "行业资讯",
    date: "2026-02-20",
    title: "决策树算法在人力资源管理中的应用前景",
    desc: "随着机器学习技术的成熟，决策树算法凭借其可解释性强、实现简单的特点，在企业人才筛选与评估场景中展现出广阔的应用前景。",
    content: "<p>随着机器学习技术的成熟，决策树算法凭借其可解释性强、实现简单的特点，在企业人才筛选与评估场景中展现出广阔的应用前景。</p><p>ID3、C4.5和CART等决策树算法家族已经在信用评估、医疗诊断等领域取得了显著成效。近年来，越来越多的HR科技公司开始将决策树应用于简历筛选、面试评分和绩效预测等场景。</p><p>行业专家指出，决策树算法在HR领域的优势主要体现在：1) 可解释性强，HR可以理解并信任筛选结果；2) 能够处理混合类型数据（数值型+类别型）；3) 对缺失值具有一定的鲁棒性；4) 训练和推理效率高，适合实时应用。</p><p>但也有专家提醒，算法只是辅助工具，最终的招聘决策仍需结合HR的专业判断和企业的实际需求。人机结合是未来智能招聘的最佳实践模式。</p>"
  },
  {
    id: 7,
    img: "https://images.unsplash.com/photo-1600880292203-757bb62b4baf?w=600&q=80",
    tag: "产品更新",
    date: "2026-05-10",
    title: "智能客服模块上线：AI助力招聘咨询服务",
    desc: "基于自然语言处理技术的智能客服模块正式上线，求职者可通过在线对话获取职位详情、企业文化及常见问题解答，大幅提升用户体验。",
    content: "<p>基于自然语言处理技术的智能客服模块正式上线，求职者可通过在线对话获取职位详情、企业文化及常见问题解答。</p><p>该模块采用先进的NLP模型，能够理解求职者的自然语言问题，并实时提供准确的答复。支持的咨询范围涵盖职位要求、薪资结构、公司文化、面试流程等常见场景。</p><p>自上线以来，智能客服已累计处理超过5000条咨询，平均响应时间仅1.2秒，用户满意度达92%。HR团队反馈，该模块有效减轻了重复性答疑工作负担。</p>"
  },
  {
    id: 8,
    img: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=600&q=80",
    tag: "公司新闻",
    date: "2026-05-01",
    title: "云才科技荣获2026年度HR科技创新奖",
    desc: "在刚刚落幕的2026中国人力资本科技峰会上，云才科技凭借其创新的决策树智能筛选系统荣获'年度HR科技创新奖'，行业影响力再上新台阶。",
    content: "<p>在刚刚落幕的2026中国人力资本科技峰会上，云才科技凭借其创新的决策树智能筛选系统荣获'年度HR科技创新奖'。</p><p>该奖项由中国人力资源管理协会评选颁发，旨在表彰在人力资源科技领域做出突出贡献的企业。评审委员会一致认为，云才科技将决策树算法与招聘场景深度结合，实现了技术创新的实用化落地。</p><p>公司CEO在领奖时表示：'这个奖项是对团队辛勤付出的肯定，也是对我们技术路线正确性的验证。我们将继续深耕智能招聘领域，为客户创造更大价值。'</p>"
  },
  {
    id: 9,
    img: "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=600&q=80",
    tag: "行业资讯",
    date: "2026-04-20",
    title: "AI招聘市场持续升温，多家企业加速布局",
    desc: "随着人才争夺战日益激烈，AI驱动的智能招聘工具正成为企业的核心需求。云才科技将持续加大研发投入，保持在智能招聘赛道的领先优势。",
    content: "<p>随着人才争夺战日益激烈，AI驱动的智能招聘工具正成为企业的核心需求。据最新市场研究报告，2026年全球智能招聘市场规模预计将达到120亿美元。</p><p>行业分析指出，推动这一增长的主要因素包括：数字化转型加速、人才竞争白热化、以及AI技术成熟度提升。越来越多的企业认识到，传统招聘方式已难以应对日益复杂的人力资源管理需求。</p><p>云才科技作为赛道早期入局者，凭借ID3决策树算法的独特优势，已在多个细分领域建立了先发优势。公司计划在未来12个月内将团队规模翻倍，加速产品迭代。</p>"
  }
];

export default {
  name: "NewsDetail",
  components: { Footer },
  data() {
    return {
      article: { title: '', desc: '', tag: '', date: '', img: '', content: '' },
      relatedNews: []
    }
  },
  mounted() {
    this.loadArticle();
  },
  methods: {
    loadArticle() {
      const id = parseInt(this.$route.params.id);
      const article = ALL_NEWS.find(item => item.id === id);
      if (article) {
        this.article = article;
        // 获取同标签的相关新闻
        this.relatedNews = ALL_NEWS
          .filter(item => item.tag === article.tag && item.id !== id)
          .slice(0, 3);
      } else {
        this.$router.push('/news');
      }
    },
    goNews(id) {
      this.$router.push('/news/' + id);
      window.scrollTo(0, 0);
    }
  },
  watch: {
    '$route'() {
      this.loadArticle();
    }
  }
}
</script>

<style scoped>
.detail-hero {
  background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
  padding: 60px 0 40px;
  color: #fff;
}

.detail-breadcrumb a {
  color: #94a3b8;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.2s;
}

.detail-breadcrumb a:hover {
  color: #fff;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 20px 0 12px;
}

.detail-tag {
  font-size: 12px;
  padding: 2px 10px;
  background: rgba(255,255,255,0.15);
  color: #cbd5e1;
  border-radius: 4px;
}

.detail-date {
  font-size: 14px;
  color: #94a3b8;
}

.detail-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  line-height: 1.4;
}

.detail-section {
  padding: 40px 0 60px;
  background: var(--bg-body);
}

.detail-card {
  padding: 40px;
  border: 1px solid var(--border-light);
}

.detail-image {
  margin: -40px -40px 30px;
  height: 360px;
  overflow: hidden;
}

.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-body {
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-secondary);
}

.detail-desc {
  font-size: 17px;
  color: var(--text-primary);
  font-weight: 500;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--border-light);
}

.detail-content :deep(p) {
  margin: 0 0 16px;
}

.detail-related {
  padding: 0 0 60px;
  background: var(--bg-body);
}

.related-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 20px;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.related-item {
  cursor: pointer;
  overflow: hidden;
  border: 1px solid var(--border-light);
  transition: transform 0.2s, box-shadow 0.2s;
}

.related-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.related-item-img {
  height: 140px;
  overflow: hidden;
}

.related-item-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-item-info {
  padding: 14px;
}

.related-item-tag {
  font-size: 11px;
  padding: 2px 8px;
  background: var(--bg-light);
  color: var(--primary-light);
  border-radius: 3px;
}

.related-item-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 8px 0 0;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .detail-title { font-size: 22px; }
  .detail-card { padding: 20px; }
  .detail-image { margin: -20px -20px 20px; height: 200px; }
  .related-grid { grid-template-columns: 1fr; }
}
</style>
