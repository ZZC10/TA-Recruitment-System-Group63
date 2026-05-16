# TA Recruitment System V2 - 使用手册

## 一、系统概述

TA招聘系统（TA Recruitment System V2）是一个基于 Java Swing 的图形化界面应用程序，用于管理教学助理（TA）的招聘流程。系统支持三种角色：**TA（求职者）**、**MO（招聘者/课程负责人）** 和 **ADMIN（管理员）**。

---

## 二、系统启动

### 2.1 运行方式

1. 确保已安装 Java Development Kit (JDK) 8 或更高版本
2. 在项目目录 `V2` 下运行以下命令：

```bash
# 编译项目
javac -d bin -sourcepath src src/gui/MainFrame.java

# 运行程序
java -cp bin gui.MainFrame
```

### 2.2 欢迎界面

程序启动后，首先显示欢迎界面：
- 顶部显示系统标题 "TA Recruitment System V2"
- 右上角显示当前登录状态（初始为 "Not Logged In"）
- 底部有 "Logout" 按钮（未登录时禁用）和 "Exit" 按钮

---

## 三、账号注册与登录

### 3.1 注册新账号

1. 点击登录窗口中的 **"Register"** 按钮
2. 填写注册信息：
   - **Student ID**: 学号（唯一标识）
   - **Password**: 密码
   - **Confirm Password**: 确认密码
   - **Name**: 姓名
   - **Role**: 选择角色（TA/MO/ADMIN）
3. 点击 **"Register"** 按钮完成注册
4. 注册成功后会弹出提示，自动返回登录界面

### 3.2 登录系统

1. 在登录界面输入 **Student ID** 和 **Password**
2. 点击 **"Login"** 按钮
3. 系统验证账号密码：
   - 验证成功：根据角色进入对应仪表盘
   - 验证失败：弹出错误提示

### 3.3 查找已有账号密码

由于系统使用 CSV 文件存储数据，您可以直接查看数据文件：

1. 打开项目目录下的 `users.csv` 文件
2. 文件格式：`studentId,password,name,email,major,role`
3. 例如：
   ```
   2021001,123456,John Doe,john@example.com,Computer Science,TA
   2021002,654321,Prof. Smith,smith@example.com,Computer Science,MO
   admin,admin123,Admin,admin@example.com,Admin,ADMIN
   ```

> **注意**：为了安全考虑，建议定期更改密码。

---

## 四、角色功能详解

### 4.1 TA（教学助理求职者）

TA 角色主要用于申请 TA 岗位，查看申请状态。

#### 4.1.1 TA 仪表盘

登录后进入 TA 仪表盘，包含以下功能按钮：
- **My Profile**: 查看/编辑个人信息
- **Upload CV**: 上传个人简历
- **Browse Jobs**: 浏览并申请岗位
- **My Applications**: 查看申请记录

#### 4.1.2 个人信息管理

1. 点击 **"My Profile"** 进入个人信息页面
2. 可编辑的字段：
   - Email: 电子邮箱
   - Major: 专业
3. 点击 **"Save Changes"** 保存修改
4. 点击 **"Back"** 返回 TA 仪表盘

#### 4.1.3 上传 CV

1. 点击 **"Upload CV"** 进入 CV 上传页面
2. 填写 CV 信息：
   - Intention: 申请意向
   - Experience: 相关经验
   - Skills: 技能特长
3. 点击 **"Select File"** 选择本地 CV 文件（支持 PDF/DOC/DOCX）
4. 点击 **"Upload CV"** 完成上传（带有进度条显示）

#### 4.1.4 浏览与申请岗位

1. 点击 **"Browse Jobs"** 进入岗位浏览页面
2. 页面显示所有可用岗位列表：
   - Job ID: 岗位编号
   - Title: 岗位名称
   - Module: 所属课程
   - Hours: 工作时长
   - Requirements: 岗位要求
3. 选择一个岗位后点击 **"Apply"** 按钮申请
4. 申请成功后会弹出提示

> **注意**：每个岗位只能申请一次，重复申请会被拒绝。

#### 4.1.5 查看申请状态

1. 点击 **"My Applications"** 进入申请记录页面
2. 页面显示所有申请记录：
   - Job ID: 申请的岗位编号
   - Job Title: 岗位名称
   - Status: 申请状态（PENDING/APPROVED/REJECTED）
   - Apply Date: 申请日期
