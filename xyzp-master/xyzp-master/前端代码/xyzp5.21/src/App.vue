<template>
  <div id="app">
    <NavBar></NavBar>
    <BlackDiv></BlackDiv>
    <FloatButton></FloatButton>
    <router-view></router-view>
    <el-backtop><span class="iconfont icon-xiangshang1"></span></el-backtop>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'
import BlackDiv from '@/components/BlackDiv'
import FloatButton from "@/components/FloatButton/index.vue";
export default {
  name: 'App',
  components: {
    NavBar,
    BlackDiv,
    FloatButton
  }
}
</script>

<style>
html,
body,
#app{
    height: 100%;
    margin: 0;
    padding: 0;
    background-color: rgb(247, 248, 249);
}


</style>
