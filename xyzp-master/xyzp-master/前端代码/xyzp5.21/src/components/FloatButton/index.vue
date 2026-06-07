<template>
  <div>
    <!-- AI 客服悬浮按钮 -->
    <div class="ai-float-btn" @click="togglePanel" :class="{ active: panelVisible }">
      <span class="ai-float-icon">💬</span>
      <span class="ai-float-text">AI客服</span>
    </div>

    <!-- AI 客服面板 -->
    <transition name="panel-slide">
      <div v-if="panelVisible" class="ai-panel-overlay" @click.self="panelVisible = false">
        <div class="ai-panel">
          <!-- 未登录：临时会话 -->
          <Chat v-if="!isLogin" @close="panelVisible = false" />
          <!-- 已登录：历史会话 -->
          <ChatSession v-else @close="panelVisible = false" />
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Chat from './Chat/index.vue'
import ChatSession from './ChatSession/index.vue'
import { reqCheckLogin } from '@/api'

export default {
  name: 'FloatButton',
  components: { Chat, ChatSession },
  data() {
    return {
      panelVisible: false,
      isLogin: false,
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    async checkLoginStatus() {
      try {
        const token = localStorage.getItem('token')
        if (!token) { this.isLogin = false; return }
        const res = await reqCheckLogin()
        if (res.code === 200 && res.data) {
          this.isLogin = res.data.isLoggedIn === true
        } else {
          this.isLogin = false
        }
      } catch {
        this.isLogin = false
      }
    },
    togglePanel() {
      this.panelVisible = !this.panelVisible
      if (this.panelVisible) this.checkLoginStatus()
    },
  },
}
</script>

<style scoped>
.ai-float-btn {
  position: fixed; right: 24px; bottom: 100px; z-index: 1000;
  width: 56px; height: 56px; border-radius: 50%;
  background: var(--primary, #1a3a5c); color: #fff;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  cursor: pointer; box-shadow: 0 4px 16px rgba(26,58,92,0.35);
  transition: all 0.3s ease; user-select: none;
}
.ai-float-btn:hover { transform: scale(1.1); box-shadow: 0 6px 24px rgba(26,58,92,0.5); background: var(--primary-light, #2c5282); }
.ai-float-btn.active { background: var(--accent, #c53030); }
.ai-float-icon { font-size: 22px; line-height: 1; }
.ai-float-text { font-size: 10px; margin-top: 2px; }

.ai-panel-overlay { position: fixed; top: 0; right: 0; bottom: 0; left: 0; z-index: 2000; background: rgba(0,0,0,0.25); }
.ai-panel { position: fixed; top: 0; right: 0; width: 460px; height: 100vh; background: #fff; box-shadow: -4px 0 20px rgba(0,0,0,0.12); display: flex; flex-direction: column; overflow: hidden; }

.panel-slide-enter-active, .panel-slide-leave-active { transition: opacity 0.3s ease; }
.panel-slide-enter-active .ai-panel, .panel-slide-leave-active .ai-panel { transition: transform 0.3s ease; }
.panel-slide-enter .ai-panel, .panel-slide-leave-to .ai-panel { transform: translateX(100%); }
.panel-slide-enter-to .ai-panel, .panel-slide-leave .ai-panel { transform: translateX(0); }
</style>
