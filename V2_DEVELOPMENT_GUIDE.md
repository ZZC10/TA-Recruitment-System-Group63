  # EBU6304 Group63 V2 Development Guide (GUI Version)

**Applicable Scenario**: V2 parallel development | **Effective Date**: 2026.04.19

**Purpose**: To provide a clear 6-member division of work for V2 (GUI version) development, assuming V1 is complete.

---

## 一、V2版本目标

V2重点不是新增复杂功能，而是：
- 将现有控制台系统改为图形化界面（GUI）
- 保持原有业务逻辑（V1代码）不被破坏
- 实现基本的用户交互流程（窗口/页面切换）
- 展示完整业务流程：TA申请 → MO审批 → Admin查看数据

---

## 二、总体分工策略

### 分工原则

- **UI层与业务逻辑分离**：每个成员负责自己V1模块对应的GUI界面
- **调用原有Service**：GUI层通过调用V1的Service类完成业务逻辑
- **集中整合**：成员6负责系统整合和集成测试
- **并行开发**：各成员可独立开发自己的GUI模块

### 协作方式

- 每周同步进度，统一界面风格
- 成员6制定公共GUI组件规范（如按钮样式、窗口尺寸）
- 整合前各自测试，整合后统一联调

---

## 三、6人详细分工

### 成员1：Zhuang Zuchen（auth/ UI + 登录注册界面）

**负责模块**：登录/注册UI

**具体任务**：
1. 设计登录窗口（LoginWindow）
   - 学生ID输入框
   - 密码输入框
   - 登录/注册按钮
   - 角色选择下拉框（TA/MO/ADMIN）

2. 设计注册窗口（RegisterWindow）
   - 学生ID、密码、姓名输入
   - 角色选择（TA/MO/ADMIN）
   - 表单验证提示

3. 调用AuthService完成验证
   - `authService.login()`
   - `authService.register()`
   - `authService.getUserRole()`

4. 登录成功后跳转对应角色主界面

**依赖关系**：
- 依赖成员6的MainFrame框架
- 依赖成员6的窗口跳转机制

---

### 成员2：Wu Bohan（user/ UI + TA个人信息界面）

**负责模块**：TA个人信息管理UI

**具体任务**：
1. 设计个人信息窗口（ProfileWindow）
   - 显示/编辑姓名、邮箱、专业
   - 显示当前角色

2. 设计CV上传窗口（CVUploadWindow）
   - 文件选择按钮
   - 上传进度显示
   - CV预览区域

3. 调用UserService和UploadCV
   - `userService.updateInfo()`
   - `uploadCV.upload()`
   - `userService.getUserByStudentId()`

**依赖关系**：
- 依赖成员1的登录流程获取当前用户ID
- 依赖成员6的MainFrame框架

---

### 成员3：Xing Wanying（job/ UI + TA申请界面）

**负责模块**：TA申请功能UI

**具体任务**：
1. 设计职位浏览窗口（JobBrowseWindow）
   - 职位列表展示（表格形式）
   - 职位分类筛选
   - 职位详情查看

2. 设计申请窗口（ApplicationWindow）
   - 申请表单
   - 申请状态查看
   - 历史申请记录

3. 调用JobService和ApplicationService
   - `jobService.showCategories()`
   - `applicationService.applyForJob()`
   - `applicationService.getApplicationStatus()`

**依赖关系**：
- 依赖成员5发布的职位数据
- 依赖成员6的MainFrame框架

---

### 成员4：Guo Aozhi（module/ UI + Admin模块管理界面）

**负责模块**：模块管理UI（Admin权限）

**具体任务**：
1. 设计模块管理窗口（ModuleManageWindow）
   - 模块列表展示
   - 新建模块表单
   - 模块信息编辑

2. 设计模块详情窗口
   - 显示模块负责人
   - 显示关联职位

3. 调用ModuleService
   - `moduleService.createModule()`
   - `moduleService.getModulesByLeader()`
   - `moduleService.getModuleById()`

**依赖关系**：
- 依赖成员6的Admin权限验证
- 依赖成员6的MainFrame框架

---

### 成员5：Yu Yue（position/ UI + MO职位发布界面）

**负责模块**：职位发布UI（MO权限）

**具体任务**：
1. 设计职位发布窗口（PublishJobWindow）
   - 职位名称、分类输入
   - 关联模块选择
   - 截止日期选择
   - 职位描述文本框

2. 设计MO工作台窗口（MOWorkbenchWindow）
   - 查看已发布职位
   - 查看申请人列表
   - 审批/拒绝申请

3. 调用PositionService和ApplicationService
   - `positionService.publishJob()`
   - `applicationService.getApplicantsByJob()`
   - `applicationService.approveApplication()`

**依赖关系**：
- 依赖成员4的模块数据
- 依赖成员6的MainFrame框架

