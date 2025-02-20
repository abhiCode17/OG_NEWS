import android.content.Context
import android.content.SharedPreferences
import com.app.ognews.model.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookmarkManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveBookmark(article: Article) {
        val bookmarks = getBookmarks().toMutableList()
        if (!bookmarks.contains(article)) {
            bookmarks.add(article)
            prefs.edit().putString("bookmarked_articles", gson.toJson(bookmarks)).apply()
        }
    }

    fun removeBookmark(article: Article) {
        val bookmarks = getBookmarks().toMutableList()
        bookmarks.removeIf { it.title == article.title } // Remove by title (or use unique ID)
        prefs.edit().putString("bookmarked_articles", gson.toJson(bookmarks)).apply()
    }

    fun getBookmarks(): List<Article> {
        val json = prefs.getString("bookmarked_articles", null) ?: return emptyList()
        val type = object : TypeToken<List<Article>>() {}.type
        return gson.fromJson(json, type)
    }
}