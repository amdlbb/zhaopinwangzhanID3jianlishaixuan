<template>
  <div id="app">
    <NavBar></NavBar>
    <BlackDiv></BlackDiv>
    <router-view></router-view>
    <el-backtop><span class="iconfont icon-xiangshang1"></span></el-backtop>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'
import BlackDiv from '@/components/BlackDiv'
export default {
  name: 'App',
  components: {
    NavBar,
    BlackDiv
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
