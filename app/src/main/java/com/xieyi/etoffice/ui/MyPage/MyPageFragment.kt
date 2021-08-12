package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class MyPageFragment : Fragment() {

    private val TAG: String = "MyPageFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")

    }
    private lateinit var mainView: View


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private fun mainViewUpdate() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r =
                        JC.pEtOfficeUserInfo.post()                                    //Json 送信
                    Log.e(TAG, "pEtOfficeUserInfo.post() :$r")

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeUserInfo.post()",e)
                }

                doOnUiCode()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.e(TAG, "onCreateView: begin")

        mainView = inflater.inflate(R.layout.fragment_my_page, container, false)
        mainViewUpdate()

        return mainView
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            try {

                val mUserName: TextView = mainView.findViewById(R.id.user_name)
                mUserName.text = JC.pEtOfficeUserInfo.infoJson().result.username

                val mUserMail: TextView = mainView.findViewById(R.id.user_mail)
                mUserMail.text = JC.pEtOfficeUserInfo.infoJson().result.mail

                val mNameValue: TextView = mainView.findViewById(R.id.name_value)
                mNameValue.text = JC.pEtOfficeUserInfo.infoJson().result.username

                val mMobileValue: TextView = mainView.findViewById(R.id.mobile_value)
                mMobileValue.text = JC.pEtOfficeUserInfo.infoJson().result.phone

                val mMailValue: TextView = mainView.findViewById(R.id.mail_value)
                mMailValue.text = JC.pEtOfficeUserInfo.infoJson().result.mail

            } catch (e: Exception) {
                Log.e(TAG, "doOnUiCode", e)
            }

            //Place　Setting
            val pTableRowPlaceManagement: LinearLayout =
                mainView.findViewById(R.id.place_management) as LinearLayout
            pTableRowPlaceManagement.setOnClickListener(View.OnClickListener {
                Navigation.findNavController(mainView)
                    .navigate(R.id.MyPagePlaceSettingFragment)

            })

            //change　company
            val pTableRow: LinearLayout = mainView.findViewById(R.id.change_company) as LinearLayout
            pTableRow.setOnClickListener(View.OnClickListener {
                Navigation.findNavController(mainView)
                    .navigate(R.id.MyPageChangeCompanyFragment)
            })


            //ログアウト
            val pTableLayout: TableLayout = mainView.findViewById(R.id.SYSTEM_info) as TableLayout
            pTableLayout.setOnClickListener(View.OnClickListener {

                val mMyPageLogoutDialog = MyPageLogoutDialog()
                val fragmentManager = this@MyPageFragment.parentFragmentManager
                fragmentManager.let { it1 -> mMyPageLogoutDialog.show(it1, "mMyPageLogoutDialog") }

            })
        }
    }


    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}