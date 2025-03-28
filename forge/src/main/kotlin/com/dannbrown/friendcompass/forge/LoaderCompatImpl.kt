package com.dannbrown.friendcompass.forge

import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.util.FakePlayer

object LoaderCompatImpl {
  @JvmStatic
  fun getFakePlayer(): Class<out Player> {
    return FakePlayer::class.java
  }
}