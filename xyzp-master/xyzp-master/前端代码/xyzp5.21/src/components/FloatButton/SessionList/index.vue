<template>
  <div class="session-list">
    <div class="session-list-title">历史会话</div>
    <div class="session-list-scroll">
      <div
        v-for="s in sessions"
        :key="s.id"
        class="session-item"
        :class="{ active: s.id === activeId }"
        @click="$emit('select', s.id)"
      >
        <div class="session-item-title">{{ s.title || '新会话' }}</div>
        <div class="session-item-meta">
          <span>{{ s.message_count }}条</span>
          <span>{{ shortTime(s.updated_at) }}</span>
        </div>
        <span class="session-item-del" @click.stop="$emit('remove', s.id)">×</span>
      </div>
      <div v-if="sessions.length === 0" class="session-empty">
        暂无历史会话
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SessionList',
  props: {
    sessions: { type: Array, default: () => [] },
    activeId: { type: Number, default: null },
  },
  methods: {
    shortTime(dateStr) {
      if (!dateStr) return ''
      try {
        const d = new Date(dateStr)
        const h = String(d.getHours()).padStart(2, '0')
        const m = String(d.getMinutes()).padStart(2, '0')
        return `${h}:${m}`
      } catch {
        return ''
      }
    },
  },
}
</script>

<style scoped>
.session-list {
  width: 140px;
  min-width: 140px;
  border-right: 1px solid var(--border-light, #e2e8f0);
  display: flex;
  flex-direction: column;
  background: #f8fafc;
}

.session-list-title {
  padding: 12px 10px;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary, #1a202c);
  border-bottom: 1px solid var(--border-light, #e2e8f0);
  flex-shrink: 0;
}

.session-list-scroll {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  position: relative;
  padding: 10px 8px 10px 10px;
  cursor: pointer;
  border-bottom: 1px solid var(--border-light, #e2e8f0);
  transition: background 0.15s;
}

.session-item:hover {
  background: #eef2f7;
}

.session-item.active {
  background: #e6edf5;
  border-left: 3px solid var(--primary, #1a3a5c);
  padding-left: 7px;
}

.session-item-title {
  font-size: 13px;
  color: var(--text-primary, #1a202c);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  word-break: break-all;
  margin-bottom: 4px;
}

.session-item-meta {
  font-size: 11px;
  color: var(--text-muted, #a0aec0);
  display: flex;
  justify-content: space-between;
}

.session-item-del {
  position: absolute;
  top: 4px;
  right: 6px;
  font-size: 14px;
  color: #cbd5e0;
  cursor: pointer;
  display: none;
  line-height: 1;
}

.session-item:hover .session-item-del {
  display: block;
}

.session-item-del:hover {
  color: var(--accent, #c53030);
}

.session-empty {
  padding: 20px 10px;
  text-align: center;
  font-size: 12px;
  color: var(--text-muted, #a0aec0);
}
</style>
