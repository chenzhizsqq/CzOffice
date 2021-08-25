package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.databinding.FragmentMemberBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetStuffList
import kotlinx.coroutines.*


class MemberFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private val TAG = javaClass.simpleName

    private lateinit var pEtOfficeGetStuffList : EtOfficeGetStuffList

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter:GetStuffSectionListAdapter
    
    private lateinit var binding: FragmentMemberBinding

    //暂时看到的资料数量
    private var listInt = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pEtOfficeGetStuffList = EtOfficeGetStuffList()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)

        refreshPage()

        mSwipeRefreshLayout= binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = binding.recyclerViewStuffList
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        Log.e(TAG, "onScrollStateChanged: more date", )
                        mSwipeRefreshLayout.isRefreshing = false

                        moreData(10)
                    }
                }

            }
        })
        return binding.root
    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r = pEtOfficeGetStuffList.post()
                    Log.e(TAG, "pEtOfficeGetStuffList.post() :$r")

                    doOnUiRefresh()
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
    private suspend fun doOnUiRefresh() {
        withContext(Dispatchers.Main) {
            Log.e(TAG, "doOnUiRefresh: begin", )
            val recyclerView: RecyclerView = binding.recyclerViewStuffList

            val sectionlistEmpty = ArrayList<EtOfficeGetStuffList.SectionList>()
            mAdapter=GetStuffSectionListAdapter(sectionlistEmpty,requireActivity())
            recyclerView.adapter = mAdapter

            listInt = 0
            moreData(10)



        }
    }

    //获取更多数据 add是数量
    private fun moreData(add:Int) {
        val maxInt = pEtOfficeGetStuffList.infoJson().result.sectionlist.size
        for (i in 0..add - 1) {
            if (listInt < maxInt) {
                mAdapter.notifyDataAdd(pEtOfficeGetStuffList.infoJson().result.sectionlist[listInt])
                listInt++
            }
        }
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }
}