3. 点击 **"Refresh"** 刷新状态

---

### 4.2 MO（课程负责人/招聘者）

MO 角色负责发布 TA 岗位并审核申请者。

#### 4.2.1 MO 仪表盘

登录后进入 MO 仪表盘，包含以下功能按钮：
- **Publish Job**: 发布新岗位
- **Manage Applications**: 管理申请

#### 4.2.2 发布岗位

1. 点击 **"Publish Job"** 进入发布页面
2. 填写岗位信息：
   - Select Module: 选择所属课程（仅显示 MO 负责的课程）
   - Job Title: 岗位名称
   - Working Hours: 工作时长（小时）
   - Requirements: 岗位要求
   - Description: 岗位描述（可选）
3. 点击 **"Publish"** 发布岗位
4. 点击 **"Cancel"** 清空表单

#### 4.2.3 管理申请

1. 点击 **"Manage Applications"** 进入管理页面
2. **左侧面板**: MO 发布的所有岗位列表
3. **右侧面板**: 选中岗位的申请者列表
4. 操作步骤：
   1. 在左侧表格中选择一个岗位
   2. 右侧显示该岗位的所有申请者
   3. 选择一个申请者
   4. 点击 **"Approve"** 批准申请，或点击 **"Reject"** 拒绝申请

---

### 4.3 ADMIN（管理员）

ADMIN 角色负责管理课程模块信息。

#### 4.3.1 Admin 仪表盘

登录后进入 Admin 仪表盘，包含以下功能按钮：
- **Module Management**: 课程模块管理

#### 4.3.2 模块管理

1. 点击 **"Module Management"** 进入管理页面
2. 页面显示所有课程模块列表：
   - Module ID: 模块编号
   - Module Name: 模块名称
   - MO ID: 负责该模块的 MO 学号
   - Actions: 操作按钮
3. **新增模块**:
   1. 点击 **"Add Module"** 按钮
   2. 填写模块信息（Module ID, Module Name, MO ID）
   3. 点击 **"Save"** 保存
4. **编辑模块**:
   1. 点击模块右侧的 **"Edit"** 按钮
   2. 修改信息后点击 **"Save"**
5. **删除模块**:
   1. 点击模块右侧的 **"Delete"** 按钮
   2. 确认删除操作

---

## 五、通用功能

### 5.1 返回主菜单

所有功能页面都有 **"Back"** 按钮，点击可返回对应角色的仪表盘。

### 5.2 登出系统

1. 点击底部的 **"Logout"** 按钮
2. 系统清除当前登录状态，返回欢迎界面

### 5.3 退出程序

点击底部的 **"Exit"** 按钮，关闭程序。

### 5.4 消息提示

系统使用统一的消息对话框：
- **Success**: 操作成功提示（绿色）
- **Warning**: 警告提示（黄色）
- **Error**: 错误提示（红色）
- **Info**: 信息提示（蓝色）

---

## 六、数据文件说明

系统使用 CSV 文件存储数据，文件位于项目根目录：

| 文件名称 | 用途 | 字段说明 |
|---------|------|---------|
| `users.csv` | 用户信息 | studentId, password, name, email, major, role |
| `jobs.csv` | 岗位信息 | jobId, title, hours, moduleId, requirements, description |
| `modules.csv` | 课程模块 | moduleId, moduleName, moId |
| `applications.csv` | 申请记录 | studentId, jobId, status, applyDate |
| `cv.csv` | CV 信息 | studentId, intention, experience, skills |

---

## 七、常见问题

### Q1: 忘记密码怎么办？

A: 由于系统没有密码重置功能，您需要手动修改 `users.csv` 文件中的密码字段。

### Q2: MO 看不到任何课程模块？

A: MO 只能管理自己负责的课程模块。请联系管理员在 `modules.csv` 中添加您负责的课程。

### Q3: TA 看不到任何岗位？

A: 岗位由 MO 发布，请联系相关 MO 发布岗位。

### Q4: 申请状态一直显示 PENDING？

A: 请联系对应的 MO 审核您的申请。

---

## 八、快捷键

| 快捷键 | 功能 |
|-------|------|
| Alt + F4 | 关闭窗口 |
| Enter | 确认按钮（部分按钮） |

---

## 九、技术支持

如遇到系统问题，请联系系统管理员。

---

**版本**: V2.0  
**更新日期**: 2026年5月