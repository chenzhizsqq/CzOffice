package com.xieyi.etoffice.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportAddViewModel  : ViewModel() {
//    private val searchLiveData = MutableLiveData<RequestBody>()
    val projectList = ArrayList<Project>()

//    val projectLiveData = Transformations.switchMap(searchLiveData) { query ->
//        Repository.searchProject(query)
//    }
//    fun searchProject(body: RequestBody) {
//        searchLiveData.value = body
//    }

    val projectCd = MutableLiveData<String>()
    var wbsPickerData = ArrayList<OptionItem>()
    fun getProjectWbsOption(){
        if (projectList != null) {
            for(i in 0 until projectList!!.size){
                var itemCd = projectList[i].projectcd
                if(itemCd == projectCd.value){
                    for(wbs in projectList[i].wbslist) {
                        wbsPickerData.add(OptionItem(wbs.wbscd, wbs.wbsname))
                    }
                    break
                }
            }
        }
    }

    val projectPickerData = ArrayList<OptionItem>()
    fun initProjectOption(){
        for(i in 0 until projectList!!.size){
            var code = projectList[i].projectcd
            val name = projectList[i].projectname
            projectPickerData.add(OptionItem(code, name))
        }
    }

}