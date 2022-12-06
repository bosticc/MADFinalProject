package com.example.goodhabits.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.goodhabits.R
import com.example.goodhabits.adapter.ListAdapter
import com.example.goodhabits.databinding.FragmentHomeBinding
import com.example.goodhabits.utils.hideKeyboard
import com.example.goodhabits.utils.shortToast
import com.example.goodhabits.viewmodel.TodoViewModel
import com.example.goodhabits.viewmodel.TodoViewModelFactory
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var viewModel: TodoViewModel

    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        activity?.hideKeyboard()
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = TodoViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]

        mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = ListAdapter(viewModel)

        val mDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("EEEE, MMM dd yyyy")
        val currentDate = formatter.format(mDate)

        binding.apply {
            date.text = currentDate
            addTaskBtn.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_addFragment)
            )
        }

        // Setup RecyclerView
        setupRecyclerview()

        viewModel.getAllTodos().observe(viewLifecycleOwner, { list ->
            adapter.setData(list)

            if (list.isEmpty()) {
                binding.noDataImage.visibility = View.VISIBLE
                binding.noDataText.visibility = View.VISIBLE
                binding.totalTask.text = "0"
            } else {
                binding.noDataImage.visibility = View.GONE
                binding.noDataText.visibility = View.GONE
                binding.totalTask.text = list.size.toString()
            }
        })

        viewModel.getAllCompleted().observe(viewLifecycleOwner, { list ->
            if (list.isNotEmpty()) binding.completed.text = list.size.toString()
            else binding.completed.text = "0"
        })
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.rvTodo
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = FadeInUpAnimator().apply {
            addDuration = 100
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val delete: String

        when (item.itemId) {
            R.id.delete_selected -> {
                delete = "selected"
                showDialog(delete)
            }

            R.id.delete_all -> {
                delete = "all"
                showDialog(delete)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(delete: String) {
        if (delete == "selected") {

            AlertDialog.Builder(requireContext(), R.style.MyDialogTheme).apply {
                setTitle("Delete Selected Tasks")
                setMessage("This will delete the selected tasks. Would you like to continue?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteSelected()

                    context.shortToast("All selected tasks have been deleted.")
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            }.create().show()

        } else if (delete == "all") {

            AlertDialog.Builder(requireContext(), R.style.MyDialogTheme).apply {
                setTitle("Delete ALL tasks")
                setMessage("All tasks will be deleted. Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.clearTodos()

                    context.shortToast("All of the tasks have been deleted.")
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            }.create().show()
        }
    }

}