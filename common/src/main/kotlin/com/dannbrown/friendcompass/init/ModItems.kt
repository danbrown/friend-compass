package com.dannbrown.friendcompass.init

import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.friendcompass.content.items.FriendCompassItem
import com.dannbrown.friendcompass.init.ModContent.REGISTRATE
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import java.util.function.Supplier

object ModItems {

  val FRIEND_COMPASS = REGISTRATE.item<FriendCompassItem>("friend_compass")
    .factory { p -> FriendCompassItem(p.rarity(Rarity.RARE).stacksTo(1)) }
    .recipe { c, p ->
      c.simpleShapedRecipe(
        { p.get() }, arrayOf(" G ", "GDG", " G "), mapOf(
          'G' to Supplier { DataIngredient(Items.IRON_INGOT) },
          'D' to Supplier { DataIngredient(Items.ENDER_PEARL) },
        ), 1
      )
    }
    .register()

  fun register() {
    // init
  }
}