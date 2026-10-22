import request from '../utils/request'

// ==================== 认证 ====================
export const userLogin = (data) => request.post('/auth/login', data)
export const userRegister = (data) => request.post('/auth/register', data)
export const adminLogin = (data) => request.post('/auth/admin/login', data)

// ==================== 用户 ====================
export const getUserInfo = () => request.get('/user/info')
export const updateUser = (data) => request.put('/user/update', data)
export const changePassword = (data) => request.put('/user/password', data)

// ==================== 分类 ====================
export const getCategoryList = () => request.get('/category/list')
export const getAdminCategoryList = (params) => request.get('/category/admin/list', { params })
export const createCategory = (data) => request.post('/category/admin', data)
export const updateCategory = (id, data) => request.put(`/category/admin/${id}`, data)
export const deleteCategory = (id) => request.delete(`/category/admin/${id}`)

// ==================== 商品 ====================
export const getProductList = (params) => request.get('/product/list', { params })
export const getProductDetail = (id) => request.get(`/product/detail/${id}`)
export const getAdminProductList = (params) => request.get('/product/admin/list', { params })
export const createProduct = (data) => request.post('/product/admin', data)
export const updateProduct = (id, data) => request.put(`/product/admin/${id}`, data)
export const deleteProduct = (id) => request.delete(`/product/admin/${id}`)

// ==================== 轮播图 ====================
export const getBannerList = () => request.get('/banner/list')
export const getAdminBannerList = () => request.get('/banner/admin/list')
export const createBanner = (data) => request.post('/banner/admin', data)
export const updateBanner = (id, data) => request.put(`/banner/admin/${id}`, data)
export const deleteBanner = (id) => request.delete(`/banner/admin/${id}`)

// ==================== 购物车 ====================
export const getCartList = () => request.get('/cart/list')
export const addToCart = (data) => request.post('/cart/add', data)
export const updateCart = (id, data) => request.put(`/cart/${id}`, data)
export const deleteCart = (id) => request.delete(`/cart/${id}`)
export const clearCart = () => request.delete('/cart/clear')

// ==================== 地址 ====================
export const getAddressList = () => request.get('/address/list')
export const createAddress = (data) => request.post('/address', data)
export const updateAddress = (id, data) => request.put(`/address/${id}`, data)
export const deleteAddress = (id) => request.delete(`/address/${id}`)

// ==================== 订单 ====================
export const createOrder = (data) => request.post('/order/create', data)
export const payOrder = (orderId) => request.post(`/order/pay/${orderId}`)
export const getOrderList = (params) => request.get('/order/list', { params })
export const getOrderDetail = (orderId) => request.get(`/order/detail/${orderId}`)
export const cancelOrder = (orderId) => request.post(`/order/cancel/${orderId}`)
export const getAdminOrderList = (params) => request.get('/order/admin/list', { params })
export const updateOrderStatus = (orderId, data) => request.put(`/order/admin/status/${orderId}`, data)

// ==================== 评论 ====================
export const getProductComments = (productId) => request.get(`/comment/product/${productId}`)
export const createComment = (data) => request.post('/comment', data)
export const getAdminCommentList = (params) => request.get('/comment/admin/list', { params })
export const deleteComment = (id) => request.delete(`/comment/${id}`)

// ==================== 收藏 ====================
export const getFavoriteList = () => request.get('/favorite/list')
export const getFavoriteStatus = (productId) => request.get(`/favorite/status/${productId}`)
export const addFavorite = (productId) => request.post(`/favorite/add/${productId}`)
export const cancelFavorite = (productId) => request.delete(`/favorite/cancel/${productId}`)

// ==================== 充值 ====================
export const recharge = (amount) => request.post('/recharge', { amount })
export const getRechargeList = () => request.get('/recharge/list')
export const getAdminRechargeList = () => request.get('/recharge/admin/list')

// ==================== 统计 ====================
export const getOverview = () => request.get('/statistics/overview')
export const getOrderStatus = () => request.get('/statistics/orderStatus')
export const getRecent7Days = () => request.get('/statistics/recent7Days')
export const getProductByCategory = () => request.get('/statistics/productByCategory')
export const getHotProducts = (params) => request.get('/statistics/hotProducts', { params })
