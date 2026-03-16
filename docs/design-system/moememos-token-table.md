# MoeMemos 第一版 Design Token 表

目标：把 `yuanbao` 审计里反复出现的设计规律，转成适合 `MoeMemosAndroid` 的第一版 token。

这不是逐值拷贝 `yuanbao`，而是根据它的设计方法，建立 `MoeMemosAndroid` 自己的可复用语义层。

---

## 1. 设计原则

- 先定义语义，不先定义页面特例。
- 先保证统一层级，再追求局部精致。
- 先做能覆盖首页、详情页、输入页的 token。
- 不把 `MaterialTheme.colorScheme.*` 直接当设计系统本体。

---

## 2. Color Token

建议第一版至少稳定下面这些语义色。

| Token | 用途 | Light | Dark |
| --- | --- | --- | --- |
| `bgApp` | 全局底色，页面最外层背景 | `#F6F4EF` | `#14171A` |
| `bgSurface` | 普通内容面 | `#FFFCF7` | `#1B2024` |
| `bgElevated` | 浮起卡片、输入容器 | `#FFFFFF` | `#22282D` |
| `bgOverlay` | 弹层、浮层、模糊层底色 | `#FFF8EF` | `#2A3036` |
| `bgPressed` | 按压反馈底色 | `#ECE6DC` | `#313941` |
| `textPrimary` | 主文本 | `#171A1C` | `#F4F3EF` |
| `textSecondary` | 次文本 | `#596068` | `#B3BCC3` |
| `textTertiary` | 辅助文本、提示文本 | `#808891` | `#8A949D` |
| `strokeSubtle` | 轻分隔线、弱描边 | `#E5DED2` | `#343B42` |
| `strokeStrong` | 强描边、选中边界 | `#CFC6B9` | `#4B555E` |
| `accentPrimary` | 主强调色 | `#17643E` | `#79D7A5` |
| `accentSoft` | 轻强调背景 | `#DCEFE3` | `#1D4A33` |
| `accentDanger` | 危险操作 | `#C44D42` | `#FF8F84` |
| `accentWarning` | 警示、草稿、处理中 | `#B97818` | `#F0B457` |
| `accentInfo` | 链接、辅助功能入口 | `#2D6CA3` | `#87BFF1` |

说明：

- `yuanbao` 的关键特征不是单色，而是背景层级很多，所以这里把 `bg*` 拆得更细。
- `MoeMemosAndroid` 现有绿色可以保留，但应从“品牌主色”收敛为 `accentPrimary`，不要再让它主导所有层级。

---

## 3. Spacing Token

建议第一版只保留一套简单网格，避免页面里继续出现随机间距。

| Token | 值 | 主要用途 |
| --- | --- | --- |
| `xxs` | `2.dp` | 细微对齐、描边补偿 |
| `xs` | `4.dp` | 紧密元素间距 |
| `sm` | `8.dp` | 图标与文字、小组块间距 |
| `md` | `12.dp` | 卡片内部紧凑间距 |
| `lg` | `16.dp` | 默认卡片内边距、按钮内边距 |
| `xl` | `20.dp` | 页面左右边距、主要模块边距 |
| `xxl` | `24.dp` | 大区块分隔 |
| `xxxl` | `32.dp` | 首屏、空状态、大模块留白 |

规则：

- 页面横向边距优先使用 `16 / 20`
- 卡片内边距优先使用 `12 / 16`
- 模块间主间距优先使用 `20 / 24`

---

## 4. Radius Token

`yuanbao` 很明显依赖统一圆角体系。`MoeMemosAndroid` 也应该尽快从“组件自带圆角”改成固定 token。

| Token | 值 | 主要用途 |
| --- | --- | --- |
| `xs` | `8.dp` | 小 chip、小图标底板 |
| `sm` | `12.dp` | 标签、轻按钮、输入附属项 |
| `md` | `16.dp` | 默认卡片、输入容器 |
| `lg` | `20.dp` | 浮层卡片、首页大卡片 |
| `xl` | `28.dp` | 底部弹层、大型品牌卡 |
| `full` | `50.dp` | 胶囊按钮、状态 pill |

