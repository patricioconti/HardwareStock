package com.example.hardwarestock

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hardwarestock.databinding.FragmentItemListBinding
import java.util.concurrent.TimeUnit

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {



    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory( requireActivity().application,
            (activity?.application as StockApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        //Read From Data Store when changes are observed on preference Flow and set it to updateReminderEnabled
        viewModel.readFromDataStore.observe(viewLifecycleOwner) { isUpdateReminderChecked ->

            //If updateReminderEnabled then schedule Reminder

            if (isUpdateReminderChecked) {

                //Call reminder (it will push a notification using WorkManager)
                viewModel.scheduleReminder(5, TimeUnit.SECONDS)

            }
        }



        val adapter = ItemListAdapter { item ->

            val action =   ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(item.id)
            this.findNavController().navigate(action)

        }
        binding.recyclerView.adapter = adapter

       //Attach an observer on the allItems to listen for the data changes.
       //Call submitList() on the adapter and pass in the new list.
       // This will update the RecyclerView with the new items on the list.
      viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
          items.let {
              adapter.submitList(it)
           }
      }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            //Pass the fragment title as argument to Add item fragment an navigate to it
            //This is because I will re use add item fragment also as an update item fragment
            //and in that case I will need to update fragment title
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }


}