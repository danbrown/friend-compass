package com.dannbrown.friendcompass.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.CreativeTabsUtil
import net.minecraft.world.item.ItemStack

object ModContent {
  const val MOD_ID = "friendcompass"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

//  val TAB = REGISTRATE.creativeTab(MOD_ID, "Friend Compass", { ItemStack(ModItems.MOD_ICON.get()) }, { p, o ->
//    CreativeTabsUtil.displayAll(
//      REGISTRATE, p, o
//    )
//  })

  fun init() {
    ModConfig.register()
    ModSounds.register()
    ModTags.register()
    ModBlocks.register()
    ModEntityTypes.register()
    ModItems.register()
    ModParticles.register()
    ModConfiguredFeatures.register()
    ModPlacedFeatures.register()
    ModBlockEntities.register()
    ModModelLayers.register()
    ModPlacerTypes.register()
    ModLang.register()
    ModBiomeModifiers.register()
    REGISTRATE.buildRegistries()
  }
}