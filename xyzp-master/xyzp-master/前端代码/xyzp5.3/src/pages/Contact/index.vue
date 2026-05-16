<template>
  <div class="contact-page">
    <!-- Hero 区 -->
    <section class="contact-hero">
      <div class="page-container">
        <h1 class="contact-hero-title">联系我们</h1>
        <p class="contact-hero-desc">期待与您的合作，欢迎随时联络</p>
      </div>
    </section>

    <!-- 联系信息 -->
    <section class="contact-section">
      <div class="page-container">
        <div class="contact-grid">
          <div class="contact-info-col">
            <div class="contact-info-card card">
              <h3>联系方式</h3>
              <ul class="contact-detail-list">
                <li>
                  <span class="contact-label">公司地址</span>
                  <span class="contact-value">湖南省衡阳市南华大学红湘校区</span>
                </li>
                <li>
                  <span class="contact-label">联系电话</span>
                  <span class="contact-value">0734-8888888</span>
                </li>
                <li>
                  <span class="contact-label">电子邮箱</span>
                  <span class="contact-value">contact@yuncaihr.com</span>
                </li>
                <li>
                  <span class="contact-label">工作时间</span>
                  <span class="contact-value">周一至周五 9:00 – 18:00</span>
                </li>
              </ul>
            </div>

            <div class="contact-info-card card">
              <h3>业务合作</h3>
              <ul class="contact-detail-list">
                <li>
                  <span class="contact-label">商务合作</span>
                  <span class="contact-value">bd@yuncaihr.com</span>
                </li>
                <li>
                  <span class="contact-label">技术支持</span>
                  <span class="contact-value">support@yuncaihr.com</span>
                </li>
                <li>
                  <span class="contact-label">媒体联系</span>
                  <span class="contact-value">pr@yuncaihr.com</span>
                </li>
              </ul>
            </div>
          </div>

          <div class="contact-form-col">
            <div class="contact-form-card card">
              <h3>在线留言</h3>
              <p class="contact-form-desc">请填写以下信息，我们将尽快与您联系</p>
              <div class="contact-form">
                <div class="form-row">
                  <div class="form-group">
                    <label>您的姓名</label>
                    <input type="text" placeholder="请输入姓名" v-model="form.name">
                  </div>
                  <div class="form-group">
                    <label>联系邮箱</label>
                    <input type="email" placeholder="请输入邮箱" v-model="form.email">
                  </div>
                </div>
                <div class="form-group">
                  <label>联系电话</label>
                  <input type="text" placeholder="请输入电话" v-model="form.phone">
                </div>
                <div class="form-group">
                  <label>留言内容</label>
                  <textarea rows="4" placeholder="请输入您的留言..." v-model="form.message"></textarea>
                </div>
                <button class="btn-primary" @click="submitForm">提交留言</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 地图占位 -->
    <section class="contact-map">
      <div class="page-container">
        <div class="map-placeholder">
          <span>地图占位 — 公司位置：湖南省衡阳市</span>
        </div>
      </div>
    </section>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer'
export default {
  name: "Contact",
  components: {
    Footer
  },
  data() {
    return {
      form: {
        name: '',
        email: '',
        phone: '',
        message: ''
      }
    }
  },
  methods: {
    submitForm() {
      alert('感谢您的留言，我们会尽快与您联系！(此功能为静态展示)')
      this.form = { name: '', email: '', phone: '', message: '' }
    }
  }
}
</script>

<style scoped>
.contact-hero {
  background: linear-gradient(135deg, #0f2640 0%, #1a3a5c 50%, #2c5282 100%);
  padding: 80px 0 60px;
  text-align: center;
}

.contact-hero-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 12px;
}

.contact-hero-desc {
  font-size: 18px;
  color: #94a3b8;
  margin: 0;
}

.contact-section {
  padding: 60px 0;
  background: var(--bg-body);
}

.contact-grid {
  display: flex;
  gap: 32px;
  align-items: flex-start;
}

.contact-info-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.contact-info-card,
.contact-form-card {
  padding: 28px;
  border: 1px solid var(--border-light);
}

.contact-info-card h3,
.contact-form-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-light);
}

.contact-detail-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.contact-detail-list li {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.contact-label {
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
}

.contact-value {
  font-size: 15px;
  color: var(--text-primary);
}

.contact-form-col {
  flex: 1.3;
}

.contact-form-desc {
  font-size: 14px;
  color: var(--text-secondary);
  margin: -8px 0 20px;
}

.contact-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-row .form-group {
  flex: 1;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  color: var(--text-primary);
  font-family: var(--font-family);
  transition: border 0.2s;
  outline: none;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px rgba(44, 82, 130, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.contact-map {
  padding: 0 0 60px;
  background: var(--bg-body);
}

.map-placeholder {
  height: 300px;
  background: var(--bg-light);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  font-size: 16px;
  border: 2px dashed var(--border-light);
}

@media (max-width: 768px) {
  .contact-hero {
    padding: 60px 0 40px;
  }
  .contact-hero-title {
    font-size: 28px;
  }
  .contact-hero-desc {
    font-size: 16px;
  }
  .contact-grid {
    flex-direction: column;
    gap: 20px;
  }
  .form-row {
    flex-direction: column;
    gap: 16px;
  }
  .map-placeholder {
    height: 200px;
  }
}
</style>
