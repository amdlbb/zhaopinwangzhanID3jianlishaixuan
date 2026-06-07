<template>
  <div class="cs-root">
    <!-- 头部 -->
    <div class="cs-header">
      <span class="cs-header-title">AI 客服 · 历史会话</span>
      <span class="cs-header-close" @click="$emit('close')">✕</span>
    </div>

    <!-- 主体：左栏 + 右栏 -->
    <div class="cs-body">
      <!-- 左栏 -->
      <SessionList
        :sessions="sessions"
        :activeId="currentSessionId"
        @select="selectSession"
        @remove="handleRemove"
      />

      <!-- 右栏 -->
      <div class="cs-chat">
        <div class="cs-messages" ref="msgArea">
          <div v-if="messages.length === 0 && !isStreaming" class="cs-empty">
            <div class="cs-empty-icon">🤖</div>
            <div>选择左侧会话或输入问题开始对话</div>
          </div>

          <div v-for="(msg, idx) in messages" :key="idx" class="cs-msg" :class="msg.role">
            <div class="cs-msg-bubble">{{ msg.content }}</div>
            <div v-if="msg.role==='assistant' && msg.sources && msg.sources.length" class="cs-sources">
              <div class="cs-sources-toggle" @click="toggleSources(idx)">📎 参考来源</div>
              <div v-if="expandedSources.includes(idx)" class="cs-sources-list">
                <div v-for="(s, si) in msg.sources" :key="si" class="cs-source-item">
                  <span class="cs-source-title">{{ s.title }}</span>
                  <span class="cs-source-score">{{ (s.score*100).toFixed(0) }}%</span>
                </div>
              </div>
            </div>
          </div>

          <div v-if="isStreaming" class="cs-msg assistant">
            <div class="cs-msg-bubble">{{ streamBuffer }}<span class="cs-cursor">|</span></div>
          </div>
        </div>

        <div class="cs-input">
          <input v-model="inputText" type="text" placeholder="输入问题..."
            :disabled="isStreaming" @keyup.enter="send" />
          <button class="cs-send-btn" :disabled="!inputText.trim()||isStreaming" @click="send">
            {{ isStreaming?'···':'发送' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SessionList from '../SessionList/index.vue'
import { reqSendMessage, reqSearchSession, reqGetSession, reqRemoveSession } from '@/api'

export default {
  name: 'ChatSession',
  components: { SessionList },
  data() {
    return {
      sessions: [],
      currentSessionId: null,
      messages: [],
      inputText: '',
      isStreaming: false,
      streamBuffer: '',
      streamCtrl: null,
      expandedSources: [],
    }
  },
  mounted() { this.loadSessions() },
  methods: {
    async loadSessions() {
      try {
        const res = await reqSearchSession()
        if (res.code === 200 && res.data) {
          this.sessions = res.data.sessions || []
          if (this.sessions.length && !this.currentSessionId) {
            this.selectSession(this.sessions[0].id)
          }
        }
      } catch(e) { console.error(e) }
    },
    async selectSession(id) {
      this.currentSessionId = id
      try {
        const res = await reqGetSession(id)
        if (res.code === 200 && res.data) {
          this.messages = res.data.messages || []
          this.$nextTick(() => this.scrollToBottom())
        }
      } catch(e) { console.error(e) }
    },
    async handleRemove(id) {
      const res = await reqRemoveSession(id)
      if (res.code === 200) {
        this.sessions = this.sessions.filter(s => s.id !== id)
        if (this.currentSessionId === id) {
          this.currentSessionId = null
          this.messages = []
          if (this.sessions.length) this.selectSession(this.sessions[0].id)
        }
      }
    },
    send() {
      const q = this.inputText.trim()
      if (!q || this.isStreaming) return
      this.messages.push({ role: 'user', content: q, sources: [] })
      this.inputText = ''
      this.isStreaming = true
      this.streamBuffer = ''
      this.$nextTick(() => this.scrollToBottom())

      this.streamCtrl = reqSendMessage(q, this.currentSessionId, {
        onMeta: (data) => {
          if (data.code !== 200) { this.streamBuffer = data.msg || '请求失败'; return }
          if (data.session_id && !this.currentSessionId) this.currentSessionId = data.session_id
        },
        onToken: (token) => {
          this.streamBuffer += token
          this.$nextTick(() => this.scrollToBottom())
        },
        onDone: (data) => {
          this.messages.push({ role: 'assistant', content: this.streamBuffer, sources: data.sources || [] })
          this.streamBuffer = ''
          this.isStreaming = false
          this.loadSessions()
          this.$nextTick(() => this.scrollToBottom())
        },
        onError: () => {
          if (this.streamBuffer) this.messages.push({ role: 'assistant', content: this.streamBuffer + '\n[连接中断]', sources: [] })
          this.streamBuffer = ''
          this.isStreaming = false
          this.loadSessions()
        },
      })
    },
    toggleSources(idx) {
      const p = this.expandedSources.indexOf(idx)
      if (p > -1) this.expandedSources.splice(p, 1)
      else this.expandedSources.push(idx)
    },
    scrollToBottom() {
      const el = this.$refs.msgArea
      if (el) el.scrollTop = el.scrollHeight
    },
  },
  beforeDestroy() { if (this.streamCtrl) this.streamCtrl.abort() },
}
</script>

<style scoped>
.cs-root { height: 100vh; display: flex; flex-direction: column; background: #fff; }
.cs-header { height: 48px; display: flex; align-items: center; justify-content: space-between; padding: 0 16px; border-bottom: 1px solid var(--border-light,#e2e8f0); flex-shrink: 0; background: var(--primary,#1a3a5c); color: #fff; }
.cs-header-title { font-size: 15px; font-weight: 600; }
.cs-header-close { font-size: 18px; cursor: pointer; opacity: 0.8; padding: 4px; }
.cs-header-close:hover { opacity: 1; }
.cs-body { flex: 1; display: flex; overflow: hidden; }
.cs-chat { flex: 1; display: flex; flex-direction: column; min-width: 0; }
.cs-messages { flex: 1; overflow-y: auto; padding: 12px; background: #f7f8f9; }
.cs-empty { text-align: center; padding: 60px 20px; color: var(--text-muted,#a0aec0); font-size: 14px; }
.cs-empty-icon { font-size: 40px; margin-bottom: 12px; }
.cs-msg { margin-bottom: 12px; display: flex; flex-direction: column; }
.cs-msg.user { align-items: flex-end; }
.cs-msg.assistant { align-items: flex-start; }
.cs-msg-bubble { max-width: 85%; padding: 10px 14px; border-radius: 8px; font-size: 14px; line-height: 1.6; word-break: break-word; white-space: pre-wrap; }
.cs-msg.user .cs-msg-bubble { background: var(--primary,#1a3a5c); color: #fff; }
.cs-msg.assistant .cs-msg-bubble { background: #fff; color: var(--text-primary,#1a202c); box-shadow: 0 1px 3px rgba(0,0,0,.06); }
.cs-cursor { animation: blink 1s step-end infinite; color: var(--primary-light,#2c5282); }
@keyframes blink { 50% { opacity: 0; } }
.cs-sources { font-size: 12px; margin-top: 4px; padding-left: 4px; }
.cs-sources-toggle { color: var(--primary-light,#2c5282); cursor: pointer; user-select: none; }
.cs-sources-toggle:hover { text-decoration: underline; }
.cs-sources-list { margin-top: 4px; padding: 6px 10px; background: #edf2f7; border-radius: 6px; }
.cs-source-item { display: flex; justify-content: space-between; padding: 2px 0; }
.cs-source-title { color: var(--text-secondary,#4a5568); max-width: 70%; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.cs-source-score { color: var(--text-muted,#a0aec0); }
.cs-input { display: flex; padding: 10px 12px; border-top: 1px solid var(--border-light,#e2e8f0); background: #fff; flex-shrink: 0; }
.cs-input input { flex: 1; height: 38px; padding: 0 12px; border: 1px solid var(--border-light,#e2e8f0); border-radius: 6px; font-size: 14px; outline: none; }
.cs-input input:focus { border-color: var(--primary-light,#2c5282); }
.cs-send-btn { margin-left: 8px; padding: 0 20px; height: 38px; background: var(--primary,#1a3a5c); color: #fff; border: none; border-radius: 6px; font-size: 14px; cursor: pointer; transition: background .2s; white-space: nowrap; }
.cs-send-btn:hover:not(:disabled) { background: var(--primary-light,#2c5282); }
.cs-send-btn:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
