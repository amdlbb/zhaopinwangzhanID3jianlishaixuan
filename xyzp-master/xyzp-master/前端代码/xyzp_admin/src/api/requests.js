// 对axios进行二次封装
import axios from "axios";
// 引入进度条
import nprogress from "nprogress";
// 引入进度条相应样式，在nprogress.css中可以自定义修改颜色
import 'nprogress/nprogress.css'

const requests = axios.create({
    // 基础路径
    baseURL: '/api',
    // 代表请求超过的时间
    timeout: 5000,

});

// 请求拦截器：在发送请求之前，拦截器可以检测到
requests.interceptors.request.use((config) => {
    // // 若存在token则在请求头中加入token
    var token = localStorage.getItem("token")
    if (token != "" && token != undefined && token != null) {
        config.headers.token = token
    }
    nprogress.start();
    return config;
})

// 响应拦截器
requests.interceptors.response.use((res) => {
    // 成功的回调函数：服务器响应数据回来以后，拦截器可以检测到
    nprogress.done();
    return res.data;
}, (error) => {
    // 响应失败的回调函数
    return Promise.reject(new Error('faile '))
})

export default requests;