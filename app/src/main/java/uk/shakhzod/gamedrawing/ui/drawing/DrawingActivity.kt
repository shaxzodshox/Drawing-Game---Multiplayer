package uk.shakhzod.gamedrawing.ui.drawing

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.data.remote.ws.models.DrawAction
import uk.shakhzod.gamedrawing.data.remote.ws.models.GameError
import uk.shakhzod.gamedrawing.data.remote.ws.models.JoinRoomHandshake
import uk.shakhzod.gamedrawing.databinding.ActivityDrawingBinding
import uk.shakhzod.gamedrawing.util.Constants.DEFAULT_PAINT_THICKNESS
import uk.shakhzod.gamedrawing.util.OnDrawerClosedListener
import uk.shakhzod.gamedrawing.util.extensions.onClick
import javax.inject.Inject

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@AndroidEntryPoint
class DrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawingBinding

    private val viewModel: DrawingViewModel by viewModels()

    private val args: DrawingActivityArgs by navArgs()

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rvPlayers: RecyclerView

    @Inject
    lateinit var clientId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subsribeToUiStateUpdates()
        listenToConnectionEvents()
        listenToSocketEvents()

        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        toggle.syncState()
        binding.drawingView.roomName = args.roomName

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
        binding.drawingView.setOnDrawListener {
            if (binding.drawingView.isUserDrawing) {
                viewModel.sendBaseModel(it)
            }
        }
        binding.ibUndo.onClick {
            if (binding.drawingView.isUserDrawing){
                binding.drawingView.undo()
                viewModel.sendBaseModel(DrawAction(DrawAction.ACTION_UNDO))
            }
        }
    }

    private fun selectColor(color: Int) {
        binding.drawingView.setColor(color)
        binding.drawingView.setThickness(DEFAULT_PAINT_THICKNESS)
    }

    private fun subsribeToUiStateUpdates() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedColorButtonId.collect { id ->
                binding.colorGroup.check(id)
                when (id) {
                    R.id.rbBlack -> selectColor(Color.BLACK)
                    R.id.rbBlue -> selectColor(
                        ContextCompat.getColor(
                            this@DrawingActivity,
                            android.R.color.holo_blue_dark
                        )
                    )
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
        lifecycleScope.launchWhenStarted {
            viewModel.connectionProgressBarVisible.collect { isVisible ->
                binding.connectionProgressBar.isVisible = isVisible
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.chooseWordOverlayVisible.collect { isVisible ->
                binding.chooseWordOverlay.isVisible = isVisible
            }
        }
    }

    private fun listenToSocketEvents() = lifecycleScope.launchWhenStarted {
        viewModel.socketEvent.collect { event ->
            when (event) {
                is DrawingViewModel.SocketEvent.DrawDataEvent -> {
                    val drawData = event.data
                    if (!binding.drawingView.isUserDrawing) {
                        when (drawData.motionEvent) {
                            MotionEvent.ACTION_DOWN -> binding.drawingView.startedTouchExternally(
                                drawData
                            )
                            MotionEvent.ACTION_MOVE -> binding.drawingView.movedTouchExternally(
                                drawData
                            )
                            MotionEvent.ACTION_UP -> binding.drawingView.releasedTouchExternally(
                                drawData
                            )
                        }
                    }
                }
                is DrawingViewModel.SocketEvent.UndoEvent -> {
                    binding.drawingView.undo()
                }
                is DrawingViewModel.SocketEvent.GameErrorEvent -> {
                    when (event.data.errorType) {
                        GameError.ERROR_ROOM_NOT_FOUND -> finish()
                    }
                }
                else -> Unit
            }
        }
    }

    private fun listenToConnectionEvents() = lifecycleScope.launchWhenStarted {
        viewModel.connectionEvent.collect { event ->
            when (event) {
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    viewModel.sendBaseModel(
                        JoinRoomHandshake(
                            args.username, args.roomName, clientId
                        )
                    )
                    viewModel.setConnectionProgressBarVisibility(false)
                }
                is WebSocket.Event.OnConnectionFailed -> {
                    viewModel.setConnectionProgressBarVisibility(false)
                    Snackbar.make(
                        binding.root,
                        R.string.error_connection_failed,
                        Snackbar.LENGTH_LONG
                    ).show()
                    event.throwable.printStackTrace()
                }
                is WebSocket.Event.OnConnectionClosed -> {
                    viewModel.setConnectionProgressBarVisibility(false)
                }
                else -> Unit
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}