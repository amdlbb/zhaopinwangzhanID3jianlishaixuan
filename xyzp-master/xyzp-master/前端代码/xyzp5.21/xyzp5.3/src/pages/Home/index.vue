<template>
    <div class="homeDiv">
        <Swiper></Swiper>
        <Info></Info>
        <Footer></Footer>
    </div>
</template>

<script>
import Swiper from './Swiper'
import Info from './Info'
import Footer from '@/components/Footer'
export default {
    name: "Home",
    components: {
        Swiper,
        Info,
        Footer
    }
}
</script>

<style>
.homeDiv{
    position: relative;
}
</style>
