package com.sunnyweather.android.ui.weather.placemanage

import androidx.lifecycle.*
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.PlaceManage
import com.sunnyweather.android.logic.repository.Repository
import kotlinx.coroutines.launch

class PlaceManageViewModel: ViewModel() {

    private val refreshPlaceManageLiveData = MutableLiveData<Any?>()

    private val _toastLiveData = MutableLiveData<String>()

    val toastLiveData: LiveData<String>
        get() = _toastLiveData

    val placeManageList = ArrayList<PlaceManage>()

    val placeManageLiveData = refreshPlaceManageLiveData.switchMap { _ ->
        Repository.loadAllPlaceManages()
    }

    fun addPlaceManage(placeManage: PlaceManage) {
        viewModelScope.launch {
            val result = Repository.addPlaceMange(placeManage)
            if (result.isSuccess) {
                _toastLiveData.value = "地点已添加"
                refreshPlaceManageLiveData.value = refreshPlaceManageLiveData.value
            } else {
                _toastLiveData.value = "地点添加失败"
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun deletePlaceManage(lng: String, lat: String){
        viewModelScope.launch {
            val result = Repository.deletePlaceManage(lng, lat)
            if (result.isSuccess) {
                _toastLiveData.value = "已删除"
                refreshPlaceManageLiveData.value = refreshPlaceManageLiveData.value
            } else {
                _toastLiveData.value = "地点删除失败"
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun updatePlaceManage(placeManage: PlaceManage) {
        viewModelScope.launch {
            val result = Repository.updatePlaceManage(placeManage)
            if (result.isSuccess) {
                refreshPlaceManageLiveData.value = refreshPlaceManageLiveData.value
            } else {
                _toastLiveData.value = "地点更新失败"
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun refreshPlaceManage() {
        refreshPlaceManageLiveData.value = refreshPlaceManageLiveData.value
    }

    fun clearToast() {
        _toastLiveData.value = ""
    }

    fun savePlace(place: Place) = Repository.savePlace(place)
}