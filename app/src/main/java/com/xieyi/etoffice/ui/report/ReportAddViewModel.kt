package com.xieyi.etoffice.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xieyi.etoffice.common.model.ProjectInfo

class ReportAddViewModel : ViewModel() {
    val projectList = ArrayList<ProjectInfo>()
    var minuteList = ArrayList<String>()
    var hourList = ArrayList<String>()

    val projectCd = MutableLiveData<String>()
    var reportAddDate = ""
    var wbsPickerData = ArrayList<OptionItemModel>()
    fun getProjectWbsOption() {
        if (projectList != null) {
            for (i in 0 until projectList.size) {
                var itemCd = projectList[i].projectcd
                if (itemCd == projectCd.value) {
                    for (wbs in projectList[i].wbslist) {
                        wbsPickerData.add(OptionItemModel(wbs.wbscd, wbs.wbsname))
                    }
                    break
                }
            }
        }
    }

    val projectPickerData = ArrayList<OptionItemModel>()
    fun initProjectOption() {
        for (i in 0 until projectList.size) {
            var code = projectList[i].projectcd
            val name = projectList[i].projectname
            projectPickerData.add(OptionItemModel(code, name))
        }
    }

    fun initTime() {
        for (i in 0..24) {
            var hour = i.toString()
            if (i < 10) {
                hour = "0$i"
            }
            hourList.add(hour)
        }
        for (i in 0..11) {
            var temp = (i * 5).toString()
            if (i * 5 < 10) {
                temp = "0$temp"
            }
            minuteList.add(temp)
        }
    }
}