package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetStuffList
import kotlinx.coroutines.*


class MemberFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private val TAG = javaClass.simpleName

    private lateinit var mainView: View

    private lateinit var pEtOfficeGetStuffList : EtOfficeGetStuffList

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pEtOfficeGetStuffList = EtOfficeGetStuffList()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_member, container, false)

        refreshPage()

        mSwipeRefreshLayout= mainView.findViewById(R.id.swipeRefreshLayout)

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = mainView.findViewById(R.id.recycler_view_stuff_list)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        Log.e(TAG, "onScrollStateChanged: more date", )
                    }
                }

            }
        })
        return mainView
    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r = pEtOfficeGetStuffList.post()
                    Log.e(TAG, "pEtOfficeGetStuffList.post() :$r")

                    doOnUiCode()
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetStuffList.post()", e)
                }
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    // UI更新
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            val recyclerView: RecyclerView = mainView.findViewById(R.id.recycler_view_stuff_list)
            recyclerView.adapter = GetStuffSectionListAdapter(pEtOfficeGetStuffList.infoJson().result.sectionlist,requireActivity())
        }
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }
}