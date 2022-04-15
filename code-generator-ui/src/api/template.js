import request from '@/utils/request'

// 获取所有模板组及模板组下的模板名称列表
export function getAllTemplate() {
  return request({
    url: '/template/all',
    method: 'get'
  })
}

// 获取所有模板 包括内容
export function getAllTemplateContent() {
  return request({
    url: '/template/all/content',
    method: 'get'
  })
}

// 创建模板组
export function addTemplateGroup(groupName) {
  return request({
    url: `/template/group/add/${groupName}`,
    method: 'get'
  })
}

// 创建模板
export function addTemplate(groupName, templateName) {
  return request({
    url: `/template/add/${groupName}/${templateName}`,
    method: 'get'
  })
}

// 写入模板内容
export function updateTemplateContent(groupName, data) {
  return request({
    url: `/template/content/update/${groupName}`,
    method: 'post',
    data
  })
}

// 删除模板组
export function deleteTemplateGroup(groupName) {
  return request({
    url: `/template/group/delete/${groupName}`,
    method: 'delete'
  })
}

// 删除模板
export function deleteTemplate(groupName, templateName) {
  return request({
    url: `/template/delete/${groupName}/${templateName}`,
    method: 'delete'
  })
}
