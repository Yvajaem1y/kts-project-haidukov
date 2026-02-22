package org.example.project.helloScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kts_project_haidukov.composeapp.generated.resources.Res
import kts_project_haidukov.composeapp.generated.resources.ic_empty_photo
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun HelloScreen(
    goToLogin : () -> Unit
){
    Column(
        modifier = Modifier.statusBarsPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = "https://avatars.mds.yandex.net/i?id=ccc69626162807c656adfabaf538e35e_l-10340155-images-thumbs&n=13",
            contentDescription = "image",
            placeholder = painterResource(Res.drawable.ic_empty_photo),
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Crop
        )

        Text("Hello")
        Button(
            onClick = { goToLogin() }
        ){
            Text("Go to login")
        }
    }


}