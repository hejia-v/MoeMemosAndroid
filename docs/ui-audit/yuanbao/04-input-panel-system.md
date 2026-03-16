# Yuanbao 页面审计 04: 输入面板系统

审计类型：静态审计  
主要证据：

- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel\dialog\BaseInputPanelFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel\dialog\search\AiSearchPanelFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel\dialog\doc\AiReadingPanelFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel\dialog\translate\TranslatePanelFragment.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel\dialog\write\m.java`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout\fragment_input_normal_conversation.xml`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\drawable\single_v2_gradient_light_bg_input.xml`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\drawable\input_bg_conversation_input.xml`

## 1. 页面定位

这不是一个“输入框”，而是一整套输入系统。

从 `BaseInputPanelFragment` 的抽象层和一系列 `search/doc/translate/write/draw` 面板可以看出，`yuanbao` 把输入当成产品核心场景来设计，而不是表单控件。

## 2. 第一印象

- 视觉关键词：轻面板、层级深、状态丰富、可扩展
- 气质判断：输入即场景，不是输入即表单
- 信息密度：中偏高，但分层好
- 主要吸引点：切换丰富却不乱

## 3. 页面骨架

`fragment_input_normal_conversation.xml` 里和输入相关的层非常多：

- 顶部模糊标题区
- 推荐卡层
- 通知条
- 小图/小文件暂存层
- 输入上方渐变遮罩
- 空状态背景
- 主输入面板 `ConversationInputPanelWrapper`
- 参数设置浮层
- 输入引导 RecyclerView
- 语音引导 Lottie
- 编辑遮罩层

这说明 `yuanbao` 的输入体验不是“底部一块白条”，而是“围绕输入展开的一整套空间设计”。

## 4. 视觉规则

最明显的规律有：

- 输入面板与主背景之间常用渐变过渡
- 面板和浮层使用独立语义背景色，而不是同一白色
- 轮廓和描边层级明确
- 常见圆角半径集中在 `8 / 12 / 16 / 20 / 24`
- 弹出层、参数面板、标签、辅助卡片都有自己的层级背景

从资源命名可以直接看到这些系统化痕迹：

- `yb_background_canvas`
- `yb_background_primary`
- `yb_background_secondary`
- `yb_background_tertiary_popover`
- `yb_text_primary`
- `yb_text_secondary`
- `yb_text_tertiary`
- `yb_outline_secondary`

## 5. 组件模式

输入系统里最关键的组件不是发送按钮，而是这些能力：

- 输入容器
- 模态/技能切换
- 附件暂存反馈
- 参数设置浮层
- 输入引导条
- 轻提示条
- 空状态背景
- 键盘与面板联动

`BaseInputPanelFragment` 的抽象层已经说明了它希望所有技能面板共享一套基础能力和节奏。

## 6. 交互与动效推断

从代码和布局可以较明确推断：

- 文本输入会做防抖处理
- 面板切换不是简单跳转，而是同一容器内替换
- 参数设置是锚定输入区的浮层，而不是独立页面
- 推荐卡、通知条、引导条会根据上下文出现和消失
- 输入区域和内容区域之间通过渐变和遮罩过渡

这类处理直接决定“手感高级不高级”。

## 7. 最值得学的点

- 把输入当作系统，而不是组件
- 把输入相关反馈都组织在底部区域附近
- 用渐变、模糊、悬浮、遮罩去软化区域切换
- 用抽象基类统一面板节奏和状态管理

## 8. 不适合直接照搬的点

- AI 搜索、翻译、阅读、绘图这些具体技能面板
- 对话产品特有的输入模式切换
- 强业务绑定的参数面板内容

## 9. 映射到 MoeMemos

这一页对 `MemoInputPage` 的参考价值最高：

- 输入页不应只是一块文本编辑器
- 底部工具栏、附件、状态反馈应当被设计成统一系统
- 键盘弹起后的布局必须稳定
- 输入页应该有自己的空间层次，而不是简单 `Scaffold + TextField`

优先级：P0

原因：`MoeMemosAndroid` 当前最容易快速拉开质感差距的地方就是输入体验。
