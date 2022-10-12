package uk.shakhzod.gamedrawing.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.databinding.FragmentUsernameBinding
import uk.shakhzod.gamedrawing.ui.setup.SetupViewModel
import uk.shakhzod.gamedrawing.util.Constants
import uk.shakhzod.gamedrawing.util.Constants.MAX_USERNAME_LENGTH
import uk.shakhzod.gamedrawing.util.Constants.MIN_USERNAME_LENGTH
import uk.shakhzod.gamedrawing.util.extensions.navigateSafely
import uk.shakhzod.gamedrawing.util.extensions.onClick
import uk.shakhzod.gamedrawing.util.extensions.snackbar

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@AndroidEntryPoint
class UsernameFragment : Fragment(R.layout.fragment_username) {
    private var _binding: FragmentUsernameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SetupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsernameBinding.bind(view)
        listenToEvents()

        binding.btnNext.onClick {
            viewModel.validateUsernameAndNavigateToSelectRoom(
                binding.etUsername.text.toString()
            )
        }
    }

    private fun listenToEvents() {
        lifecycleScope.launchWhenCreated {
            viewModel.setupEvent.collect { event ->
                when (event) {
                    is SetupViewModel.SetupEvent.NavigateToSelectRoomEvent -> {
                        findNavController().navigateSafely(
                            R.id.action_usernameFragment_to_selectRoomFragment,
                            args = Bundle().apply { putString("username", event.username) }
                        )
                    }
                    is SetupViewModel.SetupEvent.InputEmptyError -> {
                        snackbar(R.string.error_field_empty)
                    }
                    is SetupViewModel.SetupEvent.InputTooShortError -> {
                        snackbar(getString(R.string.error_username_too_short, MIN_USERNAME_LENGTH))
                    }
                    is SetupViewModel.SetupEvent.InputTooLongError -> {
                        snackbar(getString(R.string.error_username_too_long, MAX_USERNAME_LENGTH))
                    }
                    else -> Unit
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}