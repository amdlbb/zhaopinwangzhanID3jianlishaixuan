<template>
    <div>
        <div class="text1">
            <h1>行业资讯与新闻动态</h1>
            <div class="btns">
                <a class="btn">媒体合作</a>
            </div>
        </div>
        <div class="container">
            <div class="top">
                <div class="nav">
                    <ul>
                        <li>
                            <span class="icon icon-all"></span>
                            <a target="_blank"><strong>全部资讯</strong></a>

                        </li>
                        <li>
                            <span class="icon icon-book"></span>
                            <a target="_blank"><strong>求职必读</strong></a>
                        </li>
                        <li>
                            <span class="icon icon-report"></span>
                            <a target="_blank"><strong>数据报告</strong></a>
                        </li>
                        <li>
                            <span class="icon icon-article"></span>
                            <a target="_blank"><strong>干货文章</strong></a>
                        </li>
                        <li>
                            <span class="icon icon-dynamic"></span>
                            <a target="_blank"><strong>公司动态</strong></a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="content">
                <a target="_blank">
                    <div class="floor">
                        <img src="./images/img.png"
                            alt="">
                        <div class="txt">
                            <h2>【海归必读】零经验？不慌！</h2>
                            <p>对于部分应届生，读书课程安排紧张，只是应付学业就已经非常吃力，再加上其它各种因素，根本没有时间、也没有机会实习。</p>
                        </div>
                    </div>
                </a>
                <a target="_blank">
                    <div class="floor1">
                        <img src="./images/img1.png"
                            alt="">
                        <div class="txt">
                            <h2>【求职必读】留学生如何准备背调？</h2>
                            <p>什么是企业背景调查？
                                通常在HR发放校招Offer之后，会通知候选人进行背景调查，</p>
                        </div>
                    </div>
                </a>
                <a target="_blank">
                    <div class="floor2">
                        <img src="./images/img2.png"
                            alt="">
                        <div class="txt">
                            <h2>【求职必读】关于内推那些事儿 </h2>
                            <p>又到了传说中金三银四的黄金求职期，想必大家在这段时间多多少少都听说过“内推”这个词。对于已经有过打工经历的打工人来说，这个词想必并不陌生。</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Information"
}
</script>

<style scoped>
a {
    cursor: pointer;
}

