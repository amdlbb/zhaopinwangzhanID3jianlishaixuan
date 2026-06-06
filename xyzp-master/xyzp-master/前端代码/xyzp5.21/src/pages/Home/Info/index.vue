<template>
    <div class="infoDiv">
        <!-- 公司简介快览 -->
        <section class="home-section home-intro">
            <div class="page-container">
                <h2 class="section-title">智能招聘，从云才开始</h2>
                <p class="section-subtitle">专注企业智能招聘管理，用算法让人才匹配更精准</p>
                <div class="intro-cards">
                    <div class="intro-card card">
                        <div class="intro-card-icon">
                            <img src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=80&q=80" alt="智能筛选图标图片">
                        </div>
                        <h3>智能简历筛选</h3>
                        <p>基于ID3决策树算法，自动判定简历匹配度</p>
                    </div>
                    <div class="intro-card card">
                        <div class="intro-card-icon">
                            <img src="https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=80&q=80" alt="流程管理图标图片">
                        </div>
                        <h3>招聘流程管理</h3>
                        <p>覆盖发布、筛选、面试、录取全流程</p>
                    </div>
                    <div class="intro-card card">
                        <div class="intro-card-icon">
                            <img src="https://images.unsplash.com/photo-1520607162513-77705c0f0dbf?w=80&q=80" alt="数据分析图标图片">
                        </div>
                        <h3>数据统计分析</h3>
                        <p>多维数据看板，驱动招聘决策优化</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- 核心数据 -->
        <section class="home-section home-stats">
            <div class="page-container">
                <div class="stats-row">
                    <div class="stats-row-item">
                        <span class="stats-row-num">200+</span>
                        <span class="stats-row-label">合作企业</span>
                    </div>
                    <div class="stats-row-item">
                        <span class="stats-row-num">50万+</span>
                        <span class="stats-row-label">处理简历</span>
                    </div>
                    <div class="stats-row-item">
                        <span class="stats-row-num">80%</span>
                        <span class="stats-row-label">效率提升</span>
                    </div>
                    <div class="stats-row-item">
                        <span class="stats-row-num">98%</span>
                        <span class="stats-row-label">匹配准确率</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- 热门职位 -->
        <section class="home-section home-jobs">
            <div class="page-container">
                <h2 class="section-title">热门招聘职位</h2>
                <p class="section-subtitle">各行业优质岗位，等你来投递</p>
                <div class="job-cards">
                    <div class="job-card card" v-for="index in 6" :key="index">
                        <div class="job-card-top">
                            <div class="job-card-avatar">
                                <img src="https://image0.lietou-static.com/bg_white_222x222/5bfe919474719d35745adb7903a.png" alt="企业logo图片">
                            </div>
                            <div class="job-card-info">
                                <h4>高级前端工程师</h4>
                                <span class="job-card-company">科技公司</span>
                            </div>
                        </div>
                        <div class="job-card-tags">
                            <span class="job-tag">15k-25k</span>
                            <span class="job-tag">本科</span>
                            <span class="job-tag">3-5年</span>
                        </div>
                        <div class="job-card-bottom">
                            <span class="job-card-location">北京 · 海淀</span>
                            <span class="job-card-status">正在热招</span>
                        </div>
                    </div>
                </div>
                <div class="home-jobs-more">
                    <a href="/workhome" class="btn-outline">查看全部职位 →</a>
                </div>
            </div>
        </section>

        <!-- 新闻动态预览 -->
        <section class="home-section home-news">
            <div class="page-container">
                <h2 class="section-title">新闻动态</h2>
                <p class="section-subtitle">了解云才科技最新资讯</p>
                <div class="news-pre-list">
                    <div class="news-pre-item" v-for="index in 3" :key="index">
                        <div class="news-pre-img">
                            <img src="https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=400&q=80" alt="新闻配图">
                        </div>
                        <div class="news-pre-content">
                            <span class="news-pre-date">2026-04-28</span>
                            <h4 class="news-pre-title">云才科技智能简历筛选系统正式上线运行</h4>
                            <p class="news-pre-desc">经过数月的研发与测试，云才科技智能简历OA筛选系统正式上线...</p>
                            <a href="/news" class="news-pre-link">阅读更多 →</a>
                        </div>
                    </div>
                </div>
                <div class="home-news-more">
                    <a href="/news" class="btn-outline">查看更多动态 →</a>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
