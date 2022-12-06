package com.example.goodhabits.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.goodhabits.R
import com.example.goodhabits.databinding.FragmentAddBinding
import com.example.goodhabits.utils.hideKeyboard
import com.example.goodhabits.utils.shortToast
import com.example.goodhabits.viewmodel.TodoViewModel
import com.example.goodhabits.viewmodel.TodoViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater)
        val viewModelFactory = TodoViewModelFactory.getInstance(requireContext())
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]

        binding.submitButton.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()

            if (title.isNotBlank() && description.isNotBlank()) {
                todoViewModel.addTodo(title, description)
                activity?.hideKeyboard()
                findNavController().popBackStack()
            } else {
                context?.shortToast(getString(R.string.fill_all_fields))
            }
        }

        return binding.root
    }

}