package org.example.project.presentation.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.ic_empty_photo
import kts_project_haidukov.composeapp.generated.resources.online
import kts_project_haidukov.composeapp.generated.resources.user_avatar
import kts_project_haidukov.composeapp.generated.resources.user_avatar_placeholder
import org.example.project.presentation.mockDatabase.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val currentUiState by viewModel.state.collectAsState()

    ListUserView(currentUiState.listUser)
}

@Composable
private fun ListUserView(
    listUser: ImmutableList<User>
) {
    LazyColumn(modifier = Modifier.statusBarsPadding()) {
        items(listUser) { user ->
            UserPreview(user)
        }
    }
}

@Composable
private fun UserPreview(
    user: User
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color.Gray)
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!user.userAvatar.isNullOrEmpty()) {
            AsyncImage(
                model = user.userAvatar,
                contentDescription = stringResource(Res.string.user_avatar),
                modifier = Modifier
                    .size(90.dp)
                    .clip(shape = RoundedCornerShape(200.dp)),
                placeholder = painterResource(Res.drawable.ic_empty_photo),
                error = painterResource(Res.drawable.ic_empty_photo),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.ic_empty_photo),
                contentDescription = stringResource(Res.string.user_avatar_placeholder),
                modifier = Modifier
                    .size(90.dp)
                    .clip(shape = RoundedCornerShape(200.dp)),
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = user.username)
            Text(text = user.userFirstName + " " + user.userLastName)
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = if (user.isUserOnline) {
                    stringResource(Res.string.online)
                } else {
                    user.lastUserTimeOnline ?: ""
                }
            )
        }
    }
}