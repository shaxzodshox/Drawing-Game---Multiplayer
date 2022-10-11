package uk.shakhzod.gamedrawing.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.databinding.FragmentCreateRoomBinding

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

class CreateRoomFragment : Fragment(R.layout.fragment_create_room) {
    private var _binding: FragmentCreateRoomBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateRoomBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}