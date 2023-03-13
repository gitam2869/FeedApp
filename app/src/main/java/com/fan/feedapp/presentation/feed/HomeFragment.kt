package com.fan.feedapp.presentation.feed

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.videoplayer.exo.ExoMediaPlayer
import com.app.videoplayer.player.VideoView
import com.app.videoplayer.render.TextureRenderViewFactory
import com.app.videoplayer.util.Utils
import com.fan.feedapp.data.local.model.TextData
import com.fan.feedapp.databinding.FragmentHomeBinding
import com.fan.feedapp.domain.model.Video
import com.fan.feedapp.presentation.feed.data.Feed
import com.fan.feedapp.presentation.feed.data.Loading
import com.fan.feedapp.presentation.feed.image.ImageViewModel
import com.fan.feedapp.presentation.feed.text.TextViewModel
import com.fan.feedapp.presentation.feed.video.VideosViewModel
import com.fan.feedapp.presentation.feed.viewholder.VideoViewHolder
import com.fan.feedapp.utils.ViewExtension.gone
import com.fan.feedapp.utils.ViewExtension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val READ_STORAGE_PERMISSION_CODE = 121

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val videosViewModel: VideosViewModel by viewModels()
    private val imagesViewModel: ImageViewModel by viewModels()
    private val textViewModel: TextViewModel by viewModels()

    private var start = 0
    private var end = 2
    private lateinit var feedAdapter: FeedAdapter
    private var feedList = mutableListOf<Feed>()
    private var loadPosition = 6
    private var isLoading = false

    private var mCurPos = -1
    var mVideoView: VideoView<ExoMediaPlayer>? = null
    var previousVideoHolder: VideoViewHolder? = null
    private var isFirstTime = true
    private var videoListener: VideoView.OnStateChangeListener? = null

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        callListeners()

        showLoading()
        getVideoList()
    }

    private fun initComponent() {
        initVideoView()
        addText(5)

        feedAdapter = FeedAdapter()
        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = feedAdapter
        }
    }

    private fun callListeners() {
        registerVideoDataList()
        registerImageDataList()
        registerTextDataList()
        recyclerViewListener()
    }

    private fun recyclerViewListener() {
        binding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //滚动停止
                    autoPlayVideo(recyclerView)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val layout: LinearLayoutManager =
                        recyclerView.layoutManager as LinearLayoutManager
                    if (layout.findLastVisibleItemPosition() >= loadPosition || layout.findLastVisibleItemPosition() == feedList.size - 1) {
                        addText(2)
                        isLoading = true
                        loadPosition += 7

                        addLoader()
                        getVideoList()
                    }
                }
            }
        })
    }


    private fun registerVideoDataList() {
        lifecycleScope.launch {
            videosViewModel.videoList.collect {
                if (it.error.isNotBlank()) {
                    getImageList()
                }

                it.data?.let { list ->
                    removeLoader()
                    feedList.addAll(list)
                    getImageList()
                }
            }
        }
    }

    private fun getVideoList() {
        videosViewModel.getVideoList()
    }

    private fun registerImageDataList() {
        lifecycleScope.launch {
            imagesViewModel.imageList.collect {
                if (it.error.isNotBlank()) {
                    getTextList()
                }

                it.data?.let { list ->
                    feedList.addAll(list)
                    getTextList()
                }
            }
        }
    }

    private fun getImageList() {
        if (checkRequirePermissions()) {
            imagesViewModel.getImageList(start, end)
            incrementImageRange()
        }
    }

    private fun incrementImageRange() {
        start = end
        end += 2
    }


    private fun registerTextDataList() {
        lifecycleScope.launch {
            textViewModel.textList.collect {
                if (it.error.isNotBlank()) {
                    removeLoader()
                    isLoading = false
                    updateAdapter()
                }

                it.data?.let { list ->
                    removeLoader()
                    isLoading = false
                    hideLoading()
                    feedList.addAll(list)
                    updateAdapter()
                }
            }
        }
    }

    private fun getTextList() {
        textViewModel.getTextList()
    }

    private fun addText(limit: Int) {
        for (i in 1..limit)
            textViewModel.addText(TextData("Dummy Text"))
    }

    private fun showLoading() {
        binding.pbLoading.visible()
    }

    private fun hideLoading() {
        binding.pbLoading.gone()
    }

    /******** Recyclerview Adapter **********/
    private fun updateAdapter() {
        val newList = feedList
        feedAdapter.submitList(newList)

        if (isFirstTime) {
            if (feedList[0] is Video) {
                binding.rvFeed.post {
                    val itemView: View =
                        binding.rvFeed.layoutManager?.findViewByPosition(0) ?: return@post
                    val viewHolder = itemView.tag as VideoViewHolder
                    startPlay(0, viewHolder)
                    isFirstTime = false
                }
            }
        }
    }

    private fun removeLoader() {
        if (feedList.isNotEmpty()) {
            val position = feedList.size - 1
            if (feedList[position] is Loading) {
                feedList.removeAt(position)
                updateAdapter()
            }
        }
    }

    private fun addLoader() {
        feedList.add(Loading(Feed.TYPE_LOADING))
        updateAdapter()
    }


    /********** Video playing related methods ************/

    private fun autoPlayVideo(view: RecyclerView?) {
        if (view == null) return

        var isViewFind = false
        var index = 0

        val count = view.childCount
        for (i in 0 until count) {
            val itemView = view.getChildAt(i) ?: continue
            val holder = itemView.tag
            if (holder is VideoViewHolder) {
                val rect = Rect()
                holder.binding.playerContainer.getLocalVisibleRect(rect)
                val height: Int = holder.binding.playerContainer.height
                if (rect.top == 0 && rect.bottom == height) {
                    startPlay(holder.adapterPosition, holder)
                    isViewFind = true
                    index = i
                    break
                }
            }
        }

        for (i in index + 1 until count) {
            val itemView = view.getChildAt(i) ?: continue
            val holder = itemView.tag
            if (holder is VideoViewHolder) {
                holder.binding.pbLoading.gone()
                holder.binding.ivThumbnail.visible()
            }
        }

        for (i in 0 until index - 1) {
            val itemView = view.getChildAt(i) ?: continue
            val holder = itemView.tag
            if (holder is VideoViewHolder) {
                holder.binding.pbLoading.gone()
                holder.binding.ivThumbnail.visible()
            }
        }

        if (!isViewFind)
            releaseVideoView()
    }

    private fun initVideoView() {
        mVideoView = VideoView(requireContext())
        mVideoView?.setLooping(false)
        mVideoView?.keepScreenOn = true
        mVideoView?.setRenderViewFactory(TextureRenderViewFactory.create())

    }

    private fun registerVideoStateChangeListener() {
        videoListener = object : VideoView.OnStateChangeListener {
            override fun onPlayerStateChanged(playerState: Int) {
            }

            override fun onPlayStateChanged(playState: Int) {
                when (playState) {
                    VideoView.STATE_PREPARED,
                    VideoView.STATE_BUFFERED,
                    VideoView.STATE_PLAYING -> {
                        mVideoView?.keepScreenOn = true
                        hideVideoLoadingView()
                    }

                    VideoView.STATE_BUFFERING, VideoView.STATE_PREPARING -> {
                        showVideoLoadingView()
                    }

                    VideoView.STATE_PAUSED -> {
                        previousVideoHolder?.binding?.ivThumbnail?.visible()
                    }

                    VideoView.STATE_ERROR -> {
                        previousVideoHolder?.binding?.pbLoading?.gone()
                    }
                }
            }
        }
        videoListener?.let { mVideoView?.addOnStateChangeListener(it) }
    }

    private fun unRegisterVideoStateChangeListener() {
        videoListener?.let { mVideoView?.removeOnStateChangeListener(it) }
    }

    private fun startPlay(position: Int, videoViewHolder: VideoViewHolder) {
        if (mCurPos == position) return

        if (mCurPos != -1) {
            releaseVideoView()
        }
        unRegisterVideoStateChangeListener()

        val video = feedList[position] as Video
        mVideoView?.setUrl(video.url)
        Utils.removeViewFormParent(mVideoView)
        videoViewHolder.binding.playerContainer.addView(mVideoView, 0)
        mVideoView?.start()
        mCurPos = position
        previousVideoHolder = videoViewHolder

        previousVideoHolder?.let {
            it.binding.pbLoading.visible()
            it.binding.ivThumbnail.visible()
        }
        registerVideoStateChangeListener()
    }

    private fun releaseVideoView() {
        mVideoView?.release()
        mCurPos = -1
    }

    private fun showVideoLoadingView() {
        previousVideoHolder?.binding?.run {
            pbLoading.visible()
            ivThumbnail.visible()
        }
    }

    private fun hideVideoLoadingView() {
        previousVideoHolder?.binding?.run {
            pbLoading.gone()
            ivThumbnail.gone()
        }
    }

    /******** Permissino Related ********/
    private fun checkRequirePermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                READ_STORAGE_PERMISSION_CODE
            )

            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val granted = checkPermissionGranted(
            requestCode,
            permissions,
            grantResults
        )

        if (granted) {
            getImageList()
        } else {
            checkRequirePermissions()
        }

    }

    private fun checkPermissionGranted(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        when (requestCode) {
            READ_STORAGE_PERMISSION_CODE -> {
                return (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
        return false
    }

    /*********** Life cycle related methods *************/
    override fun onPause() {
        super.onPause()
        mVideoView?.keepScreenOn = false
        mVideoView?.pause()
    }

    override fun onStop() {
        super.onStop()
        mVideoView?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}