export default {
    name: "Info"
}
</script>

<style scoped>
.home-section {
    padding: 60px 0;
}

.home-intro {
    background: #fff;
}

/* 介绍卡片 */
.intro-cards {
    display: flex;
    gap: 24px;
    justify-content: center;
}

.intro-card {
    flex: 1;
    max-width: 320px;
    padding: 32px 24px;
    text-align: center;
    border: 1px solid var(--border-light);
}

.intro-card-icon {
    width: 64px;
    height: 64px;
    margin: 0 auto 16px;
    border-radius: 12px;
    overflow: hidden;
}

.intro-card-icon img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.intro-card h3 {
    font-size: 17px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0 0 8px;
}

.intro-card p {
    font-size: 14px;
    color: var(--text-secondary);
    margin: 0;
    line-height: 1.6;
}

/* 数据统计 */
.home-stats {
    background: var(--primary);
    padding: 48px 0;
}

.stats-row {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    gap: 20px;
}

.stats-row-item {
    text-align: center;
    color: #fff;
}

.stats-row-num {
    display: block;
    font-size: 36px;
    font-weight: 700;
    margin-bottom: 6px;
}

.stats-row-label {
    font-size: 16px;
    color: #94a3b8;
}

/* 热门职位 */
.home-jobs {
    background: var(--bg-body);
}

.job-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    margin-bottom: 32px;
}

.job-card {
    padding: 20px;
    border: 1px solid var(--border-light);
    cursor: pointer;
    transition: all 0.2s;
}

.job-card:hover {
    border-color: var(--primary-light);
}

.job-card-top {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
}

.job-card-avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    overflow: hidden;
    flex-shrink: 0;
    background: var(--bg-light);
}

.job-card-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.job-card-info h4 {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0 0 4px;
}

.job-card-company {
    font-size: 13px;
    color: var(--text-muted);
}

.job-card-tags {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 12px;
}

.job-tag {
    font-size: 12px;
    padding: 3px 10px;
    background: var(--bg-light);
    color: var(--primary-light);
    border-radius: 4px;
    font-weight: 500;
}

.job-card-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid var(--border-light);
}

.job-card-location {
    font-size: 13px;
    color: var(--text-muted);
}

.job-card-status {
    font-size: 12px;
    padding: 2px 8px;
    background: #ebf8ff;
    color: #2b6cb0;
    border-radius: 4px;
}

.home-jobs-more,
.home-news-more {
    text-align: center;
}

/* 新闻预览 */
.home-news {
    background: #fff;
}

.news-pre-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 24px;
    margin-bottom: 32px;
}

.news-pre-item {
    display: flex;
    gap: 16px;
    padding: 20px;
    border: 1px solid var(--border-light);
    border-radius: var(--radius-md);
    transition: box-shadow 0.2s;
}

.news-pre-item:hover {
    box-shadow: var(--shadow-sm);
}

.news-pre-img {
    width: 120px;
    height: 90px;
    border-radius: 6px;
    overflow: hidden;
    flex-shrink: 0;
}

.news-pre-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.news-pre-content {
    flex: 1;
    min-width: 0;
}

.news-pre-date {
    font-size: 12px;
    color: var(--text-muted);
}

.news-pre-title {
    font-size: 15px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 4px 0;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.news-pre-desc {
    font-size: 13px;
    color: var(--text-secondary);
    margin: 0 0 6px;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.news-pre-link {
    font-size: 13px;
    color: var(--primary-light);
    font-weight: 500;
}

@media (max-width: 768px) {
    .home-section {
        padding: 40px 0;
    }
    .intro-cards {
        flex-direction: column;
        align-items: center;
    }
    .intro-card {
        max-width: 100%;
    }
    .stats-row-num {
        font-size: 28px;
    }
    .job-cards {
        grid-template-columns: 1fr;
    }
    .news-pre-list {
        grid-template-columns: 1fr;
    }
    .news-pre-item {
        flex-direction: column;
    }
    .news-pre-img {
        width: 100%;
        height: 160px;
    }
}
</style>
