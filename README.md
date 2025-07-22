# 码农圈 (Coder's Circle) - Spring Boot技术社区API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

这是一个基于Spring Boot 3构建的、功能完善的现代化技术社区后端项目。项目从零开始，整合了业界主流的技术栈，实现了一个包含完整用户认证授权、内容管理和社交互动功能的RESTful API服务。

---

## ✨ 项目亮点 (Highlights)

* **专业的安全体系**: 整合 **Spring Security + JWT**，实现了完整的无状态认证授权流程，并利用 **`@PreAuthorize`注解** 实现了精细到方法级别的**角色与资源所有权**权限控制。
* **复杂的业务流程设计**: 独立设计并实现了包含**邮件服务**和**Redis缓存**的**用户异步激活**和**密码重置**流程。
* **高效的数据访问与性能优化**: 熟练运用**MyBatis-Plus**，并通过**XML**编写**多表JOIN**及**动态SQL**来实现复杂查询。同时，通过**冗余字段**（`like_count`）和**分页查询**进行了性能优化。
* **现代化的API设计**: 遵循**RESTful**风格设计API，并通过集成**Swagger**为项目提供了自动化、交互式的API文档。

---

## 🚀 技术栈 (Tech Stack)

| 分类 | 技术 | 描述 |
| :--- | :--- | :--- |
| **核心框架** | Spring Boot 3.2.5 | 项目基础框架 |
| **安全框架** | Spring Security | 负责认证、授权与权限控制 |
| **身份认证** | JWT (JSON Web Token) | 实现无状态认证 |
| **持久层框架** | MyBatis-Plus | 强大的ORM框架，简化数据库操作 |
| **数据库** | MySQL 8.0+ | 关系型数据库 |
| **缓存数据库** | Redis | 用于存储临时令牌（激活、密码重置） |
| **邮件服务** | Spring Mail | 用于发送激活和密码重置邮件 |
| **API文档** | Swagger 3 (SpringDoc) | 自动化生成交互式API文档 |
| **文件存储** | 本地磁盘存储 | 实现用户头像等文件的上传与访问 |
| **校验框架** | Spring Validation | 实现声明式的API参数校验 |

---

## 📦 功能列表 (Features)

### 👤 用户与认证模块
- [x] **用户注册** (包含邮件异步激活)
- [x] **用户登录** (基于JWT)
- [x] **密码重置** (忘记密码流程)
- [x] **获取当前用户信息** (受保护接口)
- [x] **修改个人资料** (昵称、密码)
- [x] **上传/更新头像** (本地文件存储)

### 📝 文章模块
- [x] **发布新文章** (关联当前用户)
- [x] **修改/删除文章** (包含作者本人或管理员的权限校验)
- [x] **获取文章详情** (公开访问)
- [x] **公开文章列表** (分页、仅限已发布、支持按分类/关键字动态查询)
- [x] **我的文章列表** (分页、包含所有状态、支持按分类/状态/关键字动态查询)

### 🗂️ 分类模块
- [x] **分类的增/删/改/查** (CRUD)
- [x] **权限控制** (仅限管理员可进行写操作，所有人可读)

### 💬 评论模块
- [x] **发表评论** (需要登录)
- [x] **删除评论** (包含评论作者、文章作者、管理员的复杂权限校验)
- [x] **查看评论列表** (公开访问，包含评论者昵称和头像)

### ❤️ 点赞模块
- [x] **对文章进行点赞/取消点赞**
- [x] **文章点赞数实时更新** (通过冗余字段优化性能)

---

## 🛠️ 如何运行 (Getting Started)

### 1. 环境准备
* JDK 17
* Maven 3.6+
* MySQL 8.0+
* Redis

### 2. 克隆项目
```bash
git clone [https://github.com/ZKLOVEKFC/CodeCircle.git](https://github.com/ZKLOVEKFC/CodeCircle.git)
cd coders-circle
```

### 3. 配置数据库
1.  在你的MySQL中创建一个新的数据库，例如 `coderscircle`。
2.  依次执行项目根目录下 `database_schema.sql` 文件中的所有SQL语句，以创建所有需要的表。
    *(提示: 你需要自己整理一份包含`user`, `article`, `category`, `comment`, `article_like`建表语句的SQL文件)*

### 4. 修改配置文件
1.  打开 `src/main/resources/application.yml` 文件。
2.  根据你的本地环境，修改以下占位符信息：
    * `spring.datasource.password`: 你的数据库密码。
    * `spring.mail.username`: 你的发件人邮箱地址。
    * `spring.mail.password`: 你的邮箱授权码。
    * `jwt.secret-key`: 设置一个你自己的、复杂的JWT密钥。
    * `file-storage.path`: 设置一个你本地用于存储上传文件的文件夹路径。

### 5. 运行项目
在IDEA中，直接运行 `CodersCircleApplication.java` 的 `main` 方法即可。

---

## 📚 API文档

项目启动后，请在浏览器中访问以下地址，即可查看由Swagger自动生成的交互式API文档，并进行在线测试。

[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
*(请确保端口号与你在`application.yml`中配置的一致)*
