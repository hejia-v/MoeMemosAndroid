package me.mudkip.moememos.ui.page.memos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import me.mudkip.moememos.R
import me.mudkip.moememos.ext.string
import me.mudkip.moememos.ui.designsystem.component.MoeAppBar
import me.mudkip.moememos.ui.designsystem.foundation.MoeDesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedMemoPage(
    drawerState: DrawerState? = null
) {
    val scope = rememberCoroutineScope()
    val colors = MoeDesignTokens.colors

    Scaffold(
        containerColor = colors.bgApp,
        topBar = {
            MoeAppBar(
                title = R.string.archived.string,
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
            ArchivedMemoList(
                contentPadding = innerPadding
            )
        }
    )
}
