package uk.shakhzod.gamedrawing.ui.setup.drawing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.shakhzod.gamedrawing.databinding.ActivityDrawingBinding

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

class DrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}