package com.example.banglanews.view

import ads_mobile_sdk.h6
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.banglanews.model.NewsArticle
import com.example.banglanews.model.NewsRepository
import com.example.banglanews.model.RetrofitClient
import com.example.banglanews.ui.theme.BanglaNewsTheme
import com.example.banglanews.viewmodel.NewsViewModel
import com.example.banglanews.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = NewsRepository(RetrofitClient.instance)
        val viewModel: NewsViewModel = ViewModelProvider(this,
            NewsViewModelFactory(repository))[NewsViewModel::class.java]

        setContent {
            NewsApp(viewModel = viewModel)
        }
    }
}
@Composable
fun NewsApp(viewModel: NewsViewModel) {
    val articles by viewModel.articles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews("sports") // default category
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(articles) { article ->
                    NewsCard(article)
                }
            }
        }
    }
}

@Composable
fun NewsCard(article: NewsArticle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = article.title ?: "No Title", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.description ?: "No Description", style = MaterialTheme.typography.body2)
        }
    }
}
