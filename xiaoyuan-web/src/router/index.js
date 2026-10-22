import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 用户端
  { path: '/', redirect: '/home' },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/user/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: () => import('../views/user/ProductDetail.vue'),
    meta: { title: '商品详情' }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/user/Cart.vue'),
    meta: { title: '购物车', requiresAuth: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('../views/user/Order.vue'),
    meta: { title: '我的订单', requiresAuth: true }
  },
  {
    path: '/favorite',
    name: 'Favorite',
    component: () => import('../views/user/Favorite.vue'),
    meta: { title: '我的收藏', requiresAuth: true }
  },
  {
    path: '/address',
    name: 'Address',
    component: () => import('../views/user/Address.vue'),
    meta: { title: '收货地址', requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/user/Profile.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  // 登录/注册
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/auth/Register.vue'),
    meta: { title: '注册' }
  },
  // 管理端
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/auth/AdminLogin.vue'),
    meta: { title: '管理端登录' }
  },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { title: '数据看板', admin: true } },
      { path: 'product', name: 'AdminProduct', component: () => import('../views/admin/Product.vue'), meta: { title: '商品管理', admin: true } },
      { path: 'category', name: 'AdminCategory', component: () => import('../views/admin/Category.vue'), meta: { title: '分类管理', admin: true } },
      { path: 'banner', name: 'AdminBanner', component: () => import('../views/admin/Banner.vue'), meta: { title: '轮播图管理', admin: true } },
      { path: 'order', name: 'AdminOrder', component: () => import('../views/admin/Order.vue'), meta: { title: '订单管理', admin: true } },
      { path: 'comment', name: 'AdminComment', component: () => import('../views/admin/Comment.vue'), meta: { title: '评论管理', admin: true } },
      { path: 'recharge', name: 'AdminRecharge', component: () => import('../views/admin/Recharge.vue'), meta: { title: '充值记录', admin: true } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 校园外卖平台` : '校园外卖平台'
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }
  if (to.meta.requiresAdmin) {
    if (!token || role !== 'ADMIN') {
      next('/admin/login')
      return
    }
  }
  next()
})

export default router
