package me.mudkip.moememos.ui.page.memos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens
import me.mudkip.moememos.ui.page.common.RouteName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagMemoPage(
    drawerState: DrawerState? = null,
    tag: String,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val normalizedCurrentTag = remember(tag) { normalizeTag(tag) }
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = tag,
                navigationIcon = {
                    if (drawerState != null) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = R.string.menu.string)
                        }
                    }
                }
            )
        },

        content = { innerPadding ->
            MemosList(
                contentPadding = innerPadding,
                tag = tag,
                onTagClick = { clickedTag ->
                    if (normalizeTag(clickedTag) == normalizedCurrentTag) {
                        return@MemosList
                    }
                    navController.navigate("${RouteName.TAG}/${java.net.URLEncoder.encode(clickedTag, "UTF-8")}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    )
}

private fun normalizeTag(tag: String): String {
    return tag.removePrefix("#")
}
