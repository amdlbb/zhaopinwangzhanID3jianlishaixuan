import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import md5 from 'js-md5'
import VueHoverMask from 'vue-hover-mask'
import '@/assets/css/global.css'
import {
  Input, Button, Carousel, CarouselItem, Backtop, Image, Dialog, Pagination, Tabs, TabPane, Message,
  Upload, Popover, Table, TableColumn, DatePicker
} from 'element-ui'

Vue.use(Input)
Vue.use(Button)
Vue.use(Carousel)
Vue.use(CarouselItem)
Vue.use(Backtop)
Vue.use(Image)
Vue.use(Dialog)
Vue.use(Pagination)
Vue.use(Tabs)
Vue.use(TabPane)
Vue.use(Upload)
Vue.use(Popover)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(DatePicker)

Vue.component(VueHoverMask.name, VueHoverMask)

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this,
    Vue.prototype.$message = Message
    Vue.prototype.$md5 = md5
  },
  router,
  store,

}).$mount('#app')
