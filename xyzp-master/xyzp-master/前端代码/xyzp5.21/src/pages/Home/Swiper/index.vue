<template>
    <div class="swiperDiv">
        <el-carousel height="520px">
            <el-carousel-item>
                <div class="swiper-slide slide-1">
                    <div class="swiper-content page-container">
                        <h2>智能简历OA筛选系统</h2>
                        <p>基于决策树算法，让简历筛选更高效、更客观</p>
                        <a href="/product" class="swiper-btn">了解更多</a>
                    </div>
                </div>
            </el-carousel-item>
            <el-carousel-item>
                <div class="swiper-slide slide-2">
                    <div class="swiper-content page-container">
                        <h2>全流程招聘管理</h2>
                        <p>从职位发布到录取入职，一站式解决方案</p>
                        <a href="/workhome" class="swiper-btn">浏览职位</a>
                    </div>
                </div>
            </el-carousel-item>
            <el-carousel-item>
                <div class="swiper-slide slide-3">
                    <div class="swiper-content page-container">
                        <h2>以数据驱动人才决策</h2>
                        <p>多维度数据分析看板，助力HR精准决策</p>
                        <a href="/about" class="swiper-btn">关于我们</a>
                    </div>
                </div>
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script>
export default {
    name: 'Swiper'
}
</script>

<style scoped>
.swiperDiv .el-carousel__container {
    height: 520px;
}

.swiper-slide {
    height: 100%;
    display: flex;
    align-items: center;
    background-size: cover;
    background-position: center;
}

.slide-1 {
    background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
    background-image: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%), url('https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=1920&q=80');
    background-blend-mode: overlay;
}

.slide-2 {
    background: linear-gradient(135deg, #1a3a5c 0%, #2c5282 50%, #3b6fa5 100%);
    background-image: linear-gradient(135deg, #1a3a5c 0%, #2c5282 50%, #3b6fa5 100%), url('https://images.unsplash.com/photo-1521791136064-7986c2920216?w=1920&q=80');
    background-blend-mode: overlay;
}

.slide-3 {
    background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
    background-image: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%), url('https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=1920&q=80');
    background-blend-mode: overlay;
}

.swiper-content {
    width: 100%;
    padding: 0 20px;
}

.swiper-content h2 {
    font-size: 40px;
    font-weight: 700;
    color: #fff;
    margin: 0 0 16px;
    line-height: 1.2;
}

.swiper-content p {
    font-size: 18px;
    color: #94a3b8;
    margin: 0 0 32px;
}

.swiper-btn {
    display: inline-block;
    padding: 12px 32px;
    background: #fff;
    color: var(--primary);
    font-size: 16px;
    font-weight: 600;
    border-radius: 6px;
    text-decoration: none;
    transition: all 0.2s;
}

.swiper-btn:hover {
    background: var(--bg-light);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

@media screen and (max-width: 1000px) {
    .swiperDiv .el-carousel__container {
        height: 320px;
    }
    .swiper-content h2 {
        font-size: 26px;
    }
    .swiper-content p {
        font-size: 15px;
    }
    .swiper-btn {
        padding: 10px 24px;
        font-size: 14px;
    }
}

@media screen and (min-width: 1000px) {
    .swiperDiv img {
        width: 100%;
        height: 100%;
    }
}
</style>
