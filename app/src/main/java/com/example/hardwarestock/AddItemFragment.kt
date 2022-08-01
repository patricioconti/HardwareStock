package com.example.hardwarestock

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hardwarestock.data.Item
import com.example.hardwarestock.databinding.FragmentAddItemBinding


/**
 * Fragment to add or update an item in the Stock database.
 */
class AddItemFragment : Fragment() {

    //Standard code for factory and viewModel instance

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory( requireActivity().application,
            (activity?.application as StockApplication).database.itemDao()
        )
    }


    lateinit var item: Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Check if is entry valid calling view Model function
    private fun isEntryValid(): Boolean {

        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemType.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString()
        )

    }

    //Check if entry is valid and addNewItem with viewModel fun
    private fun addNewItem() {
        if (isEntryValid()) {

            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemType.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString(),
            )

            //Return to ItemListFragment
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)


        }
    }

    //Bind textview fields with entity data
    //Populate the text fields in the Edit Item screen with the entity details
    private fun bind(item: Item) {
        //Round the price to two decimal place
        //val price = "%.2f".format(item.itemPrice) //Bug on coma
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemType.setText(item.itemType, TextView.BufferType.SPANNABLE)
            itemPrice.setText(item.itemPrice.toString(), TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            //Set the click listener for the Save button
            saveAction.setOnClickListener { updateItem() }
        }

    }

    //Check if entry is valid and updateItem with viewModel fun
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemType.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemCount.text.toString()
            )
            //Navigate to Itemlist fragment after updating
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }




    //Add click listener to save button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId

        //On AddItemFragment screen if id is greater than 0 then item already exists so retrieve item and show data
        //If id is lower than 0 item is new so add new item when clicking on saveAction button
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null

    }



}