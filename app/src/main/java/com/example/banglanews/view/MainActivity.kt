package com.example.banglanews.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.banglanews.model.NewsArticle
import com.example.banglanews.model.NewsRepository
import com.example.banglanews.model.RetrofitClient
import com.example.banglanews.model.newsCategories
import com.example.banglanews.viewmodel.NewsViewModel
import com.example.banglanews.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.flow.debounce

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.show(WindowInsetsCompat.Type.statusBars())
        insetsController.isAppearanceLightStatusBars = true
        val repository = NewsRepository(RetrofitClient.instance)
        val viewModel: NewsViewModel = ViewModelProvider(this,
            NewsViewModelFactory(repository))[NewsViewModel::class.java]

        setContent {
            NewsApp(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NewsApp(viewModel: NewsViewModel) {
    val articles by viewModel.articles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val selectedCategory = newsCategories[selectedTabIndex]

    LaunchedEffect(Unit) {
        snapshotFlow { selectedTabIndex }
            .debounce(300)
            .collect { tabIndex ->
                val category = newsCategories[tabIndex]
                Log.i("MainActivity", "Fetching news for $category")
                viewModel.fetchNews(category)
            }
    }

    // Use a Scaffold with a TopAppBar to ensure the status bar remains visible
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("BanglaNews") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Log.i("MainActivity", "SelectedTab Index: $selectedTabIndex")
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp
            ) {
                newsCategories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(category.replaceFirstChar {
                                it.uppercase()
                            })
                        }
                    )
                }
            }

            Text(
                text = "${selectedCategory.replaceFirstChar { it.uppercase() }} News",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                val refreshState = rememberPullRefreshState(isLoading, onRefresh = {
                    viewModel.fetchNews(selectedCategory)
                })
                Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(articles) { article ->
                            NewsCard(article)
                        }
                    }
                    PullRefreshIndicator(isLoading, refreshState, Modifier.align(Alignment.TopCenter))
                }
            }
        }
    }
}

@Composable
fun NewsCard(article: NewsArticle) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Log.i("MainActivity", "article: $article")
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = article.title ?: "No Title", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.description ?: "No Description", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(8.dp))
            article.link?.let { link ->
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
                    context.startActivity(intent)
                }) {
                    Text(text = "Read Full Article")
                }
            }
        }
    }
}
