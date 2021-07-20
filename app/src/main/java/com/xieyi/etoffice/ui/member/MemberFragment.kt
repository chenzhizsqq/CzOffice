package com.xieyi.etoffice.ui.member

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
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

        for (i in 0 until 4){



            val ll= LinearLayout(activity)

            ll.setOrientation(LinearLayout.HORIZONTAL)

            ll.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


            //image logo
            val imageView = makeImage(180)

            ll.addView(imageView)


            //info left
            val tl_Left = funTableLayoutL()
            ll.addView(tl_Left)

            //info right
            val tl_right = funTableLayoutR()
            tl_right.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT)
            ll.addView(tl_right)

            //recordLinearLayout setting
            recordLinearLayout.setBackgroundColor(Color.WHITE)
            recordLinearLayout.setPadding(10)

            //over to add
            recordLinearLayout.addView(ll)

            //線
            val mLinearLayout2= LinearLayout(activity)
            val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
            mLinearLayout2.layoutParams = lp2
            mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
            recordLinearLayout.addView(mLinearLayout2)

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

    private fun makeImage(size:Int):ImageView {
        val imageView = ImageView(activity)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.icons8_plus_50, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(size, size)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)

        return imageView
    }



    private fun funTableLayoutL(): TableLayout {
        val r = TableLayout(activity)


        makeRowLeft(r,"321")
        makeRowLeft(r,"okeqe3dfdsa")
        makeRowLeft(r,"fgfds")


        return r
    }


    private fun funTableLayoutR(): TableLayout {
        val r = TableLayout(activity)


        makeRowRightS(r,"ok1432432")

        makeRowRight(r,"ok22")

        makeRowRight(r,"ok343")

        r.gravity = Gravity.RIGHT;
        return r
    }

    private fun makeRowLeft(r: TableLayout,s:String) {
        val tableRow = TableRow(activity)
        val t = makeText(s)
        tableRow.addView(t)
        r.addView(tableRow)
    }

    private fun makeRowRight(r: TableLayout, s:String) {
        val tr = TableRow(activity)

        tr.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
        val text = makeText(s)
        text.gravity = Gravity.RIGHT;

        tr.addView(text)
        tr.gravity = Gravity.RIGHT;

        r.addView(tr)
    }

    private fun makeRowRightS(r: TableLayout, s:String) {
        val tr = TableRow(activity)

        tr.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)


        val text_mr = makeText(" ● ")
        text_mr.gravity = Gravity.RIGHT;
        tr.addView(text_mr)
        text_mr.textSize = 20F
        text_mr.setTextColor(Color.GREEN)

        val text = makeText(s)
        text.gravity = Gravity.RIGHT;

        tr.addView(text)
        tr.gravity = Gravity.RIGHT;

        r.addView(tr)
    }

    private fun makeText(s:String):TextView {
        val t = TextView(activity)
        t.text = s
        return t
    }
}