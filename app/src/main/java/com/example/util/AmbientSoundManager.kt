package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.AmbientSoundType
import java.util.Random

class AmbientSoundManager {
  private var audioTrack: AudioTrack? = null
  private var isPlaying = false
  private var audioThread: Thread? = null
  private var currentSoundType = AmbientSoundType.NONE

  fun playSound(type: AmbientSoundType) {
    if (currentSoundType == type && isPlaying) return
    stopSound()
    if (type == AmbientSoundType.NONE) return

    currentSoundType = type
    isPlaying = true

    val sampleRate = 22050
    val minBufferSize = AudioTrack.getMinBufferSize(
      sampleRate,
      AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT
    )
    val bufferSize = (minBufferSize * 2).coerceAtLeast(sampleRate / 4)

    try {
      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(bufferSize)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()

      audioTrack = track
      track.play()

      audioThread = Thread {
        val buffer = ShortArray(bufferSize / 2)
        val random = Random()
        var phase = 0.0
        var b0 = 0.0
        var b1 = 0.0
        var b2 = 0.0

        while (isPlaying && !Thread.currentThread().isInterrupted) {
          when (type) {
            AmbientSoundType.RAIN -> {
              for (i in buffer.indices) {
                val white = random.nextDouble() * 2.0 - 1.0
                b0 = 0.99765 * b0 + white * 0.0990460
                b1 = 0.96300 * b1 + white * 0.2965164
                b2 = 0.57000 * b2 + white * 1.0526913
                val pink = (b0 + b1 + b2 + white * 0.1848) * 0.08
                buffer[i] = (pink.coerceIn(-1.0, 1.0) * Short.MAX_VALUE * 0.35).toInt().toShort()
              }
            }
            AmbientSoundType.OCEAN -> {
              val swell = (Math.sin(phase) + 1.0) / 2.0
              phase += 0.0005
              if (phase > Math.PI * 2) phase = 0.0
              for (i in buffer.indices) {
                val white = random.nextDouble() * 2.0 - 1.0
                b0 = 0.99 * b0 + white * 0.1
                val wave = b0 * (0.1 + 0.3 * swell)
                buffer[i] = (wave.coerceIn(-1.0, 1.0) * Short.MAX_VALUE * 0.3).toInt().toShort()
              }
            }
            AmbientSoundType.FIRE -> {
              for (i in buffer.indices) {
                val white = random.nextDouble() * 2.0 - 1.0
                b0 = 0.98 * b0 + white * 0.05
                val crackle = if (random.nextDouble() < 0.003) (random.nextDouble() * 0.8) else 0.0
                val sample = b0 * 0.2 + crackle
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE * 0.4).toInt().toShort()
              }
            }
            AmbientSoundType.SNOW -> {
              for (i in buffer.indices) {
                val white = random.nextDouble() * 2.0 - 1.0
                b0 = 0.85 * b0 + white * 0.15
                buffer[i] = (b0.coerceIn(-1.0, 1.0) * Short.MAX_VALUE * 0.15).toInt().toShort()
              }
            }
            AmbientSoundType.FOREST -> {
              phase += 0.001
              val breeze = (Math.sin(phase) + 1.0) * 0.1
              for (i in buffer.indices) {
                val white = random.nextDouble() * 2.0 - 1.0
                b0 = 0.95 * b0 + white * 0.08
                val bird = if (random.nextDouble() < 0.0008) Math.sin(i * 0.3) * 0.3 else 0.0
                val sample = b0 * breeze + bird
                buffer[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE * 0.25).toInt().toShort()
              }
            }
            else -> {
              buffer.fill(0)
            }
          }
          track.write(buffer, 0, buffer.size)
        }
      }.apply {
        priority = Thread.MIN_PRIORITY
        start()
      }
    } catch (_: Exception) {
      stopSound()
    }
  }

  fun stopSound() {
    isPlaying = false
    currentSoundType = AmbientSoundType.NONE
    audioThread?.interrupt()
    audioThread = null
    try {
      audioTrack?.stop()
      audioTrack?.release()
    } catch (_: Exception) {}
    audioTrack = null
  }
}
