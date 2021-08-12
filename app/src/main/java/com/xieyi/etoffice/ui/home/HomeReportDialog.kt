package com.xieyi.etoffice.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.EtOfficeGetStatusList

import kotlinx.coroutines.*


class HomeReportDialog : DialogFragment() {
    private val TAG: String = "HomeReportDialog"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private lateinit var pEtOfficeGetStatusList : EtOfficeGetStatusList

    private lateinit var mainView: View
    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.dialog_home_report, container)

        pEtOfficeGetStatusList = EtOfficeGetStatusList()


        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r = pEtOfficeGetStatusList.post()
                    Log.e(TAG, "pEtOfficeGetStatusList.post() :$r")
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetStatusList.post()",e)
                }

                doOnUiCode()
            }
        }

        return mainView
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            //フルスクリーン　Full screen
            val window = dialog!!.window
            window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val attributes = window.attributes
            //attributes.gravity = Gravity.BOTTOM //下方
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
            window.attributes = attributes


            //ボタン　保存後に閉じる
            val btnSaveAndClose = mainView.findViewById<TextView>(R.id.btn_save_and_close)
            btnSaveAndClose.setOnClickListener {
                dialog!!.dismiss()
            }

            //record_linearLayout
            val recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)
            recordLinearLayout.setPadding(10)


            val size = pEtOfficeGetStatusList.infoJson().result.recordlist.size

            for (i in 0..size - 1) {

                val mLinearLayout = LinearLayout(activity)

                mLinearLayout.setOrientation(LinearLayout.HORIZONTAL)

                val lp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                mLinearLayout.layoutParams = lp


                //left
                val textView = TextView(activity)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F);
                textView.setTextColor(Color.parseColor("#000000"))
                textView.text =
                    Tools.allDateTime(pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustime)

                mLinearLayout.addView(textView)


                //right
                val textViewRight = TextView(activity)
                textViewRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F);
                textViewRight.layoutParams = FrameLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                val rightText = (
                        pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustext
                        + " " + pEtOfficeGetStatusList.infoJson().result.recordlist[i].memo )
                textViewRight.text = rightText

                textViewRight.gravity = Gravity.RIGHT;
                mLinearLayout.addView(textViewRight)



                mLinearLayout.setPadding(10)
                recordLinearLayout.addView(mLinearLayout)


                //線

                val mLinearLayout2 = LinearLayout(activity)
                val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
                mLinearLayout2.layoutParams = lp2
                mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
                recordLinearLayout.addView(mLinearLayout2)

            }
        }
    }


    private fun makeTextView(text: String): TextView? {
        val textView = TextView(activity)
        textView.text = text
        textView.setPadding(5, 1, 5, 1)
        return textView
    }
}
