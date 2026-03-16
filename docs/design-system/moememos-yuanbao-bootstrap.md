# MoeMemos x Yuanbao 设计系统起步清单

目标：不是逐页照搬 `yuanbao`，而是把它的高级感和交互完成度，转译为适合 `MoeMemosAndroid` 的 Compose 设计系统。

适用范围：

- `MemosHomePage`
- `MemoInputPage`
- `MemoDetailPage`
- 之后再扩散到 `SearchPage`、`ResourceListPage`、`SettingsPage`

---

## 1. 当前现状判断

从现有工程看，`MoeMemosAndroid` 已具备改造基础，但还停留在“标准 Compose + Material 3 应用”的层级：

- 主题层较薄，主要还是默认 Material 语义。
- 颜色、字体、圆角、间距没有独立 token 层。
- 页面骨架较标准，缺少强辨识度布局。
- 转场和微交互偏基础。
- 组件实现偏功能型，缺少品牌气质。

先关注这些现有文件：

- `app/src/main/java/me/mudkip/moememos/ui/theme/Theme.kt`
- `app/src/main/java/me/mudkip/moememos/ui/theme/Color.kt`
- `app/src/main/java/me/mudkip/moememos/ui/theme/Type.kt`
- `app/src/main/java/me/mudkip/moememos/ui/component/MemosCard.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/memos/MemosHomePage.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/memoinput/MemoInputPage.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/common/Navigation.kt`

---

## 2. 第一批 Design Token

建议先不要继续把所有值散落在 `Theme.kt` 和各个组件里，而是新增独立 token 层。

建议目录：

- `ui/designsystem/token/MoeColors.kt`
- `ui/designsystem/token/MoeSpacing.kt`
- `ui/designsystem/token/MoeRadius.kt`
- `ui/designsystem/token/MoeElevation.kt`
- `ui/designsystem/token/MoeTypography.kt`
- `ui/designsystem/token/MoeMotion.kt`

### 2.1 Color Token

先抽这 12 类，不要一开始就过细。

- `bgApp`
- `bgSurface`
- `bgElevated`
- `bgOverlay`
- `textPrimary`
- `textSecondary`
- `textTertiary`
- `strokeSubtle`
- `strokeStrong`
- `accentPrimary`
- `accentSoft`
- `accentDanger`

规则：

- 背景至少分 3 层，不要只有一个 `surface`
- 文本至少分 3 级对比
- 强调色要有实色和柔和色两个版本
- 分割线和描边不要用同一色值

### 2.2 Spacing Token

建议只保留 8 个基础值：

- `2`
- `4`
- `8`
- `12`
- `16`
- `20`
- `24`
- `32`

规则：

- 页面边距以 `16/20/24` 为主
- 卡片内边距优先 `12/16`
- 大块区隔优先 `24/32`

### 2.3 Radius Token

建议至少定义：

- `xs = 8.dp`
- `sm = 12.dp`
- `md = 16.dp`
- `lg = 20.dp`
- `xl = 28.dp`
- `full`

规则：

- 输入框、chip、按钮不要混用随机圆角
- 主卡片建议统一落在 `16~20dp`
- 大型弹层和底部面板可以更圆，但要统一

### 2.4 Elevation Token

先定义语义，不先定义 Material elevation 名字。

- `flat`
- `raised`
- `floating`
- `overlay`

每层需要记录：

- 阴影强度
- 模糊半径
- y 偏移
- 是否额外描边

### 2.5 Typography Token

不要继续只靠默认 `Typography`。建议至少定义：

- `display`
- `headline`
- `title`
- `body`
- `bodyStrong`
- `caption`
- `label`
- `monoNumber`

规则：

- 标题和正文拉开字重差
- 次级信息不要只是简单调浅颜色
- 数字统计和标签建议单独定义样式

### 2.6 Motion Token

这是最容易缺失，但最影响“像不像”的部分。

建议先定义：

- `fast = 120ms`
- `normal = 220ms`
- `slow = 320ms`
- `emphasized = 420ms`

以及 4 类 easing：

- `standard`
- `decelerate`
- `accelerate`
- `springSoft`

建议覆盖场景：

