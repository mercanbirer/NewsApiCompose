package com.example.newsapicompose.view

import android.util.Log
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.newsapicompose.di.AppModule
import com.example.newsapicompose.di.Resource
import com.example.newsapicompose.viewmodel.MainViewModel
import timber.log.Timber

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val news = viewModel.apiNews.collectAsState()
    val isList = remember { mutableStateOf(false) }

    LaunchedEffect(Unit, block = {
        viewModel.getNews("tr", "df038a2589b7445cb7e0f762e4db63d4")
    })

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "News List",
                textAlign = TextAlign.Center,
                fontWeight =  FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

        }

        when (news.value) {
            is Resource.Loading -> {
            }
            is Resource.Error -> {
                if (news.value.message != null) {
                    Timber.tag("Error").e(news.value.message)
                }
            }
            is Resource.Success -> {
                isList.value = true
            }
            else -> {
            }
        }

        if (isList.value) {
            val newsList = news.value.data!!.articles
            LazyColumn {
                items(items = newsList) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp, 4.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = 4.dp
                    ) {
                        Surface() {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .clickable { it.url },
                            ) {
                                Image(
                                    painter = rememberImagePainter(
                                        data = it.urlToImage,
                                        builder = {
                                            size(OriginalSize)
                                            scale(Scale.FILL)
                                            transformations(RoundedCornersTransformation())
                                        }
                                    ),
                                    contentDescription = it.description,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop,
                                )

                                Text(
                                    text = it.title ?: "",
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = it.description ?: "",
                                    style = MaterialTheme.typography.body1,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                            }
                        }
                    }

                }
            }
        }
    }
}