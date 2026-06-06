<template>
  <div class="news-page">
    <!-- Hero 区 -->
    <section class="news-hero">
      <div class="page-container">
        <h1 class="news-hero-title">新闻动态</h1>
        <p class="news-hero-desc">了解云才科技最新动态与行业资讯</p>
      </div>
    </section>

    <!-- 新闻分类 -->
    <section class="news-section">
      <div class="page-container">
        <div class="news-categories">
          <span
            v-for="cat in categories"
            :key="cat"
            :class="['news-cat', { active: activeCategory === cat }]"
            @click="switchCategory(cat)"
          >{{ cat }}</span>
        </div>

        <div class="news-list">
          <div class="news-item card" v-for="(item, index) in pagedNews" :key="index" @click="goDetail(item.id)">
            <div class="news-item-img">
              <img :src="item.img" :alt="item.title + '配图'">
            </div>
            <div class="news-item-content">
              <div class="news-item-meta">
                <span class="news-item-tag">{{ item.tag }}</span>
                <span class="news-item-date">{{ item.date }}</span>
              </div>
              <h3 class="news-item-title">{{ item.title }}</h3>
              <p class="news-item-desc">{{ item.desc }}</p>
              <span class="news-item-link">了解更多 →</span>
            </div>
          </div>
        </div>

        <div class="news-more">
          <span class="btn-outline" v-if="hasMore" @click="loadMore">加载更多</span>
          <span class="btn-outline btn-disabled" v-else>已加载全部</span>
        </div>
      </div>
    </section>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer'
export default {
  name: "News",
  components: {
    Footer
  },
  data() {
    return {
      activeCategory: "全部",
      categories: ["全部", "公司新闻", "行业资讯", "产品更新", "媒体报道"],
      currentPage: 1,
      pageSize: 6,
      newsList: [
        {
          id: 1,
          img: "https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=600&q=80",
          tag: "公司新闻",
          date: "2026-04-28",
          title: "云才科技智能简历筛选系统正式上线",
          desc: "经过数月的研发与测试，云才科技智能简历OA筛选系统正式上线运行。系统基于ID3决策树算法，为企业提供高效、客观、可解释的简历筛选解决方案。"
        },
        {
          id: 2,
          img: "https://images.unsplash.com/photo-1553877522-43269d4ea984?w=600&q=80",
          tag: "行业资讯",
          date: "2026-04-15",
          title: "2026年企业招聘趋势报告：智能化成为标配",
          desc: "最新行业报告显示，超过65%的企业已经开始或计划引入AI辅助招聘工具。其中，简历智能筛选是最受关注的应用场景之一。"
        },
        {
          id: 3,
          img: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&q=80",
          tag: "产品更新",
          date: "2026-04-10",
          title: "V2.0版本更新：新增面试管理模块与数据看板",
          desc: "本次更新重点推出了面试管理系统和数据统计分析看板两大模块，进一步完善了招聘全流程管理能力。"
        },
        {
          id: 4,
          img: "https://images.unsplash.com/photo-1542744173-8e7e53415bb0?w=600&q=80",
          tag: "媒体报道",
          date: "2026-03-20",
          title: "南华大学优秀毕业设计项目获企业高度评价",
          desc: "基于决策树算法的智能简历OA筛选系统作为南华大学计算机学院优秀毕业设计项目，获得了多家企业HR的一致好评。"
        },
        {
          id: 5,
          img: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600&q=80",
          tag: "公司新闻",
          date: "2026-03-08",
          title: "云才科技与多家企业达成战略合作协议",
          desc: "云才科技宣布与多家企业达成战略合作，将智能招聘管理系统推广至更广泛的行业领域，助力企业数字化招聘转型。"
        },
        {
          id: 6,
          img: "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=600&q=80",
          tag: "行业资讯",
          date: "2026-02-20",
          title: "决策树算法在人力资源管理中的应用前景",
          desc: "随着机器学习技术的成熟，决策树算法凭借其可解释性强、实现简单的特点，在企业人才筛选与评估场景中展现出广阔的应用前景。"
        },
        {
          id: 7,
          img: "https://images.unsplash.com/photo-1600880292203-757bb62b4baf?w=600&q=80",
          tag: "产品更新",
          date: "2026-05-10",
          title: "智能客服模块上线：AI助力招聘咨询服务",
          desc: "基于自然语言处理技术的智能客服模块正式上线，求职者可通过在线对话获取职位详情、企业文化及常见问题解答，大幅提升用户体验。"
        },
        {
          id: 8,
          img: "https://images.unsplash.com/photo-1552664730-d307ca884978?w=600&q=80",
          tag: "公司新闻",
          date: "2026-05-01",
          title: "云才科技荣获2026年度HR科技创新奖",
          desc: "在刚刚落幕的2026中国人力资本科技峰会上，云才科技凭借其创新的决策树智能筛选系统荣获'年度HR科技创新奖'，行业影响力再上新台阶。"
        },
        {
          id: 9,
          img: "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=600&q=80",
          tag: "行业资讯",
          date: "2026-04-20",
          title: "AI招聘市场持续升温，多家企业加速布局",
          desc: "随着人才争夺战日益激烈，AI驱动的智能招聘工具正成为企业的核心需求。云才科技将持续加大研发投入，保持在智能招聘赛道的领先优势。"
        }
      ]
    }
  },
  computed: {
    filteredNews() {
      if (this.activeCategory === "全部") return this.newsList;
      return this.newsList.filter(item => item.tag === this.activeCategory);
    },
    pagedNews() {
      const start = 0;
      const end = this.currentPage * this.pageSize;
      return this.filteredNews.slice(start, end);
    },
    hasMore() {
      return this.filteredNews.length > this.currentPage * this.pageSize;
    }
  },
  methods: {
    switchCategory(cat) {
      this.activeCategory = cat;
      this.currentPage = 1;
    },
    goDetail(id) {
      this.$router.push('/news/' + id);
    },
    loadMore() {
      this.currentPage++;
    }
  }
}
</script>

