import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
	return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
	mode: "history",
	routes: [{
		path: '/index',
		component: () => import('@/pages/Home'),
	},
	{
		path: '/userhome/:id',
		component: () => import('@/pages/UserHome'),
	},
	{
		path: '/teamhome/:id',
		component: () => import('@/pages/TeamHome'),
	},
	{
		path: '/workhome',
		component: () => import('@/pages/WorkHome'),
	},
	{
		path: '/workhome/:id',
		component: () => import('@/pages/WorkHome/WorkItem'),
	},
	{
		path: '/information',
		component: () => import('@/pages/Information'),
	},
	{
		path: '/resume',
		component: () => import('@/pages/Resume'),
	},
	{
		path: '/test',
		component: () => import('@/pages/ChatHome'),
	},
	// ====== 新增：企业官网静态页面 ======
	{
		path: '/about',
		component: () => import('@/pages/AboutUs'),
	},
	{
		path: '/product',
		component: () => import('@/pages/ProductService'),
	},
	{
		path: '/news',
		component: () => import('@/pages/News'),
	},
	{
		path: '/contact',
		component: () => import('@/pages/Contact'),
	},


	// 重定向，每次访问*都定向到首页
	{
		path: '',
		redirect: '/index',
	}
	],
	// 控制路由跳转滚动条的位置为最顶部
	scrollBehavior(to, from, savedPosition) {
		// 始终滚动到顶部，y代表距离顶部的距离
		return {
			y: 0
		}
	},
})

router.beforeEach((to, from, next) => {
	// 没登陆的就拦截
	if (to.path.match("/userhome") || to.path.match("/teamhome")) {
		var token = localStorage.getItem("token")
		if(token == null){
			next("/index")
		}
	}
	next()
})


export default router