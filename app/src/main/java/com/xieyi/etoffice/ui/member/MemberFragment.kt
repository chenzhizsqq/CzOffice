package com.xieyi.etoffice.ui.member

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
}