<style scoped>
.news-hero {
  background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
  padding: 80px 0 60px;
  text-align: center;
}

.news-hero-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 12px;
}

.news-hero-desc {
  font-size: 18px;
  color: #94a3b8;
  margin: 0;
}

.news-section {
  padding: 60px 0;
  background: var(--bg-body);
}

.news-categories {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 32px;
}

.news-cat {
  padding: 6px 18px;
  border: 1px solid var(--border-light);
  border-radius: 20px;
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
}

.news-cat:hover {
  border-color: var(--primary-light);
  color: var(--primary-light);
}

.news-cat.active {
  background: var(--primary);
  color: #fff;
  border-color: var(--primary);
}

.news-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 24px;
}

.news-item {
  overflow: hidden;
  border: 1px solid var(--border-light);
}

.news-item-img {
  height: 200px;
  overflow: hidden;
}

.news-item-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.news-item:hover .news-item-img img {
  transform: scale(1.05);
}

.news-item-content {
  padding: 20px;
}

.news-item-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.news-item-tag {
  font-size: 12px;
  padding: 2px 10px;
  background: var(--bg-light);
  color: var(--primary-light);
  border-radius: 4px;
  font-weight: 500;
}

.news-item-date {
  font-size: 13px;
  color: var(--text-muted);
}

.news-item-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px;
  line-height: 1.4;
}

.news-item-desc {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0 0 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-item-link {
  font-size: 14px;
  color: var(--primary-light);
  font-weight: 500;
}

.btn-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.news-item {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.news-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.news-more {
  text-align: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .news-hero {
    padding: 60px 0 40px;
  }
  .news-hero-title {
    font-size: 28px;
  }
  .news-hero-desc {
    font-size: 16px;
  }
  .news-list {
    grid-template-columns: 1fr;
  }
}
</style>
