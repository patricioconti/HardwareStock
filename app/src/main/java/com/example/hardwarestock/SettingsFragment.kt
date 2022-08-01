package com.example.hardwarestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hardwarestock.databinding.FragmentSettingsBinding
import java.util.concurrent.TimeUnit

/**
 * Fragment for settings
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    //Declare Update Reminder checked
    private var isUpdateReminderChecked = true


    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory( requireActivity().application,
            (activity?.application as StockApplication).database.itemDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Read From Data Store when changes are observed on preference Flow and change switch state
        //accordingly
        viewModel.readFromDataStore.observe(viewLifecycleOwner) { isUpdateReminderChecked ->
            binding.updateReminderSwitch.isChecked = isUpdateReminderChecked

        }

        //Setup a checked change listener for updateReminderSwitch to save preference
        binding.updateReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            //get updateReminderSwitch current values and store on isUpdateReminderChecked
            isUpdateReminderChecked = binding.updateReminderSwitch.isChecked
            // Call viewModel and write the update reminder setting in the preference Datastore
            viewModel.saveToDataStore(isUpdateReminderChecked,requireContext())

            //If updateReminderEnabled then schedule Reminder
            if (isUpdateReminderChecked) {
                //Call reminder (it will push a notification using WorkManager)
                viewModel.scheduleReminder(5, TimeUnit.SECONDS)

            }

            //If UpdateReminder is not checked, cancel work (disable schedule notification)
            else viewModel.cancelWork()

        }





    }

}