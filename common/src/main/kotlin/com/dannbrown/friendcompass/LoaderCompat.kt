package com.dannbrown.friendcompass

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.entity.player.Player
import java.lang.AssertionError

object LoaderCompat {
  @JvmStatic
  @ExpectPlatform
  fun getFakePlayer(): Class<out Player> {
    throw AssertionError()
  }
}