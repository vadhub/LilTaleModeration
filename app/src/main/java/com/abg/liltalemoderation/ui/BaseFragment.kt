package com.abg.liltalemoderation.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.abg.liltalemoderation.data.local.SaveConfiguration
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.repository.ComplaintRepository

open class BaseFragment : Fragment() {

    protected lateinit var thisContext: Context
    protected lateinit var configuration: SaveConfiguration
    protected val reportViewModel: ReportViewModel by activityViewModels {
        ReportViewModelFactory(ComplaintRepository(RemoteInstance))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.thisContext = context
        configuration = SaveConfiguration(thisContext)
    }

}