import request from '@/utils/request'

// 查询${classInfo.classComment}列表
export function list${classInfo.className}(query) {
    return request({
        url: '${requestMapping}/list',
        method: 'get',
        params: query
    })
}

// 查询${classInfo.classComment}详细
export function get${classInfo.className}(${classInfo.className?uncap_first}Id) {
    return request({
        url: '${requestMapping}/' + ${classInfo.className?uncap_first}Id,
        method: 'get'
    })
}

// 新增${classInfo.classComment}
export function add${classInfo.className}(data) {
    return request({
        url: '${requestMapping}',
        method: 'post',
        data: data
    })
}

// 修改${classInfo.classComment}
export function update${classInfo.className}(data) {
    return request({
        url: '${requestMapping}',
        method: 'put',
        data: data
    })
}

// 删除${classInfo.classComment}
export function del${classInfo.className}(${classInfo.className?uncap_first}Id) {
    return request({
        url: '${requestMapping}/' + ${classInfo.className?uncap_first}Id,
        method: 'delete'
    })
}
