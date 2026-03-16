# Yuanbao 页面审计 02: 首页壳层与聊天主 Tab

审计类型：静态审计  
主要证据：

- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\home\v2\YBHomeActivityV2.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\home\v2\YBHomeFragmentV2.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout\activity_yb_home_v2.xml`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout\fragment_input_normal_conversation.xml`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\drawable\bg_home_top.webp`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\drawable\blury_background.webp`

## 1. 页面定位

这里不是单一页面，而是一层“应用壳”。`activity_yb_home_v2.xml` 清楚说明它把下面几件东西统一管理了：

- 主内容容器
- 底部导航 ComposeView
- 右下浮动层 ComposeView
- 搜索全屏覆盖层 ComposeView
- 抽屉层 ComposeView
- Web 运营层

这就是为什么 `yuanbao` 看起来像一个整体产品，而不是很多独立页面拼起来。

## 2. 第一印象

- 视觉关键词：沉浸、轻悬浮、层次多、状态切换自然
- 气质判断：像“容器化产品壳”，不是简单 tab app
- 信息密度：中
- 主要吸引点：页面层级稳定，浮层切换统一

## 3. 页面骨架

从 `activity_yb_home_v2.xml` 可以看出首页壳层结构很清晰：

- `ViewPager2` / `FragmentContainerView` 承载主内容
- `navigation_bar` 用 Compose 渲染底部导航
- `chat_compose_view` 承载浮动入口
- `cv_chat_input_plus` 是全屏交互层
- `cv_im_search_overlay` 是全屏搜索覆盖层
- `cv_drawer` 是抽屉层

这说明它的策略不是每个页面自己做浮层，而是把浮层、搜索、抽屉都提升到首页壳层统一管理。

## 4. 视觉规则

首页壳层最明显的设计语言有 4 点：

- 大面积使用语义背景层，而不是单一纯色
- 强依赖模糊、遮罩、渐变和覆盖层
- 底部导航不是传统 XML 导航栏，而是 Compose 皮肤化组件
- 背景和顶层浮层之间存在明显“空气感”

从资源名也能看出这套语言：

- `bg_home_top`
- `blury_background`
- `bg_gradient_scroll_mask`
- `plus_container_gradient_bg`

## 5. 组件模式

首页壳层最关键的组件不是按钮，而是容器级组件：

- 壳层导航
- 全屏搜索覆盖层
- 抽屉
- 底部导航
- 浮动操作入口

这类组件决定的是“整个 app 的节奏感”，不是某个页面的小样式。

## 6. 交互与动效推断

从 `YBHomeActivityV2` 和 `YBHomeFragmentV2` 中大量 `ComposeView` 覆盖层和状态切换逻辑判断：

- 搜索应该是覆盖式进入，而不是普通页面跳转
- 抽屉与主内容存在位置或遮罩关系
- 底部导航和右下浮动入口会根据页面状态变化
- 首页顶部和输入区存在多模态切换
- 页面切换时更像“壳层内变换”，而不是 Activity 跳来跳去

## 7. 最值得学的点

- 先把壳层设计好，再谈单页美观
- 把搜索、抽屉、浮动操作从页面里抽到应用层
- 用覆盖层而不是新页面来保持节奏连续
- 用语义化容器层级制造高级感

## 8. 不适合直接照搬的点

- `yuanbao` 的多业务入口密度
- 聊天产品专属的导航分布
- 与搜索、项目、IM、运营位绑定的具体布局

## 9. 映射到 MoeMemos

这一页对 `MoeMemosAndroid` 的价值很高，虽然不能照搬结构，但很适合借鉴“壳层思维”：

- 把搜索、筛选、浮动操作统一放到首页壳层设计
- 不要让每个页面各自处理浮层、弹窗和辅助操作
- 用统一的顶部区域和底部行为提升产品感

优先级：P0

原因：这直接决定 `MoeMemosAndroid` 后续所有页面看起来是“一个产品”，还是“几个 Compose 页面”。