---

### 成员6：Zhang Xinpeng（系统整合 + MainFrame框架）

**负责模块**：系统框架 + 集成

**具体任务**：
1. 设计MainFrame（主窗口框架）
   - 顶部导航栏
   - 主内容区域
   - 用户信息显示
   - 退出登录按钮

2. 设计界面跳转机制
   - 根据角色显示对应菜单
   - 窗口间数据传递
   - 统一窗口尺寸和风格

3. 提供公共GUI组件
   - 统一样式的按钮
   - 统一样式的输入框
   - 消息提示框（Dialog）
   - 确认对话框

4. 系统集成与测试
   - 整合所有GUI模块
   - 测试完整业务流程
   - 修复集成问题
   - 确保TA→MO→Admin流程贯通

5. 权限控制
   - 实现角色权限验证
   - 未登录状态界面限制

**依赖关系**：
- 被所有成员依赖
- 需要协调各成员的进度

---

## 四、系统集成说明

### UI调用Service模式

```
GUI组件（成员1-5）
    ↓ 调用
V1 Service类（原有业务逻辑）
    ↓ 使用
V1 FileUtil（数据持久化）
```

### 窗口跳转流程

```
LoginWindow → (登录成功) → MainFrame(TA/MO/Admin)
                              ↓
                    对应角色功能窗口
                              ↓
                         (退出) → LoginWindow
```

### 成员6整合职责

1. **制定规范**：窗口尺寸、按钮样式、字体大小
2. **提供基类**：可复用的GUI组件
3. **协调进度**：汇总各成员进度，解决冲突
4. **集成测试**：确保完整流程可运行

---

## 五、V2功能对照表

| 功能 | V1实现 | V2实现 |
|------|--------|--------|
| 登录 | 控制台输入 | LoginWindow |
| 注册 | 控制台输入 | RegisterWindow |
| 浏览职位 | 控制台显示 | JobBrowseWindow |
| 申请职位 | 控制台输入 | ApplicationWindow |
| 发布职位 | 控制台输入 | PublishJobWindow |
| 审批申请 | 控制台输入 | MOWorkbenchWindow |
| 管理模块 | 控制台输入 | ModuleManageWindow |
| 查看工作量 | 控制台显示 | AdminDashboardWindow |

---

## 六、开发顺序建议

1. **第一周**：成员6搭建MainFrame框架，提供公共组件
2. **第二周**：成员1-5各自开发对应GUI模块
3. **第三周**：成员6集成各模块，进行联调测试
4. **第四周**：修复Bug，完善细节，准备验收

---

## 七、技术选型

| 选项 | 优点 | 缺点 |
|------|------|------|
| **Java Swing** | 无需额外依赖，跨平台好 | 代码较繁琐 |
| **JavaFX** | 现代UI设计，CSS支持 | 需要额外配置 |

**建议**：选择Java Swing（更稳定，无需额外配置）

---

## 八、文件结构

```
V2/src/
├── gui/                    # GUI相关文件（新增）
│   ├── MainFrame.java      # 主窗口框架（成员6）
│   ├── common/             # 公共GUI组件（成员6）
│   │   ├── ButtonPanel.java
│   │   ├── InputPanel.java
│   │   └── MessageDialog.java
│   ├── auth/               # 登录注册UI（成员1）
│   │   ├── LoginWindow.java
│   │   └── RegisterWindow.java
│   ├── user/               # 用户信息UI（成员2）
│   │   ├── ProfileWindow.java
│   │   └── CVUploadWindow.java
│   ├── job/                # 职位浏览UI（成员3）
│   │   ├── JobBrowseWindow.java
│   │   └── ApplicationWindow.java
│   ├── module/             # 模块管理UI（成员4）
│   │   └── ModuleManageWindow.java
│   └── position/           # 职位发布UI（成员5）
│       ├── PublishJobWindow.java
│       └── MOWorkbenchWindow.java
├── auth/                   # V1代码（不变）
├── user/                   # V1代码（不变）
├── job/                    # V1代码（不变）
├── module/                 # V1代码（不变）
├── position/               # V1代码（不变）
├── common/                 # V1代码（不变）
├── application/            # V1代码（不变）
└── Main.java              # 入口（修改为启动GUI）
```

---

## 九、严格限制

- ❌ 不要写任何业务逻辑代码（使用V1的Service）
- ❌ 不要重新设计系统架构
- ❌ 不要引入数据库或复杂框架
- ❌ 不要修改V1的CSV数据文件格式
- ❌ 不要删除V1已有功能

---

## 十、验收标准

1. 成功启动GUI应用程序
2. 能够完成登录/注册流程
3. TA能够浏览职位并提交申请
4. MO能够发布职位并审批申请
5. Admin能够管理模块
6. 完整业务流程可正常运行
