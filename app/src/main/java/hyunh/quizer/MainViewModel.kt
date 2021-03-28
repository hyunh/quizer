package hyunh.quizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class MainViewModel : ViewModel() {

    private val quizzes = MutableLiveData<List<String>>(emptyList())
    val hasQuiz = quizzes.map {
        it.isNotEmpty()
    }
    private val index = MutableLiveData(0)

    val id = index.map { index ->
        hasQuiz.value?.takeIf { it }?.let {
            "${index + 1} / ${quizzes.value?.size}"
        }
    }
    val quiz: LiveData<String?> = index.map { idx ->
        quizzes.value?.takeIf {
            it.isNotEmpty()
        }?.get(idx)
    }

    fun setQuiz(quizzesArray: Array<String>) {
        quizzesArray.shuffle()
        quizzes.postValue(mutableListOf(*quizzesArray))
        index.postValue(0)
    }

    fun shuffle() {
        val shuffled = quizzes.value?.shuffled()
                ?: return
        quizzes.postValue(shuffled)
        index.postValue(0)
    }

    fun prev() {
        val currentIndex = index.value
                ?: return
        val prevIndex = when {
            currentIndex != 0 -> currentIndex - 1
            else -> quizzes.value?.size?.let { it - 1 } ?: 0
        }
        index.postValue(prevIndex)
    }

    fun next() {
        val currentIndex = index.value
                ?: return
        val nextIndex = when {
            currentIndex + 1 != quizzes.value?.size -> currentIndex + 1
            else -> 0
        }
        index.postValue(nextIndex)
    }
}
