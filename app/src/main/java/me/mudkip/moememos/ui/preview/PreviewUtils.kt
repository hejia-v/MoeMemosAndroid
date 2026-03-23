package me.mudkip.moememos.ui.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import me.mudkip.moememos.data.local.entity.MemoEntity
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.data.model.AppThemeMode
import me.mudkip.moememos.data.model.LocalAccount
import me.mudkip.moememos.data.model.MemoVisibility
import me.mudkip.moememos.data.model.User
import me.mudkip.moememos.ui.theme.MoeMemosTheme
import java.time.Instant

/**
 * Preview theme wrapper that supports light/dark mode.
 * Use this in @PreviewLightDark annotated preview functions.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system setting.
 */
@Composable
fun MoeMemosPreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MoeMemosTheme(
        themeMode = if (darkTheme) AppThemeMode.DARK else AppThemeMode.LIGHT,
        dynamicColor = false,
        content = content
    )
}

/**
 * Sample data objects for previews.
 */
object PreviewData {
    /**
     * Sample memo entity for preview.
     */
    val sampleMemo: MemoEntity = MemoEntity(
        identifier = "preview-memo-1",
        remoteId = "1",
        accountKey = "preview-account",
        content = "# Hello World\n\nThis is a sample memo for preview purposes.\n\n- Item 1\n- Item 2\n- Item 3\n\n**Bold text** and *italic text* are supported.",
        date = Instant.now().minusSeconds(3600), // 1 hour ago
        visibility = MemoVisibility.PRIVATE,
        pinned = false,
        archived = false,
        needsSync = false
    )

    /**
     * Sample pinned memo for preview.
     */
    val samplePinnedMemo: MemoEntity = MemoEntity(
        identifier = "preview-memo-pinned",
        remoteId = "2",
        accountKey = "preview-account",
        content = "This is a pinned memo!",
        date = Instant.now().minusSeconds(86400), // 1 day ago
        visibility = MemoVisibility.PUBLIC,
        pinned = true,
        archived = false,
        needsSync = false
    )

    /**
     * Sample memo with long content for preview.
     */
    val sampleLongMemo: MemoEntity = MemoEntity(
        identifier = "preview-memo-long",
        remoteId = "3",
        accountKey = "preview-account",
        content = "This is a very long memo content that should demonstrate how the component handles longer text. ".repeat(5),
        date = Instant.now().minusSeconds(172800), // 2 days ago
        visibility = MemoVisibility.PROTECTED,
        pinned = false,
        archived = false,
        needsSync = false
    )

    /**
     * Sample memo that needs sync.
     */
    val sampleSyncPendingMemo: MemoEntity = MemoEntity(
        identifier = "preview-memo-sync",
        remoteId = null,
        accountKey = "preview-account",
        content = "This memo is pending sync.",
        date = Instant.now(),
        visibility = MemoVisibility.PRIVATE,
        pinned = false,
        archived = false,
        needsSync = true
    )

    /**
     * List of sample memos for preview.
     */
    val sampleMemos: List<MemoEntity> = listOf(
        samplePinnedMemo,
        sampleMemo,
        sampleLongMemo,
        sampleSyncPendingMemo
    )

    /**
     * Sample archived memo for preview.
     */
    val sampleArchivedMemo: MemoEntity = MemoEntity(
        identifier = "preview-memo-archived",
        remoteId = "4",
        accountKey = "preview-account",
        content = "This is an archived memo.",
        date = Instant.now().minusSeconds(604800), // 1 week ago
        visibility = MemoVisibility.PRIVATE,
        pinned = false,
        archived = true,
        needsSync = false
    )

    /**
     * Sample local account for preview.
     */
    val sampleLocalAccount: Account.Local = Account.Local(
        info = LocalAccount(
            enabled = true,
            startDateEpochSecond = Instant.now().minusSeconds(31536000).epochSecond // 1 year ago
        )
    )

    /**
     * Sample user for preview.
     */
    val sampleUser: User = User(
        identifier = "1",
        name = "Preview User",
        startDate = Instant.now().minusSeconds(31536000), // 1 year ago
        defaultVisibility = MemoVisibility.PRIVATE,
        avatarUrl = null
    )
}
