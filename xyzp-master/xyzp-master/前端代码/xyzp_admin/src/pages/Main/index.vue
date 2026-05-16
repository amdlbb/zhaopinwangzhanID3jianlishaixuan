<template>
    <div class="mainDiv">
        <NavBar></NavBar>
        <router-view></router-view>
    </div>
</template>

<script>
import NavBar from "@/components/NavBar"
export default {
    name: "Main",
    components: {
        NavBar
    },
    data() {
        return {

        }
    },
}
</script>

<style>


@media screen and (min-width: 1200px) {
    .mainDiv {
        display: flex;
    }
    
}

@media screen and (max-width: 1199px) {
   
}
</style>