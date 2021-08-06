package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.opengl.Visibility
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

    private fun imageTag(i:Int):String{
        return "imageTag_$i"
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            val recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)
            recordLinearLayout.removeAllViews()

            //record_title
            val recordTitle = mainView.findViewById<TextView>(R.id.record_title)
            val tenantid = JC.tenantid
            val hpid = JC.hpid
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

                val ll_right = rightLL()
                ll_right.visibility = View.GONE
                ll_right.tag = logoTag(i)
                ll_mm.addView(ll_right)


                mLinearLayout.addView(ll_mm)


                mLinearLayout.setBackgroundColor(Color.WHITE)
                mLinearLayout.setPadding(30)

                //check tenantid
                if(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].startflg=="1"){
                    mLinearLayout.setBackgroundColor(Color.GREEN)

                    ll_right.visibility = View.VISIBLE
                }


                //データ　発信
                mLinearLayout.setOnClickListener {

                    //
                    for (j in 0..size - 1) {
                        val ll =
                            recordLinearLayout.findViewWithTag<LinearLayout>(tagName + "_" + j)
                        ll.setBackgroundColor(Color.WHITE)

                        val rightLL=
                            recordLinearLayout.findViewWithTag<LinearLayout>(logoTag(j))
                        rightLL.visibility = View.GONE
                    }

                    ll_right.visibility = View.VISIBLE

                    GlobalScope.launch(errorHandler) {
                        withContext(Dispatchers.IO) {
                            try {

                                val r: String =
                                    JC.pEtOfficeSetTenant.post(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid)
                                Log.e(TAG, "tenantid post r=" + r)


                                //check tenantid
                                if (r == "0") {
                                    for (k in  0 .. JC.pEtOfficeSetTenant.infoJson().result.tenantlist.size -1 ){

                                        if(JC.pEtOfficeSetTenant.infoJson().result.tenantlist[k].startflg=="1"){
                                            mLinearLayout.setBackgroundColor(Color.GREEN)



                                            JC.tenantid =
                                                JC.pEtOfficeSetTenant.infoJson().result.tenantlist[k].tenantid
                                            JC.hpid =
                                                JC.pEtOfficeSetTenant.infoJson().result.tenantlist[k].hpid

                                            val r: String =
                                                JC.pEtOfficeLogin.post(JC.uid,JC.password)
                                            Log.e(TAG, "JC.pEtOfficeLogin.post r=" + r)


                                            recordTitle.text = "TENANTID = "+JC.tenantid+" HPID = "+JC.hpid
                                        }

                                    }
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

    private fun logoTag(i: Int) = "logo$i"

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


}