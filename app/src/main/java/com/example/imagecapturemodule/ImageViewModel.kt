package com.example.imagecapturemodule

import androidx.lifecycle.*
import com.example.imagecapturemodule.database.ImageDetails
import com.example.imagecapturemodule.database.ImageRepository
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    // val allWords: List<ImageDetails> = repository.allimageDetailss.asLiveData()
    //  val allWords: MutableList<ImageDetails> = mutableListOf()
    var current_image_detail = ImageDetails()
    var current_ques = ""
    var current_ans = ""

    fun insert(image: String) = viewModelScope.launch {
        repository.insert(
            ImageDetails(
                question = current_ques,
                answer_marked = current_ans,
                image = image
            )
        )

    }

    /*fun insertImage(image: String) {
        current_image_detail.image = image
    }

    fun insertQues(ques: String) {
        current_image_detail.question = ques
    }

    fun insertAns(ans: String) {
        current_image_detail.answer_marked = ans
    }*/

    /* fun submitAllData() {
         allWords.forEach {
             insert(it)
         }
     }*/
}

class ImageViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
