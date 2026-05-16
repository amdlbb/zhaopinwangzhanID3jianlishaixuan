import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/main',
            component: () => import('@/pages/Main'),
            children: [
                {
                    path: 'index',
                    component: () => import('@/pages/Main/Home'),
                },
                {
                    path: 'userManager',
                    component: () => import('@/pages/Main/UserManager'),
                },
                {
                    path: 'teamManager',
                    component: () => import('@/pages/Main/TeamManager'),
                },
                {
                    path: 'jobManager',
                    component: () => import('@/pages/Main/JobManager'),
                },
                {
                    path: 'teamJobManager',
                    component: () => import('@/pages/Main/TeamJobManager'),
                },
                {
                    path: 'adminManager',
                    component: () => import('@/pages/Main/AdminManager'),
                },
                {
                    path: 'resume',
                    component: () => import('@/pages/Main/Resume'),
                },
                {
                    path: 'interview',
                    component: () => import('@/pages/Main/Interview'),
                },
                {
                    path: 'admission',
                    component: () => import('@/pages/Main/Admission'),
                },
            ]

        },
        {
            path: '/login',
            component: () => import('@/pages/Login'),
            meta: { showNav: false }

        },


        // 重定向，每次访问*都定向到首页
        {
            path: '',
            redirect: '/main/index',
        }
    ],
    // 控制路由跳转滚动条的位置为最顶部
    scrollBehavior(to, from, savedPosition) {
        // 始终滚动到顶部，y代表距离顶部的距离
        return { y: 0 }
    },
})

router.beforeEach((to, from, next) => {
    // 保存历史记录
    // 排除首页
    if (from.path != "/") {
        var history = JSON.parse(sessionStorage.getItem("HISTORY"))
        if (history == null || history == []) history = []
        // 判断是否重复出现
        let flag = 0, i = 0
        for (i = 0; i < history.length; i++) {
            if (history[i].url == to.path) {
                flag = 1
                break
            }
        }
        // 让历史记录加入数组
        if (flag == 0) {
            if (to.path == "/main/userManager") {
                history.push({
                    name: "用户管理",
                    url: to.path
                })
            }
            else if (to.path == "/main/teamManager") {
                history.push({
                    name: "团队管理",
                    url: to.path
                })
            }
            else if (to.path == "/main/teamJobManager") {
                history.push({
                    name: "招聘审核",
                    url: to.path
                })
            }
            else if (to.path == "/main/jobManager") {
                history.push({
                    name: "职位管理",
                    url: to.path
                })
            }
            else if (to.path == "/main/adminManager") {
                history.push({
                    name: "管理员",
                    url: to.path
                })
            }
            else if (to.path == "/main/resume") {
                history.push({
                    name: "求职简历",
                    url: to.path
                })
            }
            else if (to.path == "/main/interview") {
                history.push({
                    name: "面试管理",
                    url: to.path
                })
            }
            else if (to.path == "/main/admission") {
                history.push({
                    name: "录取入职",
                    url: to.path
                })
            }
            sessionStorage.setItem("HISTORY", JSON.stringify(history))
        }
    }
    next()
})


export default router