package com.example.etoffice.ui.member

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.etoffice.R

class MemberFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_member, container, false)


        //call_telephone 電話します
        val pTableRowInfoTitle: TextView = root.findViewById(R.id.call_telephone) as TextView
        pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
            var textTitle:CharSequence = pTableRowInfoTitle.text;
            val uri: Uri = Uri.parse("tel:"+textTitle)
            val intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        })

        return root

    }
}