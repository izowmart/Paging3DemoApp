package com.example.paging3demoapp.presentation.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.paging3demoapp.R
import com.example.paging3demoapp.model.UnsplashedImage
import com.example.paging3demoapp.model.Urls
import com.example.paging3demoapp.model.User
import com.example.paging3demoapp.model.UserLinks
import com.example.paging3demoapp.ui.theme.HeartRed
import timber.log.Timber

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ListContent(items: LazyPagingItems<UnsplashedImage>, padding: PaddingValues) {
    Timber.d("Error", items.loadState.toString())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(items = items, key = { unsplashImage ->
            unsplashImage.id
        }) { unsplashImage ->
            unsplashImage?.let {
                UnsplashItem(unsplashImage = it)
            }
        }
    }

}

@ExperimentalCoilApi
@Composable
fun UnsplashItem(unsplashImage: UnsplashedImage) {

    val context = LocalContext.current
    Box(modifier = Modifier
        .clickable {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://unsplash.com/@${unsplashImage.user.username}?utm_source=DemoApp&utm_medium=referral")
            )
            startActivity(context, browserIntent, null)
        }
        .height(300.dp)
        .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter)
    {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(unsplashImage.urls.regular)
                .crossfade(durationMillis = 1000)
                .build(),
            error = painterResource(R.drawable.ic_placeholder),
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentDescription = "Unsplash Image",
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .alpha(ContentAlpha.medium),
            color = Color.Black
        ) {}
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Photo by ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                        append(unsplashImage.user.username)
                    }
                    append(" on Unsplash")
                },
                color = Color.White,
                fontSize = MaterialTheme.typography.caption.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            LikeCounter(
                modifier = Modifier.weight(3f),
                painter = painterResource(id = R.drawable.ic_heart),
                likes = "${unsplashImage.likes}"
            )
        }
    }
}

@Composable
fun LikeCounter(
    modifier: Modifier, painter: Painter, likes: String
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painter, contentDescription = "Heart Icon", tint = HeartRed
        )
        Divider(modifier = Modifier.width(6.dp))
        Text(
            text = likes,
            color = Color.White,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@ExperimentalCoilApi
@Composable
@Preview
fun UnsplashImagePreview() {
    UnsplashItem(
        unsplashImage = UnsplashedImage(
            id = "1",
            urls = Urls(regular = ""),
            likes = 100,
            user = User(username = "Stevdza-San", userLinks = UserLinks(html = ""))
        )
    )
}