package com.xieyi.etoffice.ui.member

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC

class MemberFragment : Fragment() {

    private val TAG = "MemberFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin", )

        //データ更新
        Thread {
            try {

                val r = JC.pEtOfficeGetStuffList.post()
                Log.e(TAG, "pEtOfficeGetStuffList.post() :$r")


            }catch (e:Exception){
                Log.e(TAG, "pEtOfficeGetStuffList.post() :$e")

            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_member, container, false)

        val recordLinearLayout = view.findViewById<LinearLayout>(R.id.record_linearLayout)

        val title = view.findViewById<TextView>(R.id.title)

        Log.e(TAG, "JC.pEtOfficeGetStuffList:"+JC.pEtOfficeGetStuffList.lastJson )

        val size= JC.pEtOfficeGetStuffList.infoJson().result.sectionlist.size

        for (i in 0 until 3){



            val mLinearLayout= LinearLayout(activity)

            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL)

            mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


            //image logo
            val imageView = funImage()


            mLinearLayout.addView(imageView)


            var ml = greanLL()

            mLinearLayout.addView(ml)

            var ml_bb = greanBB()

            mLinearLayout.addView(ml_bb)



            recordLinearLayout.addView(mLinearLayout)

        }



        //call_telephone 電話します
        val pTableRowInfoTitle: TextView = view.findViewById(R.id.call_telephone) as TextView
        pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
            var textTitle:CharSequence = pTableRowInfoTitle.text;
            val uri: Uri = Uri.parse("tel:"+textTitle)
            val intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        })

        return view

    }

    private fun greanLL(): LinearLayout {
        var ml = funML()
        val imageView2 = funImage2()
        val imageView3 = funImage2()
        val imageView4 = funImage2()

        ml.addView(imageView2)
        ml.addView(imageView3)
        ml.addView(imageView4)

        ml.setBackgroundColor(Color.GREEN)
        return ml
    }

    private fun greanBB(): LinearLayout {
        var ml = funML2()
        val imageView2 = funImage2()
        val imageView3 = funImage2()
        val imageView4 = funImage2()

        ml.addView(imageView2)
        ml.addView(imageView3)
        ml.addView(imageView4)

        ml.setBackgroundColor(Color.BLUE)
        return ml
    }

    private fun funML():LinearLayout {
        val ml = LinearLayout(activity)

        ml.orientation = LinearLayout.VERTICAL

        ml.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT)

        return ml
    }

    private fun funML2():LinearLayout {
        val ml = LinearLayout(activity)

        ml.orientation = LinearLayout.VERTICAL

        ml.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        return ml
    }

    private fun funImage():ImageView {
        val imageView = ImageView(activity)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.icons8_plus_50, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(200, 200)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)

        return imageView
    }

    private fun funImage2():ImageView {
        val imageView = ImageView(activity)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_home_black_24dp, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(50, 50)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)

        return imageView
    }
}