import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import md5 from 'js-md5'
import {
  Button, Collapse, CollapseItem, Dropdown, DropdownItem, DropdownMenu, Message,
  Input, Pagination, Table, DatePicker, TableColumn, Tag, Dialog, Select, Option
} from "element-ui"

Vue.use(Collapse)
Vue.use(CollapseItem)
Vue.use(Dropdown)
Vue.use(DropdownItem)
Vue.use(DropdownMenu)
Vue.use(Tag)
Vue.use(Button)
Vue.use(Table)
Vue.use(Input)
Vue.use(TableColumn)
Vue.use(Pagination)
Vue.use(Dialog)
Vue.use(Button)
Vue.use(Select)
Vue.use(Option)
Vue.use(DatePicker)



Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this,
    Vue.prototype.$message = Message
    Vue.prototype.$md5 = md5
  },
  router,
  store
}).$mount('#app')
