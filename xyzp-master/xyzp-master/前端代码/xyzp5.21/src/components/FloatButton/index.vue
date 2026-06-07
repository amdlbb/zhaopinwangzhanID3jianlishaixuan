<template>
  <div >
<!--todo-->
<!--    ai客服悬浮按钮固定在屏幕右下方，注册在app.vue，mount时获取登录状态状态保存在isLogin中，点击时根据-->
<!--    isLogin的值决定点击按钮跳转到哪个Chat路由（右侧伸出聊天界面对话框，宽度为屏幕三分之一到四分之一）-->
<!--    获取登录状态接口：/api/authorization/check-login，C:\bs\xyzp-master\xyzp-master\后端代码\xyzp\src\main\java\com\abc\xyzp\controller\AuthorizationController.java-->
<!--    {
    path: '/chat',
    component: () => import('@/components/FloatButton/Chat'),
    },
    {
    path: '/chatsession',
    component: () => import('@/components/FloatButton/ChatSession'),
    }-->
  </div>
</template>

<script>
export default {
  name: 'FloatButton',
  data() {
    return {
      isLogin: false
    }
  },
  mounted() {

  },
  methods: {}
}

</script>

<style>

</style>