# Yuanbao 页面审计 01: 登录页

审计类型：静态审计  
主要证据：

- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\login\v2\HYLoginMainActivity.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\login\v2\o.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\login\v2\YBLoginComposeFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\values\public.xml`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout\phone_num_login.xml`

## 1. 页面定位

这是一个明显的“品牌首屏 + 轻决策登录入口”页面，不是传统表单页。

从 `HYLoginMainActivity -> YBBaseLoginActivity -> YBLoginComposeFragment` 这一链路看，登录首页本身是 Compose 主导。`o.java` 直接返回 `YBLoginComposeFragment`，并且在 Activity 层处理系统栏 inset，说明它非常重视首屏稳定性和沉浸感。

## 2. 第一印象

- 视觉关键词：品牌化、简洁、低噪音、首屏聚焦
- 气质判断：产品感强于工具感
- 信息密度：低
- 主要吸引点：品牌图形、单焦点入口、弱化系统 UI 干扰

## 3. 页面骨架

静态推断的骨架大致是：

- 顶部或中上区域：品牌 Logo 与主视觉
- 中部：主登录入口
- 底部：其他登录方式、协议、次级跳转
- 浮层：账号选择弹窗、异常提示弹窗

证据：

- `YBLoginComposeFragment` 中存在账号选择弹窗逻辑
- 资源中存在 `login_yb_logo`、`login_icon_new`、`login_change_bg`、`qq_logo`、`wechat_logo`、`phone_logo`
- Activity 层显式处理系统栏底部 inset，说明页面布局会贴近屏幕边缘

## 4. 视觉规则

从资源和 token 命名看，登录页并不是独立乱做的，而是挂在统一视觉系统上。

- 文本层级：`yb_text_primary`、`yb_text_secondary`、`yb_text_tertiary`
- 背景层级：`yb_background_canvas`、`yb_background_primary`、`yb_background_secondary`
- 轮廓层级：`yb_outline_primary`、`yb_outline_secondary`

这意味着它不是简单黑白灰配色，而是有成熟语义色体系。

## 5. 组件模式

登录页最值得记录的不是按钮样式，而是“决策密度控制”：

- 主要入口数量少
- 次级入口被延后展示
- 多账号或团队/个人切换通过弹窗承载
- 系统栏与底部安全区被主动处理，避免按钮或协议区域被挤压

这种做法的好处是：第一眼品牌统一，第二眼操作清楚，第三眼才暴露复杂分支。

## 6. 交互与动效推断

- 首屏大概率是轻量入场，而不是大动画
- 账号选择页更像是底部弹窗或轻浮层，而不是整页跳转
- 登录失败或风控状态通过弹窗拦截，而不是挤进主页面
- 页面本身应当对键盘、系统栏、第三方登录跳转返回都做了较细处理

## 7. 最值得学的点

- 用低信息密度提升高级感，而不是靠堆装饰
- 登录流程拆为“首屏决策”和“详细输入”两层
- 品牌元素、操作元素、协议元素的优先级非常清楚
- 首屏沉浸式和安全区处理是产品级，不是 Demo 级

## 8. 不适合直接照搬的点

- 具体品牌图形与图标资源
- 登录供应商入口排列方式
- 团队/个人账号业务逻辑

## 9. 映射到 MoeMemos

虽然 `MoeMemosAndroid` 不一定需要这么重的登录首屏，但它可以直接借鉴这些原则：

- 首屏只保留一个主目标
- 通过更低噪音的布局提升产品感
- 把复杂入口收进弹层，而不是直接堆页面
- 对系统栏和底部安全区做主动设计

优先级：P1

原因：它不直接影响 `MoeMemos` 核心功能，但会影响“第一眼产品气质”。
