package com.xieyi.etoffice.ui.member

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*

class MemberFragment : Fragment() {

    private val TAG = javaClass.simpleName


    private val REQUEST_CALL_PERMISSION = 10111 //電話　申し込む



    private lateinit var mainView: View

    private lateinit var recordLinearLayout: LinearLayout


    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin")


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.e(TAG, "onCreateView: begin")

        mainView = inflater.inflate(R.layout.fragment_member, container, false)

        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r = JC.pEtOfficeGetStuffList.post()
                    Log.e(TAG, "pEtOfficeGetStuffList.post() :$r")
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetStuffList.post() :$e")
                }

                doOnUiCode()
            }
        }

        return mainView

    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }


    // UI更新
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {

            recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)

            Log.e(TAG, "JC.pEtOfficeGetStuffList:"+JC.pEtOfficeGetStuffList.lastJson )

            val sectionlistSize = JC.pEtOfficeGetStuffList.infoJson().result.sectionlist.size
            for (j in 0 until sectionlistSize){

                val size= JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist.size

                for (i in 0 until size){



                    val ll= LinearLayout(activity)

                    ll.setOrientation(LinearLayout.HORIZONTAL)

                    ll.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


                    //image logo
                    val imageView = makeImage(180)

                    ll.addView(imageView)


                    //info left
                    val tl_Left = funTableLayoutL(
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].userkana,
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].username,
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].phone,
                    )
                    tl_Left.minimumWidth = 500
                    ll.addView(tl_Left)

                    //info right
                    val tl_right = funTableLayoutR(
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].tenant,
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].hpid,
                        JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].mail,
                    )
                    tl_right.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT)
                    ll.addView(tl_right)

                    //recordLinearLayout setting
                    recordLinearLayout.setBackgroundColor(Color.WHITE)
                    recordLinearLayout.setPadding(10)

                    //telephone
                    recordLinearLayout.setOnClickListener(View.OnClickListener {
                        if (ContextCompat.checkSelfPermission(
                                requireActivity(),
                                Manifest.permission.CALL_PHONE
                            ) !== PackageManager.PERMISSION_GRANTED
                        ) {
                            // CALL_PHONE 権利　ない
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf<String>(Manifest.permission.CALL_PHONE),
                                REQUEST_CALL_PERMISSION
                            )
                        } else {
                            //CALL_PHONE 権利　ある
                            val uri: Uri = Uri.parse("tel:"+JC.pEtOfficeGetStuffList.infoJson().result.sectionlist[j].stufflist[i].phone)
                            val intent = Intent(Intent.ACTION_CALL, uri)
                            startActivity(intent)
                        }
                    })

                    //over to add
                    recordLinearLayout.addView(ll)

                    //線
                    val mLinearLayout2= LinearLayout(activity)
                    val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
                    mLinearLayout2.layoutParams = lp2
                    mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
                    recordLinearLayout.addView(mLinearLayout2)

                }
            }





            //call_telephone 電話します
            val pTableRowInfoTitle: TextView = mainView.findViewById(R.id.call_telephone) as TextView
            pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
                var textTitle:CharSequence = pTableRowInfoTitle.text;
                val uri: Uri = Uri.parse("tel:"+textTitle)
                val intent = Intent(Intent.ACTION_CALL, uri)
                startActivity(intent)
            })
        }
    }

    private fun makeImage(size:Int):ImageView {
        val imageView = ImageView(activity)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_baseline_add_circle_outline_24, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(size, size)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)

        return imageView
    }



    private fun funTableLayoutL(t1:String,t2:String,t3:String): TableLayout {
        val r = TableLayout(activity)


        makeRowLeft(r,t1,14F)
        makeRowLeft(r,t2,20F)
        makeRowLeft(r,t3,14F)


        return r
    }


    private fun funTableLayoutR(t1:String,t2:String,t3:String): TableLayout {
        val r = TableLayout(activity)


        makeRowRightS(r,t1)

        makeRowRight(r,t2)

        makeRowLeft(r,t3,14F)

        r.gravity = Gravity.RIGHT;
        return r
    }

    private fun makeRowLeft(r: TableLayout,s:String ,ts:Float) {
        val tableRow = TableRow(activity)
        val t = makeText(s)
        t.textSize = ts
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