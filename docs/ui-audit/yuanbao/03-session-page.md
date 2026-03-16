# Yuanbao 页面审计 03: 会话页

审计类型：静态审计  
主要证据：

- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\session\SessionActivity.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\session\SessionFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout\fragment_chats_session.xml`

## 1. 页面定位

这是典型的“内容流 + 顶栏 + 底部输入 + 浮动状态”的复合页面。

虽然业务是聊天，但它的页面方法论对 `MoeMemosAndroid` 的详情页和内容阅读页非常有参考价值。

## 2. 第一印象

- 视觉关键词：清爽、轻层级、重内容、操作贴边
- 气质判断：高频操作页，但没有压迫感
- 信息密度：中
- 主要吸引点：内容区和输入区边界清楚，顶部控制克制

## 3. 页面骨架

`fragment_chats_session.xml` 非常干净：

- 顶部：`HYTopAppBar`
- 中部：`RecyclerView`
- 中下部：`ComposeView`
- 右下或底部悬浮：小图预览 `SmallImage`
- 底部：`SessionInputPanel`

这个骨架很像高完成度内容页的理想结构：内容优先，但输入和反馈永远在手边。

## 4. 视觉规则

从 XML 可以看出它并没有大量堆装饰，而是靠几个关键点出效果：

- 内容区背景是浅灰，不是纯白
- 列表有上方和底部留白
- 输入区和内容区明确分离
- 浮动元素贴近输入区而不是漂在页面中部

这是一种非常成熟的“低噪音内容页”处理方式。

## 5. 组件模式

页面里最重要的组件关系是：

- 顶栏保持轻
- 内容列表承担主信息
- `ComposeView` 承担动态状态或补充提示
- 小图预览承担附件暂存反馈
- 输入面板始终固定底部

它不像很多聊天页那样把状态和提示塞进列表，而是愿意用独立的 Compose 容器做更柔和的层叠。

## 6. 交互与动效推断

`SessionFragment` 里能看到这些行为：

- 列表滚动监听
- 点击列表收起输入面板
- 顶栏标题动态变化
- 会话页内存在额外 Compose UI 区块
- 输入区根据状态切换展示不同内容

这说明它很重视“内容浏览”和“输入准备”之间的节奏切换。

## 7. 最值得学的点

- 用极简骨架承载复杂业务
- 把动态反馈放进独立层，而不是塞满主列表
- 输入区与内容区之间保持稳定边界
- 所有高频动作都围绕底部区域组织

## 8. 不适合直接照搬的点

- 聊天气泡和消息菜单
- 评价、转发、复制等聊天特有动作
- 右下附件预览的具体业务形态

## 9. 映射到 MoeMemos

这一页对 `MemoDetailPage` 特别有价值：

- 顶栏应更轻，不要抢正文
- 正文区与操作区要分层，不要糊在一起
- 细小状态提示可以用单独容器承载
- 底部操作区要稳定，不要跟内容区互相挤压

优先级：P0

原因：这是 `MoeMemosAndroid` 最容易借到高级感的一页。
