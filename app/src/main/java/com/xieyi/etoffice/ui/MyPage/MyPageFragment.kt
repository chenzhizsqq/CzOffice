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

    private val TAG: String? = "MyPageFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        try {

            var mUserName: TextView = view.findViewById(R.id.user_name)
            mUserName.text = JC.pEtOfficeLogin.infoLoginResult().username

            var mUserMail: TextView = view.findViewById(R.id.user_mail)
            mUserMail.text = JC.pEtOfficeLogin.infoLoginResult().mail

            var mNameValue: TextView = view.findViewById(R.id.name_value)
            mNameValue.text = JC.pEtOfficeLogin.infoLoginResult().username

            var mMobileValue: TextView = view.findViewById(R.id.mobile_value)
            mMobileValue.text = JC.pEtOfficeLogin.infoLoginResult().phone

            var mMailValue: TextView = view.findViewById(R.id.mail_value)
            mMailValue.text = JC.pEtOfficeLogin.infoLoginResult().mail


        } catch (e: Exception) {
            Log.e(TAG, "TAG", e)
        }

        //Place　Setting
        val pTableRowPlaceManagement: TableRow =
            view.findViewById(R.id.place_management) as TableRow
        pTableRowPlaceManagement.setOnClickListener(View.OnClickListener {
//            val intent = Intent(activity, MyPagePlaceSettingActivity::class.java)
//            startActivity(intent)

            Thread {
                try {
                    val r: String = JC.pEtOfficeGetUserLocation.post()                    //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$r")

                    Navigation.findNavController(view)
                    .navigate(R.id.MyPagePlaceSettingFragment);        //就是用这句去转了
                }catch (e:Exception){               //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$e")

                }
            }.start()

        })

        //change　company
        val pTableRow: TableRow = view.findViewById(R.id.change_company) as TableRow
        pTableRow.setOnClickListener(View.OnClickListener {
//            val intent = Intent(activity, MyPageChangeCompanyActivity::class.java)
//            startActivity(intent)

            Thread {
                try {
                    val r = JC.pEtOfficeGetTenant.post()                    //Json 送信
                    Log.e(TAG, "pEtOfficeGetTenant.post() :$r")

                    Navigation.findNavController(view)
                        .navigate(R.id.MyPageChangeCompanyFragment);
                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetTenant.post() :$e")

                }
            }.start()

        })


        //ログアウト
        val pTableLayout: TableLayout = view.findViewById(R.id.SYSTEM_info) as TableLayout
        pTableLayout.setOnClickListener(View.OnClickListener {

            val mMyPageLogoutDialog = MyPageLogoutDialog()
            mMyPageLogoutDialog.setTargetFragment(this@MyPageFragment, 1)
            fragmentManager?.let { it1 -> mMyPageLogoutDialog.show(it1, "mMyPageLogoutDialog") }
//            val intent = Intent(activity, LoginActivity::class.java)
//            startActivity(intent)
//            getActivity()?.finish()
        })

        return view
    }

    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}