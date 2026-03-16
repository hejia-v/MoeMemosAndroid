# Yuanbao 第一批页面审计

说明：这是一版静态审计，依据来自反编译后的代码、XML 布局、资源命名和主题 token，而不是运行时截图。

这批页面的选择标准：

- 对 `MoeMemosAndroid` 后续复刻最有参考价值
- 能覆盖首屏气质、首页壳层、内容页、输入体验
- 能帮助抽取设计 token、组件模式和交互规则

本批次包含：

- [01 登录页](./01-login-page.md)
- [02 首页壳层与聊天主 Tab](./02-home-shell-and-chat-tab.md)
- [03 会话页](./03-session-page.md)
- [04 输入面板系统](./04-input-panel-system.md)

这批审计的核心结论：

- `yuanbao` 的完成度不是来自单页，而是来自“壳层 + 页面 + 输入面板 + token”一起设计。
- UI 技术上是原生 Android，且是 Compose 与 XML/ViewBinding 混合架构。
- 设计语言已经明显系统化，至少能看到明确的语义色、层级背景、文本层级、轮廓层级和圆角层级。
- 对 `MoeMemosAndroid` 来说，最值得先借鉴的是首页壳层、输入面板和顶部区域处理，而不是去抄聊天产品专属页面结构。

本批次证据主要来自：

- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\login\v2`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\home\v2`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\session`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\java\com\tencent\hunyuan\app\chat\biz\chats\conversation\inputpanel`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\layout`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\drawable`
- `C:\Users\Admin\Downloads\GameRip\yuanbao\app\src\main\res\values`

这批文档适合直接作为后续 3 件事的输入：

- `MoeMemosAndroid` 的 Design Token 设计
- `MoeCard` / `MoeAppBar` / `MoeInputField` 的首版实现
- 首页与输入页的高保真改造
