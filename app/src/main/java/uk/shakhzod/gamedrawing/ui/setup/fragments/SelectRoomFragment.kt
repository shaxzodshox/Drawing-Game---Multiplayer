package uk.shakhzod.gamedrawing.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uk.shakhzod.gamedrawing.R
import uk.shakhzod.gamedrawing.databinding.FragmentSelectRoomBinding

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

class SelectRoomFragment : Fragment(R.layout.fragment_select_room) {
    private var _binding: FragmentSelectRoomBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectRoomBinding.bind(view)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}