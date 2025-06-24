package co.edu.udea.compumovil.gr05_20251.lab2

// Required imports
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
// Data Model
data class BlogPost(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

// API Client using HttpURLConnection
object ApiClient {
    suspend fun fetchPosts(): Result<List<BlogPost>> = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://jsonplaceholder.typicode.com/posts")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.setRequestProperty("User-Agent", "BlogApp/1.0")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = reader.readText()
                reader.close()
                connection.disconnect()

                val gson = Gson()
                val type = object : TypeToken<List<BlogPost>>() {}.type
                val posts: List<BlogPost> = gson.fromJson(response, type)
                Result.success(posts)
            } else {
                connection.disconnect()
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// WorkManager Worker
class BlogSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = ApiClient.fetchPosts()
            when {
                result.isSuccess -> {
                    val posts = result.getOrNull() ?: emptyList()
                    if (posts.isNotEmpty()) {
                        // Store in SharedPreferences for simplicity
                        val prefs = applicationContext.getSharedPreferences("blog_cache", Context.MODE_PRIVATE)
                        val gson = Gson()
                        val postsJson = gson.toJson(posts.take(20)) // Limit to 20 posts
                        prefs.edit().putString("cached_posts", postsJson).apply()
                        prefs.edit().putLong("last_update", System.currentTimeMillis()).apply()
                    }
                    Result.success()
                }
                else -> Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

// ViewModel
class BlogViewModel(private val context: Context) : ViewModel() {

    private val _posts = MutableStateFlow<List<BlogPost>>(emptyList())
    val posts: StateFlow<List<BlogPost>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val prefs = context.getSharedPreferences("blog_cache", Context.MODE_PRIVATE)

    init {
        loadCachedPosts()
        schedulePeriodicSync()
        // Load fresh data if cache is empty or old
        if (_posts.value.isEmpty() || isCacheOld()) {
            refreshPosts()
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null

            val result = ApiClient.fetchPosts()
            when {
                result.isSuccess -> {
                    val posts = result.getOrNull() ?: emptyList()
                    if (posts.isNotEmpty()) {
                        _posts.value = posts.take(20)
                        // Cache the posts
                        val gson = Gson()
                        val postsJson = gson.toJson(posts.take(20))
                        prefs.edit().putString("cached_posts", postsJson).apply()
                        prefs.edit().putLong("last_update", System.currentTimeMillis()).apply()
                    } else {
                        _error.value = "No posts received from server"
                    }
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    _error.value = "Failed to load posts: ${exception?.message ?: "Unknown error"}"
                }
            }
            _isRefreshing.value = false
        }
    }

    private fun loadCachedPosts() {
        val cachedJson = prefs.getString("cached_posts", null)
        if (cachedJson != null) {
            try {
                val gson = Gson()
                val type = object : TypeToken<List<BlogPost>>() {}.type
                val cachedPosts: List<BlogPost> = gson.fromJson(cachedJson, type)
                _posts.value = cachedPosts
            } catch (e: Exception) {
                // Ignore cache errors
            }
        }
    }

    private fun isCacheOld(): Boolean {
        val lastUpdate = prefs.getLong("last_update", 0)
        val oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000) // 1 hour
        return lastUpdate < oneHourAgo
    }

    private fun schedulePeriodicSync() {
        val workRequest = PeriodicWorkRequestBuilder<BlogSyncWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "blog_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

// ViewModelFactory
class BlogViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlogViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Compose UI
@Composable
fun BlogFeedScreen(viewModel: BlogViewModel) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val error by viewModel.error.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refreshPosts() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            error?.let {
                item {
                    ErrorCard(
                        error = error!!,
                        onRetry = { viewModel.refreshPosts() }
                    )
                }
            }

            items(posts) { post ->
                BlogPostCard(post = post)
            }

            if (posts.isEmpty() && !isLoading && error == null) {
                item {
                    EmptyStateCard()
                }
            }
        }

        if (isLoading && posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BlogPostCard(post: BlogPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Author: User ${post.userId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ErrorCard(error: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No blog posts available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: BlogViewModel = viewModel(
                        factory = BlogViewModelFactory(this@MainActivity)
                    )
                    BlogFeedScreen(viewModel = viewModel)
                }
            }
        }
    }
}

