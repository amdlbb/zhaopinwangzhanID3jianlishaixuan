<template>
  <div class="tc-root">
    <!-- 头部 -->
    <div class="tc-header">
      <div>
        <span class="tc-header-dot">● 临时会话</span>
        <span class="tc-header-badge">未登录</span>
      </div>
      <span class="tc-header-close" @click="$emit('close')">✕</span>
    </div>

    <!-- 消息区 -->
    <div class="tc-messages" ref="msgArea">
      <div v-if="messages.length === 0 && !isStreaming" class="tc-empty">
        <div class="tc-empty-icon">🤖</div>
        <div>欢迎使用 AI 客服（临时会话）</div>
        <div class="tc-empty-tip">
          当前为临时会话模式，对话记录不会保存。<br/>
          登录后可查看历史会话记录。
        </div>
      </div>

      <div v-for="(msg, idx) in messages" :key="idx" class="tc-msg" :class="msg.role">
        <div class="tc-msg-bubble">{{ msg.content }}</div>
      </div>

      <div v-if="isStreaming" class="tc-msg assistant">
        <div class="tc-msg-bubble">{{ streamBuffer }}<span class="tc-cursor">|</span></div>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="tc-input">
      <input v-model="inputText" type="text" placeholder="输入问题..."
        :disabled="isStreaming" @keyup.enter="send" />
      <button class="tc-send-btn" :disabled="!inputText.trim() || isStreaming" @click="send">
        {{ isStreaming ? '···' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script>
import { reqTempAsk } from '@/api'

export default {
  name: 'Chat',
  data() {
    return {
      messages: [],
      inputText: '',
      isStreaming: false,
      streamBuffer: '',
      sessionId: null,
      streamCtrl: null,
    }
  },
  methods: {
    send() {
      const q = this.inputText.trim()
      if (!q || this.isStreaming) return
      this.messages.push({ role: 'user', content: q })
      this.inputText = ''
      this.isStreaming = true
      this.streamBuffer = ''
      this.$nextTick(() => this.scrollToBottom())

      this.streamCtrl = reqTempAsk(q, this.sessionId, {
        onMeta: (data) => {
          if (data.session_id) this.sessionId = data.session_id
        },
        onToken: (token) => {
          this.streamBuffer += token
          this.$nextTick(() => this.scrollToBottom())
        },
        onDone: () => {
          this.messages.push({ role: 'assistant', content: this.streamBuffer })
          this.streamBuffer = ''
          this.isStreaming = false
          this.$nextTick(() => this.scrollToBottom())
        },
        onError: () => {
          if (this.streamBuffer) this.messages.push({ role: 'assistant', content: this.streamBuffer + '\n[连接中断]' })
          this.streamBuffer = ''
          this.isStreaming = false
        },
      })
    },
    scrollToBottom() {
      const el = this.$refs.msgArea
      if (el) el.scrollTop = el.scrollHeight
    },
  },
  beforeDestroy() {
    if (this.streamCtrl) this.streamCtrl.abort()
  },
}
</script>

<style scoped>
.tc-root { height: 100vh; display: flex; flex-direction: column; background: #fff; }

.tc-header {
  height: 48px; display: flex; align-items: center; justify-content: space-between;
  padding: 0 16px; border-bottom: 1px solid var(--border-light, #e2e8f0);
  flex-shrink: 0; background: var(--primary, #1a3a5c); color: #fff;
}
.tc-header-dot { font-size: 14px; color: #90cdf4; }
.tc-header-badge {
  margin-left: 8px; padding: 1px 8px; border-radius: 10px;
  background: rgba(255,255,255,0.2); font-size: 11px; color: #fff;
}
.tc-header-close { font-size: 18px; cursor: pointer; opacity: 0.8; }
.tc-header-close:hover { opacity: 1; }

.tc-messages { flex: 1; overflow-y: auto; padding: 12px; background: #f7f8f9; }

.tc-empty { text-align: center; padding: 60px 20px; color: var(--text-muted, #a0aec0); font-size: 14px; }
.tc-empty-icon { font-size: 40px; margin-bottom: 12px; }
.tc-empty-tip { font-size: 12px; margin-top: 8px; line-height: 1.6; }

.tc-msg { margin-bottom: 12px; display: flex; flex-direction: column; }
.tc-msg.user { align-items: flex-end; }
.tc-msg.assistant { align-items: flex-start; }
.tc-msg-bubble {
  max-width: 85%; padding: 10px 14px; border-radius: 8px;
  font-size: 14px; line-height: 1.6; word-break: break-word; white-space: pre-wrap;
}
.tc-msg.user .tc-msg-bubble { background: var(--primary, #1a3a5c); color: #fff; }
.tc-msg.assistant .tc-msg-bubble { background: #fff; color: var(--text-primary, #1a202c); box-shadow: 0 1px 3px rgba(0,0,0,0.06); }

.tc-cursor { animation: blink 1s step-end infinite; color: var(--primary-light, #2c5282); }
@keyframes blink { 50% { opacity: 0; } }

.tc-input { display: flex; padding: 10px 12px; border-top: 1px solid var(--border-light, #e2e8f0); background: #fff; flex-shrink: 0; }
.tc-input input { flex: 1; height: 38px; padding: 0 12px; border: 1px solid var(--border-light, #e2e8f0); border-radius: 6px; font-size: 14px; outline: none; }
.tc-input input:focus { border-color: var(--primary-light, #2c5282); }
.tc-input input:disabled { background: #f7f8f9; }
.tc-send-btn {
  margin-left: 8px; padding: 0 20px; height: 38px; background: var(--primary, #1a3a5c);
  color: #fff; border: none; border-radius: 6px; font-size: 14px; cursor: pointer;
  transition: background 0.2s; white-space: nowrap;
}
.tc-send-btn:hover:not(:disabled) { background: var(--primary-light, #2c5282); }
.tc-send-btn:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