@media screen and (min-width: 1200px) {
    h1 {
        font-size: 36px;
        font-weight: 500;
        color: #fff;
        line-height: 50px;
        display: block;
        font-size: 2em;
        /* margin-block-start: 0.67em; */
        margin-block-end: 0.67em;
        margin-inline-start: 0px;
        margin-inline-end: 0px;
        font-weight: bold;
    }

    .text1 {
        text-align: center;
        background: url(https://img.bosszhipin.com/static/file/2022/lmmi44x97d1667814225827.jpg) center 0 no-repeat #3d4f5e;
        height: 140px;
    }

    .btn {
        width: 128px;
        height: 44px;
        background: #00bebd;
        border-radius: 12px;
        font-size: 20px;
        font-weight: 500;
        box-shadow: none;
        display: inline-block;
        line-height: 44px;
        color: #fff;
        border-radius: 24px;
        text-decoration: none;
    }

    * {
        margin: 0px;
        padding: 0px;
        list-style: none;
        font-size: 12px;
    }

    body {
        width: 100%;
        height: 100%;
        background-color: rgba(134, 188, 206, 0.512);
    }

    a.btn {
        margin-top: 12px;
    }

    .container {
        height: 100px;
    }

    .content {
        width: 50%;
        height: 100%;
        justify-content: center;
        display: flex;
        margin: 0, 0;
        top: auto;
    }

    .container a {
        text-decoration: none;
        color: #3d4f5e;
    }

    .content a {
        text-decoration: none;
        color: #3d4f5e;
    }

    .top {
        justify-content: center;
        display: flex;
        /* background-color:white; */
        height: 50px;
        top: 0px;
        position: relative;
    }

    .nav {
        position: absolute;
        align-items: center;
        justify-content: center;
        display: flex;
        width: 60%;
        height: 50px;
        background-color: #fff;
        border-radius: 10px;
        top: 0px;
    }

    .nav ul {
        position: absolute;
        top: 0px;
        width: 100%;
        height: 100%;
        align-items: center;
        justify-content: center;
        display: flex;
    }

    .nav ul li {
        width: 100%;
        align-items: center;
        justify-content: center;
        display: flex;
        margin: 0px, 0px;
        font-size: 25px;
    }

    .nav ul li img {
        margin: 5px;
        height: 25px;
        width: 25px;
        border-radius: 3px;
    }

    .floor {
        position: absolute;
        top: 270px;
        border-radius: 10px;
        background-color: #fff;
        width: 50%;
        height: auto;
        align-items: center;
        display: flex;
        margin-top: 0;
    }

    .floor1 {
        position: absolute;
        border-radius: 10px;
        top: 465px;
        background-color: #fff;
        width: 50%;
        height: 150px;
        align-items: center;
        display: flex;
    }

    .floor2 {
        position: absolute;
        border-radius: 10px;
        top: 640px;
        background-color: #fff;
        width: 50%;
        height: 150px;
        align-items: center;
        display: flex;
    }

    .floor img {
        width: 200px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;
    }

    .floor1 img {
        width: 200px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;
    }

    .floor2 img {
        width: 200px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;
    }

    .txt {
        position: relative;
        top: 20px;
        margin: 10px;
        width: 650px;
        height: 150px;
    }

    .txt h2 {
        font-size: 15px;
        margin-bottom: 15px;
    }

    .txt p {
        line-height: 1.5;
        color: #a3a3a3;
    }

    .icon:before {
        content: ' ';
        display: block;
        width: 26px;
        height: 26px;
        background: url(https://img.bosszhipin.com/static/file/2022/venq8cn1iy1667886253777.png) 0 0/26px auto no-repeat;
    }

    .icon-report:before {
        background-position: 0 -26px;
    }

    .icon-all:before {
        background-position: 0 -312px;
    }

    .icon-article:before {
        background-position: 0 -52px;
    }

    .icon-dynamic:before {
        background-position: 0 -78px;
    }
}

@media screen and (max-width:1199px) {
    .container a {
        text-decoration: none;
        color: #3d4f5e;
    }

    .content a {
        text-decoration: none;
        color: #3d4f5e;
    }

    .btn {
        width: 128px;
        height: 44px;
        background: #00bebd;
        border-radius: 12px;
        font-size: 20px;
        font-weight: 500;
        box-shadow: none;
        display: inline-block;
        line-height: 44px;
        color: #fff;
        border-radius: 24px;
        text-decoration: none;
    }

    h1 {
        font-size: 36px;
        font-weight: 500;
        color: #fff;
        line-height: 50px;
        display: block;
        font-size: 20px;
        /* margin-block-start: 0.67em; */
        margin-block-end: 0.67em;
        margin-inline-start: 0px;
        margin-inline-end: 0px;
        font-weight: bold;
    }

    .text1 {
        text-align: center;
        background: url(https://img.bosszhipin.com/static/file/2022/lmmi44x97d1667814225827.jpg) center 0 no-repeat #3d4f5e;
        height: 140px;
    }

    * {
        margin: 0px;
        padding: 0px;
        list-style: none;
        font-size: 12px;
    }

    body {
        width: 100%;
        height: 100%;
        background-color: rgba(134, 188, 206, 0.512);
    }

    .container {
        width: 100%;
        height: 100%;
    }

    .content {
        width: 50%;
        height: 100%;
        /* justify-content: center; */
        display: flex;
        margin: 0, 0;
    }

    .top {
        justify-content: center;
        display: flex;
        /* background-color: rgb(32, 61, 58); */
        height: 50px;
    }

    .nav {
        position: absolute;
        align-items: center;
        justify-content: center;
        display: flex;
        /* width: 60%; */
        height: 50px;
        background-color: #fff;
        border-radius: 10px;
        top: 189px;
    }

    .nav ul {
        width: 100%;
        height: 100%;
        align-items: center;
        justify-content: center;
        display: flex;
    }

    .nav ul li {
        width: 100%;
        align-items: center;
        justify-content: center;
        display: flex;
        margin: 0px, 0px;
        font-size: 25px;
    }

    .nav ul li img {
        margin: 5px;
        height: 25px;
        width: 25px;
        border-radius: 3px;
    }

    .floor {
        position: absolute;
        border-radius: 10px;
        background-color: #fff;
        width: 100%;
        height: 150px;
        align-items: center;
        display: flex;
        top: 265px;
    }

    .floor1 {
        position: absolute;
        border-radius: 10px;
        top: 442px;
        background-color: #fff;
        width: 100%;
        height: 150px;
        align-items: center;
        display: flex;
    }

    .floor2 {
        position: absolute;
        border-radius: 10px;
        top: 621px;
        background-color: #fff;
        width: 100%;
        height: 150px;
        align-items: center;
        display: flex;
    }

    .floor img {
        width: 140px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;

    }

    .floor2 img {
        width: 140px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;

    }

    .floor1 img {
        width: 140px;
        border-radius: 10px;
        height: 120px;
        margin: 10px;

    }

    .txt {
        position: relative;
        top: 20px;
        margin: 10px;
        /* width: 650px; */
        height: 150px;
    }

    .txt h2 {
        font-size: 15px;
        margin-bottom: 15px;
    }

    .txt p {
        line-height: 1.5;
        color: #a3a3a3;
    }

    .icon:before {
        content: ' ';
        display: block;
        width: 26px;
        height: 26px;
        background: url(https://img.bosszhipin.com/static/file/2022/venq8cn1iy1667886253777.png) 0 0/26px auto no-repeat;
    }

    .icon-report:before {
        background-position: 0 -26px;
    }

    .icon-all:before {
        background-position: 0 -312px;
    }

    .icon-article:before {
        background-position: 0 -52px;
    }

    .icon-dynamic:before {
        background-position: 0 -78px;
    }
}
</style>