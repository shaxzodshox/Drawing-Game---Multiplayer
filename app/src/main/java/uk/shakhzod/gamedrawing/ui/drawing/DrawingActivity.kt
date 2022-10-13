package uk.shakhzod.gamedrawing.ui.drawing

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.databinding.ActivityDrawingBinding
import uk.shakhzod.gamedrawing.util.Constants.DEFAULT_PAINT_THICKNESS
import uk.shakhzod.gamedrawing.util.OnDrawerClosedListener
import uk.shakhzod.gamedrawing.util.extensions.onClick

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@AndroidEntryPoint
class DrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawingBinding

    private val viewModel: DrawingViewModel by viewModels()

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rvPlayers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subsribeToUiStateUpdates()

        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        toggle.syncState()

        val header = layoutInflater.inflate(R.layout.nav_drawer_header, binding.navView)
        rvPlayers = header.findViewById(R.id.rvPlayers)
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.ibPlayers.onClick {
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.root.openDrawer(GravityCompat.START)
        }

        binding.root.addDrawerListener(OnDrawerClosedListener {
             binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        })

        binding.colorGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.checkRadioButton(checkedId)
        }
    }

    private fun selectColor(color: Int){
        binding.drawingView.setColor(color)
        binding.drawingView.setThickness(DEFAULT_PAINT_THICKNESS)
    }

    private fun subsribeToUiStateUpdates(){
        lifecycleScope.launchWhenStarted {
            viewModel.selectedColorButtonId.collect{ id ->
                binding.colorGroup.check(id)
                when(id){
                    R.id.rbBlack -> selectColor(Color.BLACK)
                    R.id.rbBlue -> selectColor(ContextCompat.getColor(this@DrawingActivity,android.R.color.holo_blue_dark))
                    R.id.rbGreen -> selectColor(Color.GREEN)
                    R.id.rbOrange -> selectColor(
                        ContextCompat.getColor(this@DrawingActivity, R.color.orange)
                    )
                    R.id.rbRed -> selectColor(Color.RED)
                    R.id.rbYellow -> selectColor(Color.YELLOW)
                    R.id.rbEraser -> {
                        binding.drawingView.setColor(Color.WHITE)
                        binding.drawingView.setThickness(40f)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}