规则：

- 首页卡片统一优先 `16 / 20`
- 输入页容器统一优先 `16`
- 浮动按钮和 chip 不混用圆角

---

## 5. Elevation Token

第一版不直接照搬 Material elevation，而是保留语义层级。

| Token | 阴影/层级语义 | 主要用途 |
| --- | --- | --- |
| `flat` | 无阴影，仅靠背景区分 | 普通内容面 |
| `raised` | 轻浮起，低模糊低偏移 | 默认卡片、输入区 |
| `floating` | 中浮起，适合悬浮操作 | FAB、轻浮层、参数按钮 |
| `overlay` | 高层浮层，必要时叠加描边 | 弹层、面板、抽屉内容 |

第一版建议：

- Light 模式尽量使用“弱阴影 + 细描边”
- Dark 模式尽量少用重阴影，多用“更亮的背景层 + 描边”

---

## 6. Typography Token

当前 `Type.kt` 基本还是默认配置，不足以支撑更强设计语言。第一版建议如下。

| Token | 建议值 | 用途 |
| --- | --- | --- |
| `display` | `32sp / SemiBold / 38sp` | 首屏标题、空状态主标题 |
| `headline` | `24sp / SemiBold / 30sp` | 页面主标题 |
| `title` | `18sp / SemiBold / 24sp` | 卡片标题、区块标题 |
| `body` | `15sp / Normal / 22sp` | 正文 |
| `bodyStrong` | `15sp / Medium / 22sp` | 强调正文、关键字段 |
| `caption` | `12sp / Normal / 16sp` | 辅助信息、次级说明 |
| `label` | `13sp / Medium / 18sp` | 按钮、标签、chip |
| `monoNumber` | `13sp / SemiBold / 16sp` | 统计数字、计数徽标 |

规则：

- 标题和正文不能只差字号，必须拉开字重
- 次级信息既要变浅，也要缩小或减弱字重
- 统计类数字和标签建议单独建样式

---

## 7. Motion Token

这是最接近 `yuanbao` 质感差异的地方。第一版先把时长和曲线定住。

| Token | 值 | 用途 |
| --- | --- | --- |
| `fast` | `120ms` | 按压、轻状态切换 |
| `normal` | `220ms` | 卡片显隐、内容切换 |
| `slow` | `320ms` | 页面区块入场、底栏联动 |
| `emphasized` | `420ms` | 首屏或较重浮层转场 |

曲线建议：

- `standard`：默认状态切换
- `decelerate`：进入
- `accelerate`：退出
- `springSoft`：FAB、浮层、按压回弹

应优先覆盖：

- 首页顶部透明/位移联动
- FAB 显隐
- 输入框聚焦
- 图片/附件加入反馈
- 底部工具栏切换

---

## 8. 组件到 Token 的落点

第一批组件建议这样接 token：

| 组件 | 主要 token |
| --- | --- |
| `MoeCard` | `bgElevated` `strokeSubtle` `radius.md/lg` `elevation.raised` |
| `MoeAppBar` | `bgOverlay` `textPrimary` `strokeSubtle` |
| `MoeInputField` | `bgElevated` `bgPressed` `textPrimary` `textTertiary` `radius.md` |
| `MoePrimaryButton` | `accentPrimary` `textPrimaryReverse` 或浅色对比文本 |
| `MoeTagChip` | `bgSurface` `strokeSubtle` `label` `radius.full` |
| `MoeFloatingActionButton` | `accentPrimary` `radius.lg/full` `elevation.floating` |

---

## 9. 第一批落地顺序

1. 先在代码里补 token 骨架
2. 再让 `MoeCard`、`MoeAppBar`、`MoeInputField` 使用 token
3. 最后再替换首页和输入页

原因：

- 先换页面，不换 token，后面会返工
- 先换 token，不换页面，风险最小

---

## 10. 第一版验收标准

- 能在代码中不再直接散写常用颜色、间距、圆角
- 首页、详情页、输入页可以共享同一套语义层级
- 后续做组件时不再讨论“这个值写多少”，而是选 token
- 整个项目开始从“默认 Material 3 应用”转向“有自己设计系统的产品”
