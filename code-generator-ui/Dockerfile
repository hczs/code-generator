# 教程：https://cli.vuejs.org/zh/guide/deployment.html#docker-nginx
# 基础镜像 node 环境 16.5.0
FROM node:16.5.0 as build-prod
# 把代码复制到镜像里
COPY ./ /app
# 切换到 /app 即复制过去的代码目录
WORKDIR /app
# 执行命令 安装依赖并打包
RUN npm install && npm run build:prod

# 第二阶段构建 基于 nginx 镜像构建
FROM nginx
# 把 build 好的 dist 放入 nginx 镜像这边的 /usr/share/nginx/html 目录
COPY --from=build-prod /app/dist /usr/share/nginx/html
# 其中 nginx.conf 中就配置了 80 端口的 根目录是 /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
