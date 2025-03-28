package com.dannbrown.friendcompass.fabric

import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.world.entity.player.Player

object LoaderCompatImpl {
  @JvmStatic
  fun getFakePlayer(): Class<out Player> {
    return FakePlayer::class.java
  }
}