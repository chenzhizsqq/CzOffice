package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.LoginActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class MyPageChangeCompanyFragment : Fragment() {

    private val TAG = "MyPageChangeCompanyFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private val tagName: String = "ChangeCompany"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin", )
    }

    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.activity_my_page_change_company, container, false)

        mainViewUpdate()


        return mainView
    }

    private fun mainViewUpdate() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r =
                        JC.pEtOfficeGetTenant.post()                                    //Json 送信
                    Log.e(TAG, "pEtOfficeGetTenant.post() :$r")

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetTenant.post() :$e")
                }

                doOnUiCode()
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            val recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)
            recordLinearLayout.removeAllViews()

            //record_title
            val recordTitle = mainView.findViewById<TextView>(R.id.record_title)
            val tenantid = JC.pEtOfficeLogin.infoJson().result.tenantid
            val hpid = JC.pEtOfficeLogin.infoJson().result.hpid
            recordTitle.text = "TENANTID = $tenantid HPID = $hpid"

            Log.e(TAG, "JC.pEtOfficeGetTenant:"+JC.pEtOfficeGetTenant.lastJson )

            val size = JC.pEtOfficeGetTenant.infoJson().result.tenantlist.size

            for (i in 0..size - 1) {

                val mLinearLayout = LinearLayout(activity)

                mLinearLayout.setOrientation(LinearLayout.VERTICAL)

                mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


                //mLinearLayout tag
                mLinearLayout.tag = tagName + "_" + i



                val ll_mm = LinearLayout(activity)

                ll_mm.setOrientation(LinearLayout.HORIZONTAL)

                ll_mm.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                ll_mm.gravity = (Gravity.LEFT or Gravity.CENTER)

                val ll_left = leftLL(i)


                ll_mm.addView(ll_left)



                mLinearLayout.addView(ll_mm)


                mLinearLayout.setBackgroundColor(Color.WHITE)
                mLinearLayout.setPadding(30)

                //check tenantid
                if(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].startflg=="1"){
                    mLinearLayout.setBackgroundColor(Color.GREEN)

                    val ll_right = rightLL()
                    ll_mm.addView(ll_right)
                }
//                if (JC.pEtOfficeGetTenant.infoJson().result.tenantlist[0].tenantid == JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid) {
//                    mLinearLayout.setBackgroundColor(Color.GREEN)
//                }


                //データ　発信
                mLinearLayout.setOnClickListener {
                    GlobalScope.launch(errorHandler) {
                        withContext(Dispatchers.IO) {
                            try {
                                //
                                for (j in 0..size - 1) {
                                    val ll =
                                        recordLinearLayout.findViewWithTag<LinearLayout>(tagName + "_" + j)
                                    ll.setBackgroundColor(Color.WHITE)
                                }

                                val r: String =
                                    JC.pEtOfficeSetTenant.post(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid)
                                Log.e(TAG, "tenantid post r=" + r)


                                //check tenantid
                                if (r == "0") {
                                    for (k in  0 .. JC.pEtOfficeGetTenant.infoJson().result.tenantlist.size -1 ){

                                        if(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[k].startflg=="1"){

                                            JC.pEtOfficeLogin.infoJson().result.tenantid =
                                                JC.pEtOfficeGetTenant.infoJson().result.tenantlist[k].tenantid
                                            JC.pEtOfficeLogin.infoJson().result.hpid =
                                                JC.pEtOfficeGetTenant.infoJson().result.tenantlist[k].hpid

                                            saveUserInfo(
                                                JC.pEtOfficeGetTenant.infoJson().result.tenantlist[k].tenantid ,
                                                JC.pEtOfficeGetTenant.infoJson().result.tenantlist[k].hpid)
                                            mLinearLayout.setBackgroundColor(Color.GREEN)


                                            val intent = Intent(activity, LoginActivity::class.java)
                                            startActivity(intent)
                                            activity?.finish()
                                        }

                                    }
                                    //mLinearLayout.setBackgroundColor(Color.GREEN)
                                }

                            } catch (e: Exception) {
                                Log.e(TAG, "mLinearLayout.setOnClickListener: ", e)
                            }
                        }
                    }
                }
                //mLinearLayout touch end


                recordLinearLayout.addView(mLinearLayout)


                //線

                val mLinearLayout2 = LinearLayout(activity)
                val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
                mLinearLayout2.layoutParams = lp2
                mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
                recordLinearLayout.addView(mLinearLayout2)

            }


            //returnHome
            val returnHome = mainView.findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {

                Navigation.findNavController(mainView)
                    .navigate(R.id.MyPageFragment);

            }
        }
    }

    private fun leftLL(i: Int): LinearLayout {
        val ll_left = LinearLayout(activity)

        ll_left.orientation = LinearLayout.VERTICAL
        ll_left.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        //up
        val textView = TextView(activity)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        textView.setTextColor(Color.parseColor("#000000"))
        textView.text = JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantname
        ll_left.addView(textView)




        //down
        val textViewDown = TextView(activity)
        textViewDown.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        textViewDown.setTextColor(Color.parseColor("#000000"))
        textViewDown.text = JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].posturl
        ll_left.addView(textViewDown)

        return ll_left
    }

    private fun rightLL(): LinearLayout {
        val ll_right = LinearLayout(activity)

        ll_right.orientation = LinearLayout.VERTICAL
        ll_right.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        ll_right.gravity = (Gravity.CENTER or Gravity.RIGHT)

        ll_right.addView(makeImage(100))
        return ll_right
    }


    private fun makeImage(size:Int):ImageView {
        val imageView = ImageView(activity)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_baseline_account_circle_24_blue, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(size, size)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)

        return imageView
    }

    /**
     * 存储用户信息
     */
    private fun saveUserInfo(tenantid:String,hpid:String) {
        val userInfo = activity?.getSharedPreferences(Config.appName, AppCompatActivity.MODE_PRIVATE)
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            }
        userInfo?.registerOnSharedPreferenceChangeListener(changeListener)

        val editor = userInfo?.edit()
        editor?.putString("tenantid", tenantid)
        editor?.putString("hpid", hpid)
        editor?.commit()
    }

    /**
     * 读取用户信息
     */
    private fun userInfo(){
        val userInfo = activity?.getSharedPreferences(Config.appName, AppCompatActivity.MODE_PRIVATE)
        val tenantid = userInfo?.getString("tenantid", Config.tenantid)
        val hpid = userInfo?.getString("hpid", Config.hpid)
        Log.e(TAG, "读取用户信息")
        Log.e(
            TAG,
            "tenantid:$tenantid， hpid:$hpid"
        )
    }
}