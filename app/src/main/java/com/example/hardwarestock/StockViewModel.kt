package com.example.hardwarestock


import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.hardwarestock.data.Item
import com.example.hardwarestock.data.ItemDao
import com.example.hardwarestock.data.SettingsDataStore
import com.example.hardwarestock.worker.ReminderWorker
import com.example.hardwarestock.worker.cancelNotifications
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

const val UPDATE_REMINDER_WORK_NAME = "update_reminder_work"

class StockViewModel( private val application: Application,private val itemDao: ItemDao) : ViewModel() {




    // Initialize SettingsDataStore
    private var settingsDataStore = SettingsDataStore(application)

    val readFromDataStore = settingsDataStore.readFromDataStore.asLiveData()

fun saveToDataStore (isUpdateReminderChecked:Boolean, context: Context) = viewModelScope.launch {
    settingsDataStore.saveToDataStore(isUpdateReminderChecked,context)
}


    //The getItems() function returns a Flow. To consume the data as a LiveData value,
    // use the asLiveData() function
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()


    //Add a private function called insertItem() that takes in an Item object
    //and adds the data to the database in a non-blocking way.
    private fun insertItem(item: Item) {

        viewModelScope.launch {
            itemDao.insert(item)
        }

    }

    //Add a private function that takes in 4 strings and returns an Item instance.
    // It transforms price to Double and Quantity to Int to match types with Item in database.
    private fun getNewItemEntry(
        itemName: String,
        itemType: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            itemName = itemName,
            itemType = itemType,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    //This fun will be called from the UI fragment to add Item details to the database.
    fun addNewItem(itemName: String, itemType: String, itemPrice: String, itemCount: String) {
        // It transforms price to Double and Quantity to Int to match types with Item in database.
        val newItem = getNewItemEntry(itemName, itemType, itemPrice, itemCount)
        //Make a call to insertItem() passing in the newItem to add the new entity to the database.
        insertItem(newItem)
    }

    //Check neither text fields are empty
    fun isEntryValid(
        itemName: String,
        itemType: String,
        itemPrice: String,
        itemCount: String
    ): Boolean {
        if (itemName.isBlank() || itemType.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    //Add a private function called updateItem() that takes in an Item object
    //and updates the data to the database in a non-blocking way.
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    //Fun to decrease quantity when selling and update quantity
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            // This function is used to copy an object for changing some of its properties,
            // but keeping the rest of the properties unchanged.
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    //This fun will help disabling the Sell button when there is no item to sell
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    //Add a function called deleteItem() that takes in an Item object
    //and updates the data to the database in a non-blocking way.
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }


    //Add a function called retrieveItem() that takes in an id
    //and get item by id from the database and convert it as liveData for observing.
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }


    //Add a private function that takes in item id and 4 strings and returns an Item instance.
    // It transforms price to Double and Quantity to Int to match types with Item in database.
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemType: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemType = itemType,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

//This function also takes an Int and 4 strings for the entity details and returns nothing
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemType: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemType, itemPrice, itemCount)
    //This fun calls the Dao and updates the data to the database in a non-blocking way.
    updateItem(updatedItem)
    }

    /**
     * Notification using work manager.
     */

    //Create WorkManager
    private val workManager = WorkManager.getInstance(application)

    fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
          ) {

        //Cancel active notifications to avoid overlapping
        cancelActiveNotifications()


        // Generate a OneTimeWorkRequest with the passed in duration, time unit.

        val reminderRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            //I use delay for delaying notification
            .setInitialDelay(duration, unit)
            .build()

        //Enqueue the request as a unique work request

        workManager
            .enqueueUniqueWork(
                UPDATE_REMINDER_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                reminderRequest
            )

    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(UPDATE_REMINDER_WORK_NAME)
    }


    /**
     * Cancel active notifications
     */

    private fun cancelActiveNotifications() {

    val notificationManager =
        ContextCompat.getSystemService(
            application,
            NotificationManager::class.java
        ) as NotificationManager
    notificationManager.cancelNotifications()

    }





}


//Create ViewModelFactory (standard code for future projects)
class StockViewModelFactory(private val application: Application, private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(application,itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}