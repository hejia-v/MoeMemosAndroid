# Global Polish Backlog

目标：把当前已经落地的设计系统从“主链路可用”推进到“整体一致且可维护”。

## 当前阶段判断

首页、输入页、详情页、搜索页、设置页、登录页、账号页、资源页，以及抽屉、卡片、菜单、Snackbar 已经基本接入统一设计语言。

剩余工作不再是大规模重构，而是收口不一致、补齐状态表达、做设计 QA。

## P0

- 统一 `AlertDialog` 样式，抽成 `MoeDialog`
- 覆盖现有确认、删除、兼容性提醒、同步提示等弹窗
- 统一标题、正文、按钮语义色、危险态、间距和圆角

优先检查文件：

- `app/src/main/java/me/mudkip/moememos/ui/page/memos/MemosList.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/memos/MemosHomePage.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/settings/SettingsPage.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/login/LoginPage.kt`
- `app/src/main/java/me/mudkip/moememos/ui/component/MemosCard.kt`
- `app/src/main/java/me/mudkip/moememos/ui/component/ArchevedMemoCard.kt`

## P0

- 统一空状态、加载状态、错误状态
- 抽成 `MoeEmptyState`、`MoeLoadingState`、`MoeErrorState`
- 列表页、资源页、Explore/Archived、搜索结果都改为统一表达

重点场景：

- 无 memo
- 无资源
- Explore/Archived 首次加载
- 同步失败
- 登录失败后的提示和引导

## P1

- 清理残留的默认 `MaterialTheme` 语义色引用
- 全部回收到 `MoeDesignTokens`
- 避免后续新页面继续混用默认主题色

优先检查：

- `app/src/main/java/me/mudkip/moememos/ui/component/MemosCard.kt`
- `app/src/main/java/me/mudkip/moememos/ui/page/common/Navigation.kt`
- 其他仍然使用 `MaterialTheme.colorScheme.*` 的页面和组件

## P1

- 做最终设计 QA
- 真机或模拟器逐页检查间距、滚动节奏、按钮触达区、暗色模式、横竖屏和键盘联动
- 记录需要微调的页面截图和问题清单

建议检查范围：

- 首页
- 输入页
- 详情页
- 搜索页
- 设置页
- 登录页
- 账号页
- 资源页
- 抽屉

## P1

- 统一文档和设计基线
- 给当前设计系统补截图基线和使用说明
- 防止后续开发重新回到默认 Material 风格

建议补充内容：

- token 使用规则
- 组件使用边界
- 页面级布局模式
- 常见状态示例

## 执行顺序

1. 先做 `MoeDialog`
2. 再做空状态、加载状态、错误状态统一
3. 然后清理残留 `MaterialTheme` 引用
4. 最后做真机 QA 和文档补齐

## 完成标准

- 主链路页面不再出现明显默认 Material 组件风格
- 弹窗、Snackbar、空状态、加载状态都属于同一套视觉系统
- 颜色、圆角、间距和状态语义都来自统一 token
- 设计 QA 有书面记录，不靠聊天记录回忆
