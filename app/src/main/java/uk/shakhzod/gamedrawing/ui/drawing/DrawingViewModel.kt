package uk.shakhzod.gamedrawing.ui.drawing

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.util.DispatcherProvider
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 13/10/22
 **/

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val gson : Gson
) : ViewModel() {
    private val _selectedColorButtonId  = MutableStateFlow(R.id.rbBlack)
    val selectedColorButtonId: StateFlow<Int> = _selectedColorButtonId

    fun checkRadioButton(id: Int){
        _selectedColorButtonId.value = id
    }
}