package com.example.goodhabits.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.goodhabits.R
import com.example.goodhabits.databinding.FragmentEditBinding
import com.example.goodhabits.utils.hideKeyboard
import com.example.goodhabits.utils.shortToast
import com.example.goodhabits.viewmodel.TodoViewModel
import com.example.goodhabits.viewmodel.TodoViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mTodo = EditFragmentArgs.fromBundle(requireArguments()).todo
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater).apply {
            todo = mTodo
        }

        val viewModelFactory = TodoViewModelFactory.getInstance(requireContext())
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]

        binding.submitButton.setOnClickListener {
            val updatedTitle = binding.title.text.toString()
            val updatedDesc = binding.description.text.toString()

            if (updatedTitle.isNotBlank() && updatedDesc.isNotBlank()) {
                todoViewModel.updateTodo(mTodo!!.id, updatedTitle, updatedDesc, mTodo.checked)
                activity?.hideKeyboard()
                findNavController().popBackStack()
            } else {
                context?.shortToast(getString(R.string.fill_all_fields))
            }
        }

        return binding.root
    }

}