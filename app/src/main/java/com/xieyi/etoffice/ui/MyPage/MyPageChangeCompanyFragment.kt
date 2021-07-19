package com.xieyi.etoffice.ui.MyPage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC


class MyPageChangeCompanyFragment : Fragment() {

    private val TAG = "MyPageChangeCompanyFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private val tagName: String = "ChangeCompany"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin", )
        Thread {
            try {

                val r = JC.pEtOfficeGetTenant.post()                                    //Json 送信
                Log.e(TAG, "pEtOfficeGetTenant.post() :$r")


            }catch (e:Exception){
                Log.e(TAG, "pEtOfficeGetTenant.post() :$e")

            }
        }.start()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_my_page_change_company, container, false)

        val recordLinearLayout = view.findViewById<LinearLayout>(R.id.record_linearLayout)


        //Log.e(TAG, "JC.pEtOfficeGetTenant:"+JC.pEtOfficeGetTenant.lastJson )

        val size= JC.pEtOfficeGetTenant.infoJson().result.tenantlist.size

        for (i in 0 .. size-1){

            val mLinearLayout= LinearLayout(activity)

            mLinearLayout.setOrientation(LinearLayout.VERTICAL)

            mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


            //mLinearLayout tag
            mLinearLayout.tag = tagName+"_"+i


            //up
            val textView = TextView(activity)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
            textView.setTextColor(Color.parseColor("#000000"))
            textView.text = JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantname
            mLinearLayout.addView(textView)


            //down
            val textViewRight = TextView(activity)
            textViewRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
            textViewRight.setTextColor(Color.parseColor("#000000"))
            textViewRight.text = JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].posturl
            mLinearLayout.addView(textViewRight)


            mLinearLayout.setBackgroundColor(Color.WHITE)
            mLinearLayout.setPadding(30)

            //check tenantid
            if(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[0].tenantid == JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid){
                mLinearLayout.setBackgroundColor(Color.GREEN)
            }



            //mLinearLayout touch   begin
            mLinearLayout.setOnClickListener {
                try {
                    //
                    for (j in 0 .. size-1){
                        val ll=recordLinearLayout.findViewWithTag<LinearLayout>(tagName+"_"+j)
                        ll.setBackgroundColor(Color.WHITE)
                    }

                    val r:String = JC.pEtOfficeSetTenant.post(JC.pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid)
                    Log.e(TAG, "tenantid post r="+r )


                    //check tenantid
                    if(r=="0"){
                        mLinearLayout.setBackgroundColor(Color.GREEN)
                    }

                }catch (e:Exception){
                    Log.e(TAG, "mLinearLayout.setOnClickListener: ",e )
                }
            }
            //mLinearLayout touch end



            recordLinearLayout.addView(mLinearLayout)



            //線

            val mLinearLayout2= LinearLayout(activity)
            val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
            mLinearLayout2.layoutParams = lp2
            mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
            recordLinearLayout.addView(mLinearLayout2)

        }


        //returnHome
        val returnHome = view.findViewById<ImageView>(R.id.returnHome)
        returnHome.setOnClickListener {

            Navigation.findNavController(view)
                .navigate(R.id.MyPageFragment);

        }


        return view
    }

    fun check(){

    }
}