- 页面进入/退出
- 卡片按压
- FAB 显隐
- 顶栏透明度变化
- 输入框聚焦
- 底栏工具切换

---

## 3. 第一批基础组件

不要先大改页面，先把这些基础件做出来。

### P0 组件

- `MoeAppBar`
- `MoeCard`
- `MoeInputField`
- `MoePrimaryButton`
- `MoeSecondaryButton`
- `MoeFloatingActionButton`
- `MoeBottomActionBar`
- `MoeTagChip`

### P1 组件

- `MoeSearchBar`
- `MoeSectionHeader`
- `MoeEmptyState`
- `MoeLoadingState`
- `MoeSheet`
- `MoeDialog`
- `MoeSnackBar`

### P2 组件

- `MoeSegmentedTabs`
- `MoeInlineToolbar`
- `MoeMediaGrid`
- `MoeFilterChipGroup`

组件统一要求：

- 必须有状态设计：默认、按压、禁用、加载、选中
- 必须接 token，不允许组件内随手写死数值
- 必须写预览，至少覆盖亮色、暗色、长文案、边界态

---

## 4. 首批页面改造清单

### 4.1 `MemosHomePage`

目标：先把首页气质拉起来。

改造点：

- 重做首页顶部结构，不要停留在标准 `TopAppBar`
- 首页卡片做更明确层级，减少“默认 Card 组件感”
- 统一页面边距和垂直节奏
- 重新定义 FAB 的尺寸、圆角、显隐动效和落点
- 滚动时加入顶部栏透明/位移联动

预期收益：

- 第一眼更像产品，而不是 Demo
- 列表浏览体验更顺

### 4.2 `MemoInputPage`

目标：把高频输入体验拉到更接近 `yuanbao` 的完成度。

改造点：

- 重做输入区容器，不要只是普通编辑页
- 顶栏、底栏、输入框三者做联动
- 输入聚焦、上传附件、插入标签时增加状态反馈
- 处理键盘弹出后的安全区和工具栏稳定性
- 补“草稿中/可提交/提交中/已保存”这些反馈

预期收益：

- 手感明显提升
- 用户更容易感受到“完成度”

### 4.3 `MemoDetailPage`

目标：让单条 memo 的阅读页具备更强内容氛围。

改造点：

- 重新设计标题区和元信息区
- 图片、附件、标签的呈现方式统一
- 操作区和正文区层级更清楚
- 滚动时做轻量级内容聚焦效果

---

## 5. 不建议直接照搬的部分

- 聊天产品特有的布局骨架
- `yuanbao` 的品牌化图标和插画
- 不符合 `MoeMemos` 信息架构的浮层和导航方式
- 为了“像”而加过多装饰性动画

判断标准：

- 如果它强化的是“记 memo”主流程，可以借
- 如果它强化的是“AI 对话”场景，不要硬搬

---

## 6. 第一阶段任务拆分

建议按下面顺序开工。

### 阶段 A：文档沉淀

- 挑 6 到 8 个 `yuanbao` 页面做审计
- 每页补截图、录屏、结构图、状态图
- 建立共享术语，避免“这个感觉更高级”这种模糊描述

### 阶段 B：设计系统落地

- 抽 token
- 抽基础组件
- 建预览
- 建页面模板

### 阶段 C：试点页面

- 先改 `MemosHomePage`
- 再改 `MemoInputPage`
- 最后改 `MemoDetailPage`

### 阶段 D：扩散

- Search
- Resource
- Settings

---

## 7. 验收标准

不是看“像不像截图”，而是看这 6 件事：

- 首页是否已经有明确的视觉层次
- 输入页是否有顺滑、稳定、低打扰的手感
- 组件之间是否共享统一 token
- 页面切换和按压反馈是否一致
- 新组件是否可复用，而不是一次性代码
- 设计语言是否已经从单页上升为系统

---

## 8. 下一步建议

如果继续推进，建议下一轮直接产出下面 3 个东西：

1. `yuanbao` 首批 6 页审计结果
2. `ui/designsystem/token` 的第一版 Kotlin 文件
3. `MoeCard`、`MoeAppBar`、`MoeInputField` 的首版 Compose 实现

做到这一步，项目就从“想复刻”进入“开始具备复刻能力”了。
