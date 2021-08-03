package com.xieyi.etoffice.ui.report

data class ProjectResponse(val status:String, val result:Result, val message:String)
data class Result(val projectlist:ArrayList<Project>)
data class Project(val projectcd:String, val projectname:String, val wbslist:ArrayList<Wbs>)
data class Wbs(val wbscd:String, val wbsname:String, val date:String, val time:String)
data class OptionItem(val code:String, val name:String)
data class ReportResponse(val status:String, val result: String, val message:String)
