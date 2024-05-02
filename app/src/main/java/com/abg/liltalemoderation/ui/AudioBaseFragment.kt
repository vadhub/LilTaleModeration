package com.abg.liltalemoderation.ui

import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.abg.liltalemoderation.App
import com.abg.liltalemoderation.data.local.SaveInternalHandle
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.repository.FileRepository
import com.abg.liltalemoderation.model.PlaylistHandler
import com.abg.liltalemoderation.model.pojo.Audio

open class AudioBaseFragment : BaseFragment() {

    protected val load: FileViewModel by activityViewModels {
        LoadViewModelFactory(
            FileRepository(
                SaveInternalHandle(thisContext),
                (activity?.application as App).database.audioDao(),
                RemoteInstance
            )
        )
    }

    protected val player: ExoPlayer by lazy {
        ExoPlayer.Builder(thisContext).build()
    }

    protected fun prepareAudioHandler(): PlaylistHandler {
        var changePlayItemTemp: () -> Unit = {}

        val play: (audio: Audio, changePlayItem: () -> Unit) -> Unit =
            { audio: Audio, changePlayItem: () -> Unit ->
                load.getUri(audio)
                changePlayItemTemp = changePlayItem
            }

        load.uriAudio.observe(viewLifecycleOwner) {
            player.setMediaItem(MediaItem.fromUri(it))
            player.prepare()
            player.play()
            changePlayItemTemp.invoke()
        }

        return PlaylistHandler(player, play)
    }
}