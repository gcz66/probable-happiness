import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：携带 token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

// 响应拦截器：统一处理
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    // 未登录 / 权限不足
    if (res.code === 401 || res.code === 403) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      if (res.code === 401) {
        ElMessage.warning('请先登录')
      } else {
        ElMessage.warning('无访问权限')
      }
      router.push('/login')
      return Promise.reject(res)
    }
    ElMessage.error(res.message || '操作失败')
    return Promise.reject(res)
  },
  error => {
    ElMessage.error(error.response?.data?.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
