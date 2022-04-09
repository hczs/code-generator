import request from '@/utils/request'

export function generate(data) {
  return request({
    url: '/generate/code',
    method: 'post',
    data
  })
}

export function getAllTemplate() {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get'
  })
}
