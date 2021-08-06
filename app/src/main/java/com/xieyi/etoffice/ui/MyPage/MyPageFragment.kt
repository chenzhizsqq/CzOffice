package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC


class MyPageFragment : Fragment() {

    private val TAG: String = "MyPageFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.e(TAG, "onCreateView: begin")

        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        try {

            val mUserName: TextView = view.findViewById(R.id.user_name)
            mUserName.text = JC.pEtOfficeLogin.infoLoginResult().username

            val mUserMail: TextView = view.findViewById(R.id.user_mail)
            mUserMail.text = JC.pEtOfficeLogin.infoLoginResult().mail

            val mNameValue: TextView = view.findViewById(R.id.name_value)
            mNameValue.text = JC.pEtOfficeLogin.infoLoginResult().username

            val mMobileValue: TextView = view.findViewById(R.id.mobile_value)
            mMobileValue.text = JC.pEtOfficeLogin.infoLoginResult().phone

            val mMailValue: TextView = view.findViewById(R.id.mail_value)
            mMailValue.text = JC.pEtOfficeLogin.infoLoginResult().mail


        } catch (e: Exception) {
            Log.e(TAG, "TAG", e)
        }

        //Place　Setting
        val pTableRowPlaceManagement: TableRow =
            view.findViewById(R.id.place_management) as TableRow
        pTableRowPlaceManagement.setOnClickListener(View.OnClickListener {


                    Navigation.findNavController(view)
                    .navigate(R.id.MyPagePlaceSettingFragment);


        })

        //change　company
        val pTableRow: TableRow = view.findViewById(R.id.change_company) as TableRow
        pTableRow.setOnClickListener(View.OnClickListener {

                    Navigation.findNavController(view)
                        .navigate(R.id.MyPageChangeCompanyFragment);


        })


        //ログアウト
        val pTableLayout: TableLayout = view.findViewById(R.id.SYSTEM_info) as TableLayout
        pTableLayout.setOnClickListener(View.OnClickListener {

            val mMyPageLogoutDialog = MyPageLogoutDialog()

            val fragmentManager = this@MyPageFragment.parentFragmentManager
            fragmentManager.let { it1 -> mMyPageLogoutDialog.show(it1, "mMyPageLogoutDialog")  }

        })

        return view
    }

    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}