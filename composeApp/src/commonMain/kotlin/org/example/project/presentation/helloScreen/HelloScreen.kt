package org.example.project.presentation.helloScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.continue_as_guest
import kts_project_haidukov.composeapp.generated.resources.go_to_login_button
import kts_project_haidukov.composeapp.generated.resources.hello_message
import kts_project_haidukov.composeapp.generated.resources.hello_subtitle
import kts_project_haidukov.composeapp.generated.resources.ic_empty_photo
import kts_project_haidukov.composeapp.generated.resources.image_description
import org.example.project.presentation.common.GitHubTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HelloScreen(
    goToLogin: () -> Unit,
) {
    val colors = GitHubTheme.colors

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.backgroundPrimary
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https://i.pinimg.com/736x/63/de/54/63de5479e8eb4676570c49e2579cab01.jpg",
                contentDescription = stringResource(Res.string.image_description),
                placeholder = painterResource(Res.drawable.ic_empty_photo),
                error = painterResource(Res.drawable.ic_empty_photo),
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(Res.string.hello_message),
                color = colors.textPrimary,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.hello_subtitle),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { goToLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnColor
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(Res.string.go_to_login_button),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.continue_as_guest),
                color = colors.textLink,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {  }
            )
        }
    }
}