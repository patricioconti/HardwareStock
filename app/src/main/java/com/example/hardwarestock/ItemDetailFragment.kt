package com.example.hardwarestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hardwarestock.data.Item
import com.example.hardwarestock.data.getFormattedPrice
import com.example.hardwarestock.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment that displays the details of the selected item.
 */
class ItemDetailFragment : Fragment() {

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory( requireActivity().application,
            (activity?.application as StockApplication).database.itemDao()
        )
    }

    lateinit var item: Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }

    private fun editItem() {
        //Pass Edit title and item id to AddItemFragment and navigate to it
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            item.id
        )
        this.findNavController().navigate(action)
    }

    private fun bind(item: Item) {
        binding.apply {
            itemName.text = item.itemName
            itemType.text = item.itemType
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()
            //Disable sellItem button if stock is O
            sellItem.isEnabled = viewModel.isStockAvailable(item)
            //Set a click listener to the Sell button and call the sellItem() function on viewModel
            sellItem.setOnClickListener { viewModel.sellItem(item) }
            //Set a click listener to the delete button and showConfirmationDialog
            deleteItem.setOnClickListener { showConfirmationDialog() }
            //Set click listener for edit button (pencil) and execute editItem func
            editItem.setOnClickListener { editItem() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }


    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
