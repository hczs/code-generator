import request from '@/utils/request'

export function generate(data) {
  return request({
    url: '/generate/code',
    method: 'post',
    data
  })
}
