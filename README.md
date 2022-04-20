# code-generator
使用 SpringBoot+Vue 开发

根据建表 SQL 语句生成 实体类、mapper、service、serviceImpl、controller 代码

## 开发原因
工作中重复的 CRUD 内容太多，表字段又很多，故开发此代码生成器

基于项目：https://github.com/moshowgame/SpringBootCodeGenerator 开发，感谢作者 @moshowgame

主要添加了可自定义模板功能，可自定义代码生成的模板，完全在 web 端即可操作

演示地址：http://49.233.159.81:21345/

## 特性

### 1. 解析建表 SQL，根据模板生成代码

![image-20220416130817223](./images/generate.png)

### 2. 可自定义生成代码的模板

![image-20220416132426459](./images/template.png)

### 3. 使用自定义的模板生成代码

![image-20220416132521904](./images/custom.png)

## TODO

- [x] 可自定义代码生成模板
- [ ] 模板编写指南
