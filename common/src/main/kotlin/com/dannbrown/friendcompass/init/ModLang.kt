package com.dannbrown.friendcompass.init

import com.dannbrown.friendcompass.content.items.FriendCompassItem
import com.dannbrown.friendcompass.init.ModContent.REGISTRATE

object ModLang {
  init {
    REGISTRATE.langs()
      .addRawLang(FriendCompassItem.OFFLINE_KEY, "§cLinked player§r %s §cis offline, teleport was canceled§r")
      .addRawLang(
        FriendCompassItem.DIMENSION_KEY,
        "§cLinked player§r %s §cis in another dimension, sneak and try again!§r"
      )
      .addRawLang(FriendCompassItem.SELF_KEY, "§cYou can't teleport to yourself!§r")
      .addRawLang(
        FriendCompassItem.DESCRIPTION_KEY,
        "This compass will be linked to its owner, allowing other players to teleport to them."
      )
      .addRawLang(FriendCompassItem.IS_LINKED_TO_KEY, "When used, teleports to§r %s")
  }


  fun register() {
    // init
  }
}