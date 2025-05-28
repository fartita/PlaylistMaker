package com.example.playlistmaker.presentation.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.App.Companion.appColorScheme
import com.example.playlistmaker.presentation.viewmodels.settings.SettingsViewModel


@Composable
fun SettingsScreen(viewModel: SettingsViewModel, context: Context) {
    val themeSettings by viewModel.darkThemeState.observeAsState()

    val darkThemeEnabled = themeSettings ?: false
    (context.applicationContext as App).switchTheme(darkThemeEnabled)

    SettingsScreenContent(
        darkThemeEnabled = darkThemeEnabled,
        onThemeSwitch = { viewModel.execute(SettingsViewModel.ActionType.Theme(it)) },
        onShareApp = { Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
                        type = "text/plain"
                        context.startActivity(Intent.createChooser(this, null))
        } },
        onSupportEmail = {
                    Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_address)))
                        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_theme))
                        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_message))
                        context.startActivity(Intent.createChooser(this, null))
                    }
                         },
        onOpenAgreement = { Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(context.getString(R.string.support_user_agreement))
                            context. startActivity(Intent.createChooser(this, null))
        }}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    darkThemeEnabled: Boolean,
    onThemeSwitch: (Boolean) -> Unit,
    onShareApp: () -> Unit,
    onSupportEmail: () -> Unit,
    onOpenAgreement: () -> Unit
) {

    AppTheme() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appColorScheme.background)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        color = appColorScheme.onBackground,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_medium, FontWeight.Bold))
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = appColorScheme.background, containerColor = appColorScheme.background),
                modifier = Modifier.fillMaxWidth()
            )

            SettingsItem(
                text = stringResource(id = R.string.dark_theme),
                trailingContent = {
                    Switch(
                        modifier = Modifier.padding(end = 12.dp),
                        checked = darkThemeEnabled,
                        onCheckedChange = onThemeSwitch,
                        colors = SwitchDefaults.colors(checkedThumbColor = appColorScheme.primary)
                    )
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.share_app),
                trailingContent = {
                    IconButton(onClick = onShareApp) {
                        Icon(
                            modifier = Modifier.padding(end = 20.dp),
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = null,
                            tint = appColorScheme.onSecondary
                        )
                    }
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.write_support),
                trailingContent = {
                    IconButton(onClick = onSupportEmail) {
                        Icon(
                            modifier = Modifier.padding(end = 20.dp),
                            painter = painterResource(id = R.drawable.ic_support),
                            contentDescription = null,
                            tint = appColorScheme.onSecondary
                        )
                    }
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.user_term),
                trailingContent = {
                    IconButton(onClick = onOpenAgreement) {
                        Icon(
                            modifier = Modifier.padding(end = 20.dp),
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = null,
                            tint = appColorScheme.onSecondary
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsItem(
    text: String,
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            color = appColorScheme.onBackground
        )
        trailingContent()
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = appColorScheme,
        shapes = Shapes(),
        content = content
